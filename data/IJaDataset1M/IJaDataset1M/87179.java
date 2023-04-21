package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.AddressDomain;
import net.java.slee.resource.diameter.ro.events.avp.AddressType;
import net.java.slee.resource.diameter.ro.events.avp.OriginatorAddress;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * OriginatorAddressImpl.java
 *
 * <br>Project:  mobicents
 * <br>10:38:15 AM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class OriginatorAddressImpl extends GroupedAvpImpl implements OriginatorAddress {

    public OriginatorAddressImpl() {
        super();
    }

    /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
    public OriginatorAddressImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    public String getAddressData() {
        return getAvpAsUTF8String(DiameterRoAvpCodes.ADDRESS_DATA, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public AddressDomain getAddressDomain() {
        return (AddressDomain) getAvpAsCustom(DiameterRoAvpCodes.ADDRESS_DOMAIN, DiameterRoAvpCodes.TGPP_VENDOR_ID, AddressDomainImpl.class);
    }

    public AddressType getAddressType() {
        return (AddressType) getAvpAsEnumerated(DiameterRoAvpCodes.ADDRESS_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, AddressType.class);
    }

    public boolean hasAddressData() {
        return hasAvp(DiameterRoAvpCodes.ADDRESS_DATA, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public boolean hasAddressDomain() {
        return hasAvp(DiameterRoAvpCodes.ADDRESS_DOMAIN, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public boolean hasAddressType() {
        return hasAvp(DiameterRoAvpCodes.ADDRESS_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public void setAddressData(String addressData) {
        addAvp(DiameterRoAvpCodes.ADDRESS_DATA, DiameterRoAvpCodes.TGPP_VENDOR_ID, addressData);
    }

    public void setAddressDomain(AddressDomain addressDomain) {
        addAvp(DiameterRoAvpCodes.ADDRESS_DOMAIN, DiameterRoAvpCodes.TGPP_VENDOR_ID, addressDomain.byteArrayValue());
    }

    public void setAddressType(AddressType addressType) {
        addAvp(DiameterRoAvpCodes.ADDRESS_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, addressType.getValue());
    }
}
