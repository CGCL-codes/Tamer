package org.ozoneDB.xml.util;

import java.io.*;

/**
 */
public final class ChunkOutputStream extends OutputStream implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  the output stream has not yet exceeded its initial size.
     */
    public static final byte STATE_NORMAL = 0;

    /**
     *  the output stream has exceeded its initial size.
     */
    public static final byte STATE_OVERFLOW = 1;

    /**
     *  true if this is the last chunk in a sequence of chunks, false otherwise.
     */
    public boolean endFlag = false;

    /**
     * The buffer where data is stored.
     */
    protected byte[] buf;

    /**
     * The number of valid bytes in the buffer.
     */
    protected int count;

    /**
     *  the state of this output stream.
     */
    private byte state;

    /**
     *  The initial size of this output stream
     */
    private int initialSize;

    /**
     *  The amount of bytes by which the buffer will be increased if it overflows.
     */
    private int increase;

    /**
     *  Creates a new byte array output stream. The buffer capacity is initially
     *  1000 bytes. Its size increases by 100 bytes if necessary.
     */
    public ChunkOutputStream() {
        this(1000, 100);
    }

    /**
     *  Creates a new byte array output stream, with a buffer capacity of
     *  the specified size in bytes. Its size increases by 100 bytes if necessary.
     *
     *  @param   size   the initial size. exceeding this size causes the
     *      stream to enter state {@link #STATE_OVERFLOW}.
     *  @exception  IllegalArgumentException if size is negative.
     */
    public ChunkOutputStream(int size) {
        this(size, 100);
    }

    /**
     *  Creates a new byte array output stream, with a buffer capacity of
     *  the specified size in bytes. Its size increases by the specified increase
     *  in bytes if necessary.
     *
     *  @param size the initial size.  exceeding this size causes the
     *      stream to enter state {@link #STATE_OVERFLOW}.
     *  @param increase the amount of bytes by which the stream will be incre
     *  @exception  IllegalArgumentException if <code>size</code> is negative or
     *          <code>increase</code> is less or equal 0.
     */
    public ChunkOutputStream(int size, int increase) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative initial size: " + size);
        }
        if (increase <= 0) {
            throw new IllegalArgumentException("Increase less or equal 0: " + size);
        }
        this.buf = new byte[size + increase];
        this.increase = increase;
        this.initialSize = size;
        this.state = ChunkOutputStream.STATE_NORMAL;
        this.count = 0;
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        out.writeBoolean(endFlag);
        out.writeInt(this.increase);
        out.writeInt(this.initialSize);
        out.writeByte(this.state);
        out.writeInt(this.count);
        out.write(this.buf, 0, this.count);
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.endFlag = in.readBoolean();
        this.increase = in.readInt();
        this.initialSize = in.readInt();
        this.state = in.readByte();
        this.count = in.readInt();
        this.buf = new byte[this.initialSize + this.increase];
        in.read(this.buf, 0, this.count);
    }

    /**
     *  Writes the specified byte to this byte array output stream.
     *
     *  @param b the byte to be written.
     */
    public final void write(int b) {
        int newcount = this.count + 1;
        if (newcount > this.buf.length) {
            byte[] newbuf = new byte[this.count + this.increase];
            System.arraycopy(this.buf, 0, newbuf, 0, this.count);
            this.buf = newbuf;
            this.state = ChunkOutputStream.STATE_OVERFLOW;
        }
        this.buf[this.count] = (byte) b;
        this.count = newcount;
    }

    /**
     *  Writes <code>len</code> bytes from the specified byte array
     *  starting at offset <code>off</code> to this byte array output stream.
     *
     *  @param b the data.
     *  @param off the start offset in the data.
     *  @param len the number of bytes to write.
     */
    public final void write(byte[] b, int off, int len) {
        if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        } else {
            if (len == 0) {
                return;
            }
        }
        int newCount = this.count + len;
        if (newCount > this.buf.length) {
            int newLength = this.buf.length + increase;
            byte[] newbuf = new byte[(newLength >= newCount) ? newLength : newCount];
            System.arraycopy(this.buf, 0, newbuf, 0, this.count);
            this.buf = newbuf;
            this.state = ChunkOutputStream.STATE_OVERFLOW;
        }
        System.arraycopy(b, off, this.buf, this.count, len);
        this.count = newCount;
    }

    /**
     * Writes the complete contents of this byte array output stream to
     * the specified output stream argument, as if by calling the output
     * stream's write method using <code>out.write(buf, 0, count)</code>.
     *
     * @param      out   the output stream to which to write the data.
     * @exception  IOException  if an I/O error occurs.
     */
    public final void writeTo(OutputStream out) throws IOException {
        out.write(this.buf, 0, this.count);
    }

    /**
     * Resets the <code>count</code> field of this byte array output
     * stream to zero, so that all currently accumulated output in the
     * ouput stream is discarded. The output stream can be used again,
     * reusing the already allocated buffer space.
     *
     * @see     java.io.ByteArrayInputStream#count
     */
    public final void reset() {
        this.buf = new byte[initialSize + increase];
        this.state = ChunkOutputStream.STATE_NORMAL;
        this.count = 0;
    }

    /**
     * The number of valid bytes in this stream.
     */
    public final int size() {
        return this.count;
    }

    /**
     * Creates a newly allocated byte array. Its size is the current
     * size of this output stream and the valid contents of the buffer
     * have been copied into it.
     *
     * @return  the current contents of this output stream, as a byte array.
     * @see     java.io.ByteArrayOutputStream#size()
     */
    public final byte[] toByteArray() {
        byte[] newbuf = new byte[this.count];
        System.arraycopy(this.buf, 0, newbuf, 0, this.count);
        return newbuf;
    }

    /**
     *  @return the state of this output stream
     */
    public final byte getState() {
        return this.state;
    }

    /**
     *  marks this chunk as the last in a sequence of chunks
     */
    public final void setEndFlag() {
        this.endFlag = true;
    }

    /**
     *  @return true, if this chunk is the last in a sequence of chunks
     */
    public final boolean getEndFlag() {
        return this.endFlag;
    }
}
