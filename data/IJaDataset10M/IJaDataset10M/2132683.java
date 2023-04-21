package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.EPSLocationInformationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.MMELocationInformationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SGSNLocationInformationAvp;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link EPSLocationInformationAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EPSLocationInformationAvpImpl extends GroupedAvpImpl implements EPSLocationInformationAvp {

    public EPSLocationInformationAvpImpl() {
        super();
    }

    public EPSLocationInformationAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    public boolean hasMMELocationInformation() {
        return hasAvp(DiameterS6aAvpCodes.MME_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public void setMMELocationInformation(MMELocationInformationAvp mmeli) {
        addAvp(DiameterS6aAvpCodes.MME_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, mmeli.byteArrayValue());
    }

    public MMELocationInformationAvp getMMELocationInformation() {
        return (MMELocationInformationAvp) getAvpAsCustom(DiameterS6aAvpCodes.MME_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, MMELocationInformationAvpImpl.class);
    }

    public boolean hasSGSNLocationInformation() {
        return hasAvp(DiameterS6aAvpCodes.SGSN_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public void setSGSNLocationInformation(SGSNLocationInformationAvp sgsnli) {
        addAvp(DiameterS6aAvpCodes.SGSN_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, sgsnli.byteArrayValue());
    }

    public SGSNLocationInformationAvp getSGSNLocationInformation() {
        return (SGSNLocationInformationAvp) getAvpAsCustom(DiameterS6aAvpCodes.SGSN_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, SGSNLocationInformationAvpImpl.class);
    }
}
