package pl.edu.pjwstk.net.message.STUN.attribute;

import pl.edu.pjwstk.net.message.TLVAttribute;
import pl.edu.pjwstk.net.message.STUN.STUNAttribute;
import pl.edu.pjwstk.net.message.STUN.STUNAttributeType;
import pl.edu.pjwstk.net.message.STUN.STUNMessage;
import pl.edu.pjwstk.types.ExtendedBitSet;

/**
 * @author Robert Strzelecki rstrzele@gmail.com
 * package pl.edu.pjwstk.net.message.STUN.attribute
 */
public class STUNChangeRequest extends STUNAttribute implements TLVAttribute {

    public STUNChangeRequest(STUNMessage stunMessage, int messagePosition) {
        this(stunMessage, messagePosition, false, false);
    }

    public STUNChangeRequest(STUNMessage stunMessage, int messagePosition, boolean isChangeAddress, boolean isChangePort) {
        super(stunMessage, STUNAttributeType.CHANGE_REQUEST, messagePosition);
        String str = "0";
        str += (isChangePort ? "1" : "0");
        str += (isChangeAddress ? "1" : "0");
        attribute.set(32, new ExtendedBitSet(str, 32, false, true));
        setLength(4);
    }

    public boolean isChangeAddress() {
        return attribute.get(30);
    }

    public void setChangeAddress(boolean isChangeAddress) {
        attribute.set(30, isChangeAddress);
    }

    public boolean isChangePort() {
        return attribute.get(31);
    }

    public void setChangePort(boolean isChangePort) {
        attribute.set(30, isChangePort);
    }

    public String toString() {
        return "change address = " + attribute.get(34) + " change port = " + attribute.get(35);
    }
}
