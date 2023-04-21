package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.EventType;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * EventTypeImpl.java
 *
 * <br>Project:  mobicents
 * <br>11:20:17 AM Apr 11, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EventTypeImpl extends GroupedAvpImpl implements EventType {

    public EventTypeImpl() {
        super();
    }

    /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
    public EventTypeImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    public String getEvent() {
        return getAvpAsUTF8String(DiameterRoAvpCodes.EVENT, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public long getExpires() {
        return getAvpAsUnsigned32(DiameterRoAvpCodes.EXPIRES, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public String getSipMethod() {
        return getAvpAsUTF8String(DiameterRoAvpCodes.SIP_METHOD, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public boolean hasEvent() {
        return hasAvp(DiameterRoAvpCodes.EVENT, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public boolean hasExpires() {
        return hasAvp(DiameterRoAvpCodes.EXPIRES, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public boolean hasSipMethod() {
        return hasAvp(DiameterRoAvpCodes.SIP_METHOD, DiameterRoAvpCodes.TGPP_VENDOR_ID);
    }

    public void setEvent(String event) {
        addAvp(DiameterRoAvpCodes.EVENT, DiameterRoAvpCodes.TGPP_VENDOR_ID, event);
    }

    public void setExpires(long expires) {
        addAvp(DiameterRoAvpCodes.EXPIRES, DiameterRoAvpCodes.TGPP_VENDOR_ID, expires);
    }

    public void setSipMethod(String sipMethod) {
        addAvp(DiameterRoAvpCodes.SIP_METHOD, DiameterRoAvpCodes.TGPP_VENDOR_ID, sipMethod);
    }
}
