package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.LcsRequestorId;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * LcsRequestorIdImpl.java
 *
 * <br>Project:  mobicents
 * <br>3:41:44 AM Apr 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LcsRequestorIdImpl extends GroupedAvpImpl implements LcsRequestorId {

    public LcsRequestorIdImpl() {
        super();
    }

    /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
    public LcsRequestorIdImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    public String getLcsDataCodingScheme() {
        return getAvpAsUTF8String(DiameterRoAvpCodes.LCS_DATA_CODING_SCHEME, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public String getLcsRequestorIdString() {
        return getAvpAsUTF8String(DiameterRoAvpCodes.LCS_REQUESTOR_ID_STRING, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public boolean hasLcsDataCodingScheme() {
        return hasAvp(DiameterRoAvpCodes.LCS_DATA_CODING_SCHEME, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public boolean hasLcsRequestorIdString() {
        return hasAvp(DiameterRoAvpCodes.LCS_REQUESTOR_ID_STRING, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public void setLcsDataCodingScheme(String lcsDataCodingScheme) {
        addAvp(DiameterRoAvpCodes.LCS_DATA_CODING_SCHEME, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsDataCodingScheme);
    }

    public void setLcsRequestorIdString(String lcsRequestorIdString) {
        addAvp(DiameterRoAvpCodes.LCS_REQUESTOR_ID_STRING, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsRequestorIdString);
    }
}
