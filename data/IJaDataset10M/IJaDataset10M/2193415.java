package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link RequestedEUTRANAuthenticationInfoAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class RequestedEUTRANAuthenticationInfoAvpImpl extends GroupedAvpImpl implements RequestedEUTRANAuthenticationInfoAvp {

    /**
   * 
   */
    public RequestedEUTRANAuthenticationInfoAvpImpl() {
        super();
    }

    /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
    public RequestedEUTRANAuthenticationInfoAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    public boolean hasNumberOfRequestedVectors() {
        return hasAvp(DiameterS6aAvpCodes.NUMBER_OF_REQUESTED_VECTORS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public long getNumberOfRequestedVectors() {
        return getAvpAsUnsigned32(DiameterS6aAvpCodes.NUMBER_OF_REQUESTED_VECTORS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public void setNumberOfRequestedVectors(long numberOfRequestedVectors) {
        addAvp(DiameterS6aAvpCodes.NUMBER_OF_REQUESTED_VECTORS, DiameterS6aAvpCodes.S6A_VENDOR_ID, numberOfRequestedVectors);
    }

    public boolean hasImmediateResponsePreferred() {
        return hasAvp(DiameterS6aAvpCodes.IMMEDIATE_RESPONSE_PREFERRED, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public long getImmediateResponsePreferred() {
        return getAvpAsUnsigned32(DiameterS6aAvpCodes.IMMEDIATE_RESPONSE_PREFERRED, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public void setImmediateResponsePreferred(long immediateResponsePreferred) {
        addAvp(DiameterS6aAvpCodes.IMMEDIATE_RESPONSE_PREFERRED, DiameterS6aAvpCodes.S6A_VENDOR_ID, immediateResponsePreferred);
    }

    public boolean hasResynchronizationInfo() {
        return hasAvp(DiameterS6aAvpCodes.RESYNCHRONIZATION_INFO, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public byte[] getResynchronizationInfo() {
        return getAvpAsOctetString(DiameterS6aAvpCodes.RESYNCHRONIZATION_INFO, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public void setResynchronizationInfo(byte[] resynchronizationInfo) {
        addAvp(DiameterS6aAvpCodes.RESYNCHRONIZATION_INFO, DiameterS6aAvpCodes.S6A_VENDOR_ID, resynchronizationInfo);
    }
}
