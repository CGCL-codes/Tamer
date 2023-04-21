package com.googlecode.dni.internal.string;

import com.googlecode.dni.internal.DniInternal;
import com.googlecode.dni.type.NativeObject;
import com.googlecode.dni.type.NativeStringBuffer;
import com.googlecode.dni.type.Pointer;

/**
 * <p>
 *  Partial implementation of {@link NativeStringBuffer}.
 * </p>
 *
 * @author Matthew Wilson
 */
abstract class AbstractNativeStringBuffer implements NativeStringBuffer {

    private final Pointer pointer;

    private final int capacity;

    private final FastCharset fastCharset;

    /**
     * @param pointer
     *            the pointer to the base of the buffer
     * @param capacity
     *            the capacity in bytes
     * @param fastCharset
     *            the charset
     */
    AbstractNativeStringBuffer(final Pointer pointer, final int capacity, final FastCharset fastCharset) {
        assert capacity >= 4;
        this.pointer = pointer;
        this.capacity = capacity;
        this.fastCharset = fastCharset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String read(final String charset) {
        return read();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pointer pointer() {
        return this.pointer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameObject(final NativeObject other) {
        return other instanceof NativeStringBuffer && other.pointer().equals(this.pointer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void free() {
        DniInternal.ALLOCATOR.free(this.pointer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final NativeStringBuffer clear() {
        DniInternal.MEMORY_ACCESSOR.putInt(this.pointer.address(), 0);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int capacity() {
        return this.capacity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String charset() {
        return this.fastCharset.getName();
    }

    /** {@inheritDoc} */
    @Override
    public final String read() {
        return this.fastCharset.decode(this.pointer.address());
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(final Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        AbstractNativeStringBuffer other = (AbstractNativeStringBuffer) obj;
        return this.pointer.equals(other.pointer) && this.capacity == other.capacity && this.fastCharset.getName().equals(other.fastCharset.getName());
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        return this.pointer.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "NativeStringBuffer[" + this.pointer + "+" + this.capacity + "]";
    }

    /**
     * @return the fastCharset
     */
    final FastCharset getFastCharset() {
        return this.fastCharset;
    }

    /**
     * @param remaining
     *            the number of remaining bytes
     * @param count
     *            the number of bytes being asked to copy
     * @return an exception
     */
    static IllegalArgumentException bufferOverflow(final int remaining, final int count) {
        return new IllegalArgumentException("Buffer remaining of " + remaining + " is too small for " + count + " bytes");
    }

    /**
     * @return an exception
     */
    static IllegalStateException missingTerminator() {
        return new IllegalStateException("Missing terminator; has native code overflowed the buffer?");
    }
}
