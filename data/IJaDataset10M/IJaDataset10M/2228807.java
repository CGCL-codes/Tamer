package tw.edu.shu.im.iccio.datatype;

import tw.edu.shu.im.iccio.Streamable;
import tw.edu.shu.im.iccio.ICCProfileException;

/**
 * Response16Number is ICC Profile type with 8-byte values, used to associate a normalized device code with a measurement value.
 * 
 * @author Ted Wen
 * @version 0.1
 * @update 2006-10-29
 * 
 * <code>
 * Position		Bytes		Type			Content	
 * 0..1			2		UInt16Number		number from DeviceMin to DeviceMax (0..FFFF)
 * 2..3			2					reserved to 0
 * 4..7			4		s15Fixed16Number	measurement value
 * </code>
 */
public class Response16Number implements Streamable {

    public static final int SIZE = 8;

    private UInt16Number deviceCode_;

    private short reserved_;

    private S15Fixed16Number measurement_;

    public Response16Number() {
    }

    public Response16Number(Response16Number copy) {
        this.deviceCode_ = new UInt16Number(copy.deviceCode_);
        this.reserved_ = 0;
        this.measurement_ = new S15Fixed16Number(copy.measurement_);
    }

    public Response16Number(byte[] asByteArray) throws ICCProfileException {
        fromByteArray(asByteArray, 0, SIZE);
    }

    public Response16Number(byte[] asByteArray, int offset) throws ICCProfileException {
        fromByteArray(asByteArray, offset, SIZE);
    }

    /**
	 * Construct Response16Number with a device code and its measurement value.
	 * @param code - device code in the range of 0..FFFFh
	 * @param measurement - the measurement value for this device
	 */
    public Response16Number(int code, double measurement) throws ICCProfileException {
        this.deviceCode_ = new UInt16Number(code);
        this.reserved_ = 0;
        this.measurement_ = new S15Fixed16Number(measurement);
    }

    public int getDeviceCode() {
        return (this.deviceCode_ == null) ? 0 : deviceCode_.intValue();
    }

    public double getMeasurement() {
        return (this.measurement_ == null) ? 0.0 : measurement_.doubleValue();
    }

    public void fromByteArray(byte[] asByteArray, int offset, int len) throws ICCProfileException {
        if (asByteArray == null) throw new ICCProfileException("byte array null", ICCProfileException.NullPointerException);
        if (offset < 0) throw new ICCProfileException("offset < 0", ICCProfileException.IndexOutOfBoundsException);
        if (len != SIZE) throw new ICCProfileException("len parameter is not equal to SIZE", ICCProfileException.WrongSizeException);
        if (asByteArray.length < offset + len) throw new ICCProfileException("offset outside byte array", ICCProfileException.IndexOutOfBoundsException);
        this.deviceCode_ = new UInt16Number(asByteArray, offset);
        this.measurement_ = new S15Fixed16Number(asByteArray, offset + 4);
    }

    public byte[] toByteArray() throws ICCProfileException {
        byte[] ba1 = this.deviceCode_.toByteArray();
        byte[] ba3 = this.measurement_.toByteArray();
        byte[] all = new byte[ba1.length + 2 + ba3.length];
        if (all.length != this.SIZE) throw new ICCProfileException("wrong size", ICCProfileException.WrongSizeException);
        System.arraycopy(ba1, 0, all, 0, ba1.length);
        System.arraycopy(ba3, 0, all, 4, ba3.length);
        return all;
    }

    public int size() {
        return this.SIZE;
    }

    /**
	 * Make a string of this data object in format of "value,0,measurement".
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getDeviceCode());
        sb.append(',');
        sb.append(this.reserved_);
        sb.append(',');
        sb.append(getMeasurement());
        return sb.toString();
    }

    /**
	 * Return XML element of this object.
	 * @param name - attribute name on element
	 * @return XML fragment as a string
	 */
    public String toXmlString(String name) {
        StringBuffer sb = new StringBuffer();
        if (name == null || name.length() < 1) sb.append("<response16Number>"); else sb.append("<response16Number name=\"" + name + "\">");
        sb.append(deviceCode_.toXmlString("DeviceCode"));
        sb.append(measurement_.toXmlString("Measurement"));
        sb.append("</response16Number>");
        return sb.toString();
    }

    public String toXmlString() {
        return toXmlString(null);
    }
}
