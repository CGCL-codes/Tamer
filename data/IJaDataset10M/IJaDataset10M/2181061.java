package org.jrobin.core;

import java.io.IOException;

abstract class RrdPrimitive {

    static final int STRING_LENGTH = 20;

    static final int RRD_INT = 0, RRD_LONG = 1, RRD_DOUBLE = 2, RRD_STRING = 3;

    static final int[] RRD_PRIM_SIZES = { 4, 8, 8, 2 * STRING_LENGTH };

    private RrdBackend backend;

    private int byteCount;

    private final long pointer;

    private final boolean cachingAllowed;

    RrdPrimitive(final RrdUpdater updater, final int type, final boolean isConstant) throws IOException {
        this(updater, type, 1, isConstant);
    }

    RrdPrimitive(final RrdUpdater updater, final int type, final int count, final boolean isConstant) throws IOException {
        this.backend = updater.getRrdBackend();
        this.byteCount = RRD_PRIM_SIZES[type] * count;
        this.pointer = updater.getRrdAllocator().allocate(byteCount);
        this.cachingAllowed = isConstant || backend.isCachingAllowed();
    }

    final byte[] readBytes() throws IOException {
        final byte[] b = new byte[byteCount];
        backend.read(pointer, b);
        return b;
    }

    final void writeBytes(final byte[] b) throws IOException {
        assert b.length == byteCount : "Invalid number of bytes supplied to RrdPrimitive.write method";
        backend.write(pointer, b);
    }

    final int readInt() throws IOException {
        return backend.readInt(pointer);
    }

    final void writeInt(final int value) throws IOException {
        backend.writeInt(pointer, value);
    }

    final long readLong() throws IOException {
        return backend.readLong(pointer);
    }

    final void writeLong(final long value) throws IOException {
        backend.writeLong(pointer, value);
    }

    final double readDouble() throws IOException {
        return backend.readDouble(pointer);
    }

    final double readDouble(final int index) throws IOException {
        final long offset = pointer + ((long) index * (long) RRD_PRIM_SIZES[RRD_DOUBLE]);
        return backend.readDouble(offset);
    }

    final double[] readDouble(final int index, final int count) throws IOException {
        final long offset = pointer + ((long) index * (long) RRD_PRIM_SIZES[RRD_DOUBLE]);
        return backend.readDouble(offset, count);
    }

    final void writeDouble(final double value) throws IOException {
        backend.writeDouble(pointer, value);
    }

    final void writeDouble(final int index, final double value, final int count) throws IOException {
        final long offset = pointer + ((long) index * (long) RRD_PRIM_SIZES[RRD_DOUBLE]);
        backend.writeDouble(offset, value, count);
    }

    final void writeDouble(final int index, final double[] values) throws IOException {
        final long offset = pointer + ((long) index * (long) RRD_PRIM_SIZES[RRD_DOUBLE]);
        backend.writeDouble(offset, values);
    }

    final String readString() throws IOException {
        return backend.readString(pointer);
    }

    final void writeString(final String value) throws IOException {
        backend.writeString(pointer, value);
    }

    final boolean isCachingAllowed() {
        return cachingAllowed;
    }
}
