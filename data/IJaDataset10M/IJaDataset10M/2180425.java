package org.metastatic.rsync;

/**
 * This is the {@link Delta} in the rsync algorithm that introduces new
 * data. It is an array of bytes and an offset, such that the updated
 * file should contain this block at the given offset.
 *
 * @version $Revision: 1.8 $
 */
public class DataBlock implements Delta, java.io.Serializable {

    private static final long serialVersionUID = -3132452687703522201L;

    /**
    * The block of data to insert.
    *
    * @since 1.1
    */
    protected final byte[] data;

    /**
    * The offset in the file to start this block.
    *
    * @since 1.1
    */
    protected final long offset;

    /**
    * Create a new instance of a DataBlock with a given offset and
    * block of bytes.
    *
    * @param offset The offset where this data should go.
    * @param data The data itself.
    * @since 1.1
    */
    public DataBlock(long offset, byte[] data) {
        this.offset = offset;
        this.data = (byte[]) data.clone();
    }

    /**
    * Create a new instance of a DataBlock with a given offset and a
    * portion of a byte array.
    *
    * @param offset The write offset of this data block.
    * @param data   The data itself.
    * @param off    The offset in the array to begin copying.
    * @param len    The number of bytes to copy.
    */
    public DataBlock(long offset, byte[] data, int off, int len) {
        this.offset = offset;
        if (data.length == len && off == 0) {
            this.data = (byte[]) data.clone();
        } else {
            this.data = new byte[len];
            System.arraycopy(data, 0, this.data, off, len);
        }
    }

    public long getWriteOffset() {
        return offset;
    }

    public int getBlockLength() {
        return data.length;
    }

    /**
    * Get the offset at which this block should begin.
    *
    * @return The offset at which this block should begin.
    * @since 1.1
    */
    public long getOffset() {
        return offset;
    }

    /**
    * Return the array of bytes that is the data block.
    *
    * @return The block itself.
    * @since 1.1
    */
    public byte[] getData() {
        return data;
    }

    /**
    * Return a printable string that represents this object.
    *
    * @return A string representation of this block.
    * @since 1.1
    */
    public String toString() {
        String str = "[ off=" + offset + " len=" + data.length + " data=";
        int len = Math.min(data.length, 256);
        str += Util.toHexString(data, 0, len);
        if (len != data.length) str += "...";
        return str + " ]";
    }

    /**
    * Return the hash code for this data block.
    *
    * @return The hash code.
    * @since 1.1
    */
    public int hashCode() {
        int b = 0;
        for (int i = 0; i < data.length; i++) b ^= data[i] << ((i * 8) % 32);
        return b + (int) offset;
    }

    /**
    * Test if another object equals this one.
    *
    * @return <tt>true</tt> If <tt>o</tt> is an instance of DataBlock and
    *   if both the offsets and the byte arrays of both are equal.
    * @throws java.lang.ClassCastException If <tt>o</tt> is not an
    *   instance of this class.
    * @throws java.lang.NullPointerException If <tt>o</tt> is null.
    */
    public boolean equals(Object o) {
        return offset == ((DataBlock) o).offset && java.util.Arrays.equals(data, ((DataBlock) o).data);
    }
}
