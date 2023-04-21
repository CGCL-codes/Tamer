package java.nio;

final class DoubleViewBufferImpl extends DoubleBuffer {

    /** Position in bb (i.e. a byte offset) where this buffer starts. */
    private int offset;

    private ByteBuffer bb;

    private boolean readOnly;

    private ByteOrder endian;

    DoubleViewBufferImpl(ByteBuffer bb, int capacity) {
        super(capacity, capacity, 0, -1);
        this.bb = bb;
        this.offset = bb.position();
        this.readOnly = bb.isReadOnly();
        this.endian = bb.order();
        if (bb.isDirect()) this.address = VMDirectByteBuffer.adjustAddress(bb.address, offset);
    }

    public DoubleViewBufferImpl(ByteBuffer bb, int offset, int capacity, int limit, int position, int mark, boolean readOnly, ByteOrder endian) {
        super(capacity, limit, position, mark);
        this.bb = bb;
        this.offset = offset;
        this.readOnly = readOnly;
        this.endian = endian;
        if (bb.isDirect()) this.address = VMDirectByteBuffer.adjustAddress(bb.address, offset);
    }

    /**
   * Reads the <code>double</code> at this buffer's current position,
   * and then increments the position.
   *
   * @exception BufferUnderflowException If there are no remaining
   * <code>double</code>s in this buffer.
   */
    public double get() {
        int p = position();
        double result = ByteBufferHelper.getDouble(bb, (p << 3) + offset, endian);
        position(p + 1);
        return result;
    }

    /**
   * Absolute get method. Reads the <code>double</code> at position
   * <code>index</code>.
   *
   * @exception IndexOutOfBoundsException If index is negative or not smaller
   * than the buffer's limit.
   */
    public double get(int index) {
        return ByteBufferHelper.getDouble(bb, (index << 3) + offset, endian);
    }

    public DoubleBuffer put(double value) {
        int p = position();
        ByteBufferHelper.putDouble(bb, (p << 3) + offset, value, endian);
        position(p + 1);
        return this;
    }

    public DoubleBuffer put(int index, double value) {
        ByteBufferHelper.putDouble(bb, (index << 3) + offset, value, endian);
        return this;
    }

    public DoubleBuffer compact() {
        if (position() > 0) {
            int count = limit() - position();
            bb.shiftDown(offset, offset + 8 * position(), 8 * count);
            position(count);
            limit(capacity());
        } else {
            position(limit());
            limit(capacity());
        }
        return this;
    }

    public DoubleBuffer slice() {
        return new DoubleViewBufferImpl(bb, (position() >> 3) + offset, remaining(), remaining(), 0, -1, readOnly, endian);
    }

    DoubleBuffer duplicate(boolean readOnly) {
        int pos = position();
        reset();
        int mark = position();
        position(pos);
        return new DoubleViewBufferImpl(bb, offset, capacity(), limit(), pos, mark, readOnly, endian);
    }

    public DoubleBuffer duplicate() {
        return duplicate(readOnly);
    }

    public DoubleBuffer asReadOnlyBuffer() {
        return duplicate(true);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isDirect() {
        return bb.isDirect();
    }

    public ByteOrder order() {
        return endian;
    }
}
