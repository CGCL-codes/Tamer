package org.jpos.iso;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author joconnor
 * @version $Revision: 2854 $ $Date: 2010-01-02 05:34:31 -0500 (Sat, 02 Jan 2010) $
 */
public class ISOBinaryFieldPackager extends ISOFieldPackager {

    private BinaryInterpreter interpreter;

    private Prefixer prefixer;

    /**
     * Constructs a default ISOBinaryFieldPackager. There is no length prefix and a
     * literal interpretation. The set methods must be called to make this
     * ISOBinaryFieldPackager useful.
     */
    public ISOBinaryFieldPackager() {
        super();
        this.interpreter = LiteralBinaryInterpreter.INSTANCE;
        this.prefixer = NullPrefixer.INSTANCE;
    }

    /**
     * Creates an ISOBinaryFieldPackager.
     * @param maxLength The maximum length of the field in characters or bytes depending on the datatype.
     * @param description The description of the field. For human readable output.
     * @param interpreter The interpreter used to encode the field.
     * @param prefixer The type of length prefixer used to encode this field.
     */
    public ISOBinaryFieldPackager(int maxLength, String description, BinaryInterpreter interpreter, Prefixer prefixer) {
        super(maxLength, description);
        this.interpreter = interpreter;
        this.prefixer = prefixer;
    }

    /**
     * Creates an ISOBinaryFieldPackager.
     * @param interpreter The interpreter used to encode the field.
     * @param prefixer The type of length prefixer used to encode this field.
     */
    public ISOBinaryFieldPackager(BinaryInterpreter interpreter, Prefixer prefixer) {
        super();
        this.interpreter = interpreter;
        this.prefixer = prefixer;
    }

    /**
     * Sets the Interpreter.
     * @param interpreter The interpreter to use in packing and unpacking.
     */
    public void setInterpreter(BinaryInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    /**
     * Sets the length prefixer.
     * @param prefixer The length prefixer to use during packing and unpacking.
     */
    public void setPrefixer(Prefixer prefixer) {
        this.prefixer = prefixer;
    }

    /**
	 * @see org.jpos.iso.ISOFieldPackager#getMaxPackedLength()
	 */
    public int getMaxPackedLength() {
        return prefixer.getPackedLength() + interpreter.getPackedLength(getLength());
    }

    /**
	 * Convert the component into a byte[].
	 */
    public byte[] pack(ISOComponent c) throws ISOException {
        try {
            byte[] data = (byte[]) c.getValue();
            int packedLength = prefixer.getPackedLength();
            if (packedLength == 0) {
                if (data.length != getLength()) {
                    throw new ISOException("Binary data length not the same as the packager length (" + data.length + "/" + getLength() + ")");
                }
            }
            byte[] ret = new byte[interpreter.getPackedLength(data.length) + packedLength];
            prefixer.encodeLength(data.length, ret);
            interpreter.interpret(data, ret, packedLength);
            return ret;
        } catch (Exception e) {
            throw new ISOException(makeExceptionMessage(c, "packing"), e);
        }
    }

    /**
	 * @see org.jpos.iso.ISOFieldPackager#unpack(org.jpos.iso.ISOComponent,
	 *      byte[], int)
	 */
    public int unpack(ISOComponent c, byte[] b, int offset) throws ISOException {
        try {
            int len = prefixer.decodeLength(b, offset);
            if (len == -1) {
                len = getLength();
            }
            int lenLen = prefixer.getPackedLength();
            byte[] unpacked = interpreter.uninterpret(b, offset + lenLen, len);
            c.setValue(unpacked);
            return lenLen + interpreter.getPackedLength(len);
        } catch (Exception e) {
            throw new ISOException(makeExceptionMessage(c, "unpacking"), e);
        }
    }

    /** Unpack from an input stream */
    public void unpack(ISOComponent c, InputStream in) throws IOException, ISOException {
        try {
            int lenLen = prefixer.getPackedLength();
            int len;
            if (lenLen == 0) {
                len = getLength();
            } else {
                len = prefixer.decodeLength(readBytes(in, lenLen), 0);
            }
            int packedLen = interpreter.getPackedLength(len);
            byte[] unpacked = interpreter.uninterpret(readBytes(in, packedLen), 0, len);
            c.setValue(unpacked);
        } catch (ISOException e) {
            throw new ISOException(makeExceptionMessage(c, "unpacking"), e);
        }
    }

    /**
     * component factory
     * @param fieldNumber - the field number
     * @return the newly created component
     */
    public ISOComponent createComponent(int fieldNumber) {
        return new ISOBinaryField(fieldNumber);
    }

    /** Create a nice readable message for errors */
    private String makeExceptionMessage(ISOComponent c, String operation) {
        Object fieldKey = "unknown";
        if (c != null) {
            try {
                fieldKey = c.getKey();
            } catch (Exception ignore) {
            }
        }
        return getClass().getName() + ": Problem " + operation + " field " + fieldKey;
    }

    /**
     * Checks the length of the data against the maximum, and throws an IllegalArgumentException.
     * This is designed to be called from field Packager constructors and the setLength()
     * method.
     * @param len The length of the data for this field packager.
     * @param maxLength The maximum length allowed for this type of field packager.
     *          This depends on the prefixer that is used.
     * @throws IllegalArgumentException If len > maxLength.
     */
    protected void checkLength(int len, int maxLength) throws IllegalArgumentException {
        if (len > maxLength) {
            throw new IllegalArgumentException("Length " + len + " too long for " + getClass().getName());
        }
    }
}
