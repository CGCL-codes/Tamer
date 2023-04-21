package com.abiquo.framework.domain.messages;

import java.util.List;
import com.abiquo.framework.domain.types.IBaseType;
import com.abiquo.framework.xml.AbsXMLMessage;
import com.abiquo.framework.xml.MessageConstants;
import com.abiquo.framework.xml.events.messages.ProvisioningRequestXMLEvent;

/**
 * TODO see wiki
 */
public class ProvisioningRequest implements IGridMessage, MessageConstants {

    /** The locaion. */
    private String location;

    /** The res class. */
    private String localId;

    /** The list of the parameters */
    private List<IBaseType> baseTypeList;

    /**
	 * This method instance a new XML Provisioning Request message. Remember that can be null. TODO: redoc
	 */
    public ProvisioningRequest(String LocalId, String Location, List<IBaseType> BaseTypeList) {
        localId = LocalId;
        location = Location;
        baseTypeList = BaseTypeList;
    }

    public ProvisioningRequest(String LocalId) {
        localId = LocalId;
    }

    public boolean isSetLocation() {
        return (location != null);
    }

    public boolean isSetBaseTypeList() {
        return (baseTypeList != null);
    }

    public String getLocation() {
        return location;
    }

    public String getLocalId() {
        return localId;
    }

    public List<IBaseType> getBaseTypeList() {
        return baseTypeList;
    }

    public void setLocation(String Location) {
        location = Location;
    }

    public AbsXMLMessage toXML() {
        return new ProvisioningRequestXMLEvent(this);
    }

    public int getType() {
        return ET_PROVISIONING_REQUEST;
    }
}
