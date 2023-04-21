package com.abiquo.framework.domain.messages;

import com.abiquo.framework.xml.AbsXMLMessage;
import com.abiquo.framework.xml.MessageConstants;
import com.abiquo.framework.xml.events.messages.UnprovisioningResponseXMLEvent;

/**
 * have no data 
 * 
 * 
 * */
public class UnprovisioningResponse implements IGridMessage, MessageConstants {

    public AbsXMLMessage toXML() {
        return new UnprovisioningResponseXMLEvent(this);
    }

    public int getType() {
        return ET_UNPROVISIONING_RESPONSE;
    }
}
