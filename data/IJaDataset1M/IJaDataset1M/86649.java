package org.gpc.gen;

/** Abstraction for instruction bytes
 * 
 * CodeBytes provides a logical view of a piece of
 * instruction bytes. CodeBytes instances can share
 * an array of instruction bytes and map different
 * portions of the bytes.
 * 
 * This class is not thread-safe.
 * 
 * @author EaseWay
 *
 */
public class CodeBytes {

    private byte[] bytes;

    private int offset = 0;

    private int length;

    /** Construct CodeBytes owns the bytes
	 * 
	 * @param source instruction bytes.
	 */
    public CodeBytes(byte[] source) {
        bytes = source;
        length = bytes.length;
    }

    /** Construct with fixed-length empty byte array
	 * 
	 * @param len length of bytes.
	 */
    public CodeBytes(int len) {
        bytes = new byte[len];
        length = len;
    }

    /** Construct CodeBytes with mapping
	 * 
	 * @param source instruction bytes.
	 * @param off mapping start offset.
	 * @param len mapping length.
	 */
    public CodeBytes(byte[] source, int off, int len) {
        this(source);
        map(off, len);
    }

    /** Construct from another instance
	 * 
	 * @param source source of instruction bytes
	 */
    public CodeBytes(CodeBytes source, int off, int len) {
        if (off < 0 || off >= source.length) {
            throw new IndexOutOfBoundsException("map offset out-of-bounds");
        } else if (off + len > source.length) {
            throw new IndexOutOfBoundsException("map exceeds end of boundary");
        }
        bytes = source.bytes;
        offset = source.offset + off;
        length = len;
    }

    /** Map portion of all instruction bytes
	 * 
	 * @param off start offset
	 * @param len mapping length
	 * @throws IndexOutOfBoundsException if off or off + len is out-of-bounds
	 */
    public void map(int off, int len) {
        if (off < 0 || off >= bytes.length) {
            throw new IndexOutOfBoundsException("map offset out-of-bounds");
        } else if (off + len > bytes.length) {
            throw new IndexOutOfBoundsException("map excceeds end of boundary");
        }
        offset = off;
        length = len;
    }

    /** Reset mapped portion to the complete byte array
	 * 
	 */
    public void resetMapping() {
        offset = 0;
        length = bytes.length;
    }

    public CodeBytes subMap(int off, int len) {
        return new CodeBytes(this, off, len);
    }

    public int originalLength() {
        return bytes.length;
    }

    public byte[] originalBytes() {
        return bytes;
    }

    public int mappedOffset() {
        return offset;
    }

    public int mappedLength() {
        return length;
    }

    public byte at(int off) {
        return bytes[offset + off];
    }

    public void copy(int off, byte[] dest, int destOff, int len) {
        System.arraycopy(bytes, offset + off, dest, destOff, len);
    }

    public byte[] copy(int off, int len) {
        if (offset + off + len > bytes.length) {
            throw new IndexOutOfBoundsException("copy excceeds end of boundary");
        }
        byte[] dest = new byte[len];
        copy(off, dest, 0, len);
        return dest;
    }

    public void copy(int off, CodeBytes dest, int destOff, int len) {
        copy(off, dest.bytes, dest.offset + destOff, len);
    }

    public void copy(int off, CodeBytes dest) {
        copy(off, dest.bytes, dest.offset, dest.length);
    }

    public void set(int off, byte value) {
        bytes[offset + off] = value;
    }

    public void set(int off, byte[] src, int srcOff, int len) {
        System.arraycopy(src, srcOff, bytes, offset + off, len);
    }

    public void set(int off, byte[] src) {
        set(off, src, 0, src.length);
    }

    public void set(int off, CodeBytes src, int srcOff, int len) {
        set(off, src.bytes, src.offset + srcOff, len);
    }

    public void set(int off, CodeBytes src) {
        set(off, src.bytes, src.offset, src.length);
    }

    public int asHalf(int off) {
        return ((int) at(off) & 0xff) | (((int) at(off + 1) << 8) & 0xff00);
    }

    public int asWord(int off) {
        return asHalf(off) | ((asHalf(off + 2) << 16) & 0xffff0000);
    }

    public long asLong(int off) {
        return ((long) asWord(off) & 0x0ffffffffL) | (((long) asWord(off + 4) << 32) & 0xffffffff00000000L);
    }

    public void setHalf(int off, int value) {
        set(off, (byte) (value & 0xff));
        set(off + 1, (byte) ((value >> 8) & 0xff));
    }

    public void setWord(int off, int value) {
        setHalf(off, value & 0x0ffff);
        setHalf(off + 2, (value >> 16) & 0x0ffff);
    }

    public void setLong(int off, long value) {
        setWord(off, (int) (value & 0x0ffffffffL));
        setWord(off + 4, (int) ((value >> 32) & 0x0ffffffffL));
    }
}
