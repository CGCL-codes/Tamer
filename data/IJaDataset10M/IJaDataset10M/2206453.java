package org.mobicents.slee.resource.diameter.rf.events;

import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;
import net.java.slee.resource.diameter.rf.events.avp.LocationType;
import net.java.slee.resource.diameter.rf.events.avp.ServiceInformation;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.rf.events.avp.DiameterRfAvpCodes;
import org.mobicents.slee.resource.diameter.rf.events.avp.LocationTypeImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.ServiceInformationImpl;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RfAccountingRequestImpl extends RfAccountingMessageImpl implements RfAccountingRequest {

    /**
   * @param message
   */
    public RfAccountingRequestImpl(Message message) {
        super(message);
    }

    @Override
    public ServiceInformation getServiceInformation() {
        return (ServiceInformation) super.getAvpAsCustom(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, ServiceInformationImpl.class);
    }

    @Override
    public void setServiceInformation(ServiceInformation si) {
        super.addAvp(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, si.byteArrayValue());
    }

    @Override
    public boolean hasServiceInformation() {
        return super.hasAvp(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID);
    }

    @Override
    public String getCalledStationId() {
        return super.getAvpAsUTF8String(DiameterRfAvpCodes.CALLED_STATION_ID);
    }

    @Override
    public void setCalledStationId(String csid) {
        super.addAvp(DiameterRfAvpCodes.CALLED_STATION_ID, csid);
    }

    @Override
    public boolean hasCalledStationId() {
        return super.hasAvp(DiameterRfAvpCodes.CALLED_STATION_ID);
    }

    @Override
    public LocationType getLocationType() {
        return (LocationType) super.getAvpAsCustom(DiameterRfAvpCodes.LOCATION_TYPE, LocationTypeImpl.class);
    }

    @Override
    public void setLocationType(LocationType lt) {
        super.addAvp(DiameterRfAvpCodes.LOCATION_TYPE, lt.byteArrayValue());
    }

    @Override
    public boolean hasLocationType() {
        return super.hasAvp(DiameterRfAvpCodes.LOCATION_TYPE);
    }

    @Override
    public String getLongName() {
        return "Accounting-Request";
    }

    @Override
    public String getShortName() {
        return "ACR";
    }
}
