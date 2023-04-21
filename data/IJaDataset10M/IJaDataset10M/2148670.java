package casa.component.domain.sensor.telosB.netStub;

public class Message implements Cloneable {

    /**
     * The maximum number of characters read from an 8-bit array field
     * being converted into a Java String.
     */
    public static final int MAX_CONVERTED_STRING_LENGTH = 512;

    /** 
     * The underlying byte array storing the data for this message. 
     * This is private to enforce access to the data through the accessor
     * methods in this class, which do bounds checking and manage the
     * base_offset for embedded messages.
     */
    private byte[] data;

    /** 
     * The base offset into the data. This allows the message data to
     * exist at some non-zero offset into the actual data.
     */
    protected int base_offset;

    /**
     * The actual length of the message data. Must be less than or
     * equal to (data.length - base_offset).
     */
    protected int data_length;

    /**
     * The AM type corresponding to this object. Set to -1 if no AM type
     * is known.
     */
    protected int am_type;

    /** Private to prevent no-arg instantiation. */
    private Message() {
    }

    /**
     * Construct a new message of the given size.
     * @param data_length The size of the message to create.
     */
    protected Message(int data_length) {
        this(new byte[data_length]);
    }

    /**
     * Construct a new message of the given size and base offset.
     * Allocates a new byte array of size data_length+base_offset.
     * @param data_length The size of the message to create.
     * @param base_offset The base offset into the newly created message.
     */
    protected Message(int data_length, int base_offset) {
        this(new byte[data_length + base_offset], base_offset);
    }

    /**
     * Construct a message using data as the storage.
     * The length of data determines the length of this message.
     * @param data the storage for this message
     */
    protected Message(byte[] data) {
        this.data = data;
        this.base_offset = 0;
        this.data_length = data.length;
    }

    /**
     * Construct a message using data as the storage.
     * Use the given base_offset as the base offset into the 
     * data array. The data length will be (data.length - base_offset).
     * @param data the storage for this message
     * @param base_offset the base offset into the data array
     */
    protected Message(byte[] data, int base_offset) {
        this.data = data;
        this.base_offset = base_offset;
        this.data_length = data.length - base_offset;
    }

    /**
     * Construct a message using data as the storage.
     * Use the given base_offset as the base offset into the 
     * data array, and the specified data length.
     * @param data the storage for this message
     * @param base_offset the base offset into the data array
     * @param data_length the length of the message data
     */
    protected Message(byte[] data, int base_offset, int data_length) {
        this.data = data;
        this.base_offset = base_offset;
        this.data_length = data_length;
        if (base_offset + data_length > data.length) throw new ArrayIndexOutOfBoundsException("Cannot create Message with base_offset " + base_offset + ", data_length " + data_length + " and data array size " + data.length);
    }

    /**
     * Construct an embedded message within the given 'msg'.
     * Use the given base_offset as the base offset into the 
     * data array, and the specified data length.
     * @param msg the message to embed this message into
     * @param base_offset the base offset into the data array
     * @param data_length the length of the message data
     */
    protected Message(Message msg, int base_offset, int data_length) {
        this(msg.dataGet(), msg.base_offset + base_offset, data_length);
    }

    private Message cloneself() {
        Message copy;
        try {
            copy = (Message) super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Message: WARNING: CloneNotSupportedException in cloneself(): " + e);
            System.err.println("Message: This is a bug - please contact dgay@intel-research.net");
            copy = null;
            System.exit(2);
        }
        return copy;
    }

    /**
     * Clone this Message, including making a copy of its data
     */
    public Object clone() {
        Message copy = cloneself();
        copy.data = (byte[]) data.clone();
        copy.base_offset = this.base_offset;
        copy.data_length = this.data_length;
        copy.am_type = this.am_type;
        return copy;
    }

    /**
     * Clone this Message, but give it a new unitialised data array of size
     * size
     * @param size size of the new data array
     */
    public Message clone(int size) {
        Message copy = cloneself();
        copy.data = new byte[size];
        copy.base_offset = this.base_offset;
        copy.data_length = size;
        copy.am_type = this.am_type;
        return copy;
    }

    /**
     * Copy new data for this message from 'data'. 
     * Copies min(data.length, this.data_length) bytes.
     * @param data the array containing the data to be copied
     * @exception ArrayIndexOutOfBoundsException if any of
     * data[0..getData().length - 1] are invalid
     */
    public void dataSet(byte[] data) {
        dataSet(data, 0, this.base_offset, Math.min(this.data_length, data.length));
    }

    /**
     * Copy new data for this message from offsetFrom in data to
     * offsetTo in this message. Copies a total of length bytes
     * @param data the array containing the data to be copied
     * @param offsetFrom the offset in data to start copying from
     * @param offsetTo the offset at which to start copying data into
     * this message.
     * @param length bytes are copied.
     * @exception ArrayIndexOutOfBoundsException if any of
     * the source or target indices are invalid
     */
    public void dataSet(byte[] data, int offsetFrom, int offsetTo, int length) {
        System.arraycopy(data, offsetFrom, this.data, offsetTo + base_offset, length);
    }

    /**
     * Copy new data for this message from the raw data in msg to
     * offsetTo in this message. Copies a total of msg.dataLength() bytes
     * @param msg the message containing the data to be copied
     * @param offsetTo the offset at which to start copying data into
     * this message.
     * @exception ArrayIndexOutOfBoundsException if any of
     * the target indices are invalid
     */
    public void dataSet(Message msg, int offsetTo) {
        System.arraycopy(msg.dataGet(), msg.baseOffset(), this.data, offsetTo + base_offset, msg.dataLength());
    }

    /**
     * Return the raw byte array representing the data of this message.
     * Note that only indices in the range
     * (this.baseOffset(), this.baseOffset()+this.dataLength()) are
     * valid. 
     */
    public byte[] dataGet() {
        return data;
    }

    /**
     * Return the base offset into the data array for this message.
     */
    public int baseOffset() {
        return base_offset;
    }

    /**
     * Return the length of the data (in bytes) contained in this message.
     */
    public int dataLength() {
        return data_length;
    }

    /**
     * Return the active message type of this message (-1 if unknown)
     */
    public int amType() {
        return am_type;
    }

    /**
     * Set the active message type of this message
     */
    public void amTypeSet(int type) {
        this.am_type = type;
    }

    private void checkBounds(int offset, int length) {
        if (offset < 0 || length <= 0 || offset + length > (data_length * 8)) throw new ArrayIndexOutOfBoundsException("Message.checkBounds: bad offset (" + offset + ") or length (" + length + "), for data_length " + data_length);
    }

    private void checkValue(int length, long value) {
        if (length != 64 && (value < 0 || value >= 1L << length)) throw new IllegalArgumentException("Message.checkValue: bad length (" + length + " or value (" + value + ")");
    }

    private int ubyte(int offset) {
        int val = data[base_offset + offset];
        if (val < 0) return val + 256; else return val;
    }

    /**
     * Read the length bit unsigned int at offset
     * @param offset bit offset where the unsigned int starts
     * @param length bit length of the unsigned int
     * @exception ArrayIndexOutOfBoundsException for invalid offset, length
     */
    protected long getUIntElement(int offset, int length) {
        checkBounds(offset, length);
        int byteOffset = offset >> 3;
        int bitOffset = offset & 7;
        int shift = 0;
        long val = 0;
        if (length + bitOffset <= 8) return (ubyte(byteOffset) >> bitOffset) & ((1 << length) - 1);
        if (bitOffset > 0) {
            val = ubyte(byteOffset) >> bitOffset;
            byteOffset++;
            shift += 8 - bitOffset;
            length -= 8 - bitOffset;
        }
        while (length >= 8) {
            val |= (long) ubyte(byteOffset++) << shift;
            shift += 8;
            length -= 8;
        }
        if (length > 0) val |= (long) (ubyte(byteOffset) & ((1 << length) - 1)) << shift;
        return val;
    }

    /**
     * Set the length bit unsigned int at offset to val
     * @param offset bit offset where the unsigned int starts
     * @param length bit length of the unsigned int
     * @param val value to set the bit field to
     * @exception ArrayIndexOutOfBoundsException for invalid offset, length
     * @exception IllegalArgumentException if val is an out-of-range value
     * for this bitfield
     */
    protected void setUIntElement(int offset, int length, long val) {
        checkBounds(offset, length);
        int byteOffset = offset >> 3;
        int bitOffset = offset & 7;
        int shift = 0;
        if (length + bitOffset <= 8) {
            data[base_offset + byteOffset] = (byte) ((ubyte(byteOffset) & ~(((1 << length) - 1) << bitOffset)) | val << bitOffset);
            return;
        }
        if (bitOffset > 0) {
            data[base_offset + byteOffset] = (byte) ((ubyte(byteOffset) & ((1 << bitOffset) - 1)) | val << bitOffset);
            byteOffset++;
            shift += 8 - bitOffset;
            length -= 8 - bitOffset;
        }
        while (length >= 8) {
            data[base_offset + (byteOffset++)] = (byte) (val >> shift);
            shift += 8;
            length -= 8;
        }
        if (length > 0) data[base_offset + byteOffset] = (byte) ((ubyte(byteOffset) & ~((1 << length) - 1)) | val >> shift);
    }

    /**
     * Read the length bit signed int at offset
     * @param offset bit offset where the signed int starts
     * @param length bit length of the signed int
     * @exception ArrayIndexOutOfBoundsException for invalid offset, length
     */
    protected long getSIntElement(int offset, int length) throws ArrayIndexOutOfBoundsException {
        long val = getUIntElement(offset, length);
        if (length == 64) return val;
        if ((val & 1L << (length - 1)) != 0) return val - (1L << length);
        return val;
    }

    /**
     * Set the length bit signed int at offset to val
     * @param offset bit offset where the signed int starts
     * @param length bit length of the signed int
     * @param val value to set the bit field to
     * @exception ArrayIndexOutOfBoundsException for invalid offset, length
     * @exception IllegalArgumentException if val is an out-of-range value
     * for this bitfield
     */
    protected void setSIntElement(int offset, int length, long value) throws ArrayIndexOutOfBoundsException {
        if (length != 64 && value >= 1L << (length - 1)) throw new IllegalArgumentException();
        if (length != 64 && value < 0) value += 1L << length;
        setUIntElement(offset, length, value);
    }

    /**
     * Read the 32 bit IEEE float at offset
     * @param offset bit offset where the float starts
     * @param length is ignored
     * @exception ArrayIndexOutOfBoundsException for invalid offset
     */
    protected float getFloatElement(int offset, int length) throws ArrayIndexOutOfBoundsException {
        return Float.intBitsToFloat((int) getUIntElement(offset, 32));
    }

    /**
     * Set the 32 bit IEEE float at offset to value
     * @param offset bit offset where the float starts
     * @param length is ignored
     * @parem value value to store in bitfield
     * @exception ArrayIndexOutOfBoundsException for invalid offset
     */
    protected void setFloatElement(int offset, int length, float value) throws ArrayIndexOutOfBoundsException {
        setSIntElement(offset, 32, Float.floatToRawIntBits(value));
    }
}
