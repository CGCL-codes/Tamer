package com.abiquo.framework.messages;

import java.util.List;
import com.abiquo.framework.domain.Property;
import com.abiquo.framework.xml.AbsXMLMessage;
import com.abiquo.framework.xml.MessageConstants;
import com.abiquo.framework.xml.events.messages.ResolveRequestXMLEvent;

public class ResolveRequest implements IGridMessage, MessageConstants {

    /** The res class. */
    private String localId;

    private int quantity;

    /** The list of the CONSTRAINS */
    private List<Property> propertyList;

    public ResolveRequest(String LocalId) {
        localId = LocalId;
        quantity = 1;
    }

    public ResolveRequest(String LocalId, int Quantity, List<Property> PropertyList) {
        localId = LocalId;
        quantity = Quantity;
        propertyList = PropertyList;
    }

    public String getLocalId() {
        return localId;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isSetPropertyList() {
        return (propertyList != null);
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public AbsXMLMessage toXML() {
        return new ResolveRequestXMLEvent(this);
    }

    public int getType() {
        return ET_RESOLVE_REQUEST;
    }
}
