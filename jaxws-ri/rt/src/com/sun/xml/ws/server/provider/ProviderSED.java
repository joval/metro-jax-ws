/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://jwsdp.dev.java.net/CDDLv1.0.html
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * https://jwsdp.dev.java.net/CDDLv1.0.html  If applicable,
 * add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your
 * own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 */
package com.sun.xml.ws.server.provider;

import com.sun.pept.ept.MessageInfo;
import com.sun.pept.presentation.MessageStruct;
import com.sun.xml.ws.encoding.internal.InternalEncoder;
import com.sun.xml.ws.encoding.soap.internal.BodyBlock;
import com.sun.xml.ws.encoding.soap.internal.InternalMessage;
import com.sun.xml.ws.model.soap.SOAPRuntimeModel;
import javax.xml.transform.Source;

public class ProviderSED implements InternalEncoder {

    public void toMessageInfo(Object internalMessage, MessageInfo messageInfo) {
        throw new UnsupportedOperationException();
    }

    /*
     * Sets Source in InternalMessage's BodyBlock. If there is an exception
     * in MessageInfo, it is set as fault in BodyBlock
     *
     */
    public InternalMessage toInternalMessage(MessageInfo messageInfo) {
        InternalMessage internalMessage = new InternalMessage();
        switch(messageInfo.getResponseType()) {
            case MessageStruct.NORMAL_RESPONSE :
                Object obj = messageInfo.getResponse();
                if (obj instanceof Source) {
                    BodyBlock bodyBlock = new BodyBlock((Source)obj);
                    internalMessage.setBody(bodyBlock);
                } else {
                    throw new UnsupportedOperationException();
                }
                break;

            case MessageStruct.CHECKED_EXCEPTION_RESPONSE :
                // invoke() doesn't throw any checked exception
                // Fallthrough

            case MessageStruct.UNCHECKED_EXCEPTION_RESPONSE :
                SOAPRuntimeModel.createFaultInBody(messageInfo.getResponse(),
                        null, null, internalMessage);
                break;
        }
        return internalMessage;
    }

}
