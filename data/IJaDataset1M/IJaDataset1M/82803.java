package tw.edu.shu.im.iccio.tagtype;

import tw.edu.shu.im.iccio.datatype.Signature;
import tw.edu.shu.im.iccio.datatype.UInt32Number;
import tw.edu.shu.im.iccio.datatype.DeviceAttribute;
import tw.edu.shu.im.iccio.Streamable;
import tw.edu.shu.im.iccio.ICCUtils;
import tw.edu.shu.im.iccio.ICCProfileException;

/**
 * ProfileDesc is a class used by ProfileSequenceDescType to define a profile description.
 *
 * This class is not a tagged data element, but an internal struct used by
 * the ProfileSequenceDescType. Therefore, it doesn't inherit from 
 * AbstractTagType, but it needs to implement the Streamable interface for
 * handy conversion between byte array and data elements.
 * (This is where interface plays a role different from abstract class)
 * @see ProfileSequenceDescType for more description.
 *
 * Profile Description structure
 * <code>
 * 0..3		4	Device manufacturer signature (from corresponding profile's header)
 * 4..7		4	Device model signature (from corresponding profile's header)
 * 8..15	8	Device attributes (from corresponding profile's header)
 * 16..19	4	Device technology information such as CRT, Dye Sublimation, etc. (corresponding to profile's technology signature)
 * 20..m		variable displayable description of device manufacturer (profile's deviceMfgDescTag)
 * m+1..n		variable displayable description of device model (profile's deviceModelDescTag)
 * </code>
 */
public class ProfileDesc implements Streamable {

    private Signature deviceMakerSignature_;

    private Signature deviceModelSignature_;

    private DeviceAttribute deviceAttributes_;

    private UInt32Number deviceTechInfo_;

    private MultiLocalizedUnicodeType deviceMfgDescTag_;

    private MultiLocalizedUnicodeType deviceModelDescTag_;

    public ProfileDesc() {
    }

    public ProfileDesc(byte[] byteArray) throws ICCProfileException {
        fromByteArray(byteArray, 0, 0);
    }

    public ProfileDesc(byte[] byteArray, int offset) throws ICCProfileException {
        fromByteArray(byteArray, offset, 0);
    }

    public void fromByteArray(byte[] byteArray, int offset, int len) throws ICCProfileException {
        if (byteArray == null) throw new ICCProfileException("byte array null", ICCProfileException.NullPointerException);
        if (offset < 0 || offset + len > byteArray.length) throw new ICCProfileException("index out of range", ICCProfileException.IndexOutOfBoundsException);
        this.deviceMakerSignature_ = new Signature(byteArray, offset);
        this.deviceModelSignature_ = new Signature(byteArray, offset + 4);
        this.deviceAttributes_ = new DeviceAttribute(byteArray, offset + 8);
        this.deviceTechInfo_ = new UInt32Number(byteArray, offset + 16);
        int idx = offset + 20;
        if (idx + 16 > byteArray.length) this.deviceMfgDescTag_ = new MultiLocalizedUnicodeType(); else this.deviceMfgDescTag_ = new MultiLocalizedUnicodeType(byteArray, idx);
        idx += deviceMfgDescTag_.size();
        if (idx + 16 > byteArray.length) this.deviceModelDescTag_ = new MultiLocalizedUnicodeType(); else this.deviceModelDescTag_ = new MultiLocalizedUnicodeType(byteArray, idx);
    }

    public byte[] toByteArray() throws ICCProfileException {
        if (this.deviceMakerSignature_ == null) throw new ICCProfileException("data not set", ICCProfileException.InvalidDataValueException);
        int m = deviceMfgDescTag_.size();
        int len = 20 + m + deviceModelDescTag_.size();
        byte[] all = new byte[len];
        ICCUtils.appendByteArray(all, 0, this.deviceMakerSignature_);
        ICCUtils.appendByteArray(all, 4, this.deviceModelSignature_);
        ICCUtils.appendByteArray(all, 8, this.deviceAttributes_);
        ICCUtils.appendByteArray(all, 16, this.deviceTechInfo_);
        ICCUtils.appendByteArray(all, 20, this.deviceModelDescTag_);
        ICCUtils.appendByteArray(all, 20 + m, this.deviceModelDescTag_);
        return all;
    }

    public int size() {
        assert (deviceMfgDescTag_ != null);
        return 20 + deviceMfgDescTag_.size() + deviceModelDescTag_.size();
    }

    public Signature getDeviceMakerSignature() {
        return this.deviceMakerSignature_;
    }

    public void setDeviceMakerSignature(Signature sig) {
        this.deviceMakerSignature_ = sig;
    }

    public Signature getDeviceModelSignature() {
        return this.deviceModelSignature_;
    }

    public void setDeviceModelSignature(Signature sig) {
        this.deviceModelSignature_ = sig;
    }

    public DeviceAttribute getDeviceAttributes() {
        return this.deviceAttributes_;
    }

    public void setDeviceAttributes(DeviceAttribute da) {
        this.deviceAttributes_ = da;
    }

    public UInt32Number getDeviceTechInfo() {
        return this.deviceTechInfo_;
    }

    public void setDeviceTechInfo(UInt32Number code) {
        this.deviceTechInfo_ = code;
    }

    public MultiLocalizedUnicodeType getDeviceMakerDesc() {
        return this.deviceMfgDescTag_;
    }

    public void setDeviceMakerDesc(MultiLocalizedUnicodeType txt) {
        this.deviceMfgDescTag_ = txt;
    }

    public MultiLocalizedUnicodeType getDeviceModelDesc() {
        return this.deviceModelDescTag_;
    }

    public void setDeviceModelDesc(MultiLocalizedUnicodeType txt) {
        this.deviceModelDescTag_ = txt;
    }

    /**
	 * Return XML element of this object.
	 * @param name - attribute name on element
	 * @return XML fragment as a string
	 */
    public String toXmlString(String name) {
        StringBuffer sb = new StringBuffer();
        if (name == null || name.length() < 1) sb.append("<profileDesc sig=\"\">"); else sb.append("<profileDesc name=\"" + name + "\" sig=\"\">");
        sb.append(deviceMakerSignature_.toXmlString());
        sb.append(deviceModelSignature_.toXmlString());
        sb.append(deviceAttributes_.toXmlString());
        sb.append(deviceTechInfo_.toXmlString());
        sb.append(deviceMfgDescTag_.toXmlString());
        sb.append(deviceModelDescTag_.toXmlString());
        sb.append("</profileDesc>");
        return sb.toString();
    }

    public String toXmlString() {
        return toXmlString(null);
    }
}
