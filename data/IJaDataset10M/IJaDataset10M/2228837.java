package com.googlecode.dni.internal;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import com.googlecode.dni.Allocator;
import com.googlecode.dni.DirectNativeInterface;
import com.googlecode.dni.InitialisationOption;
import com.googlecode.dni.internal.platform.PlatformUtilities;

/**
 * Provides utilities for dealing with native memory.
 *
 * @author Matthew Wilson
 */
public final class MemoryAccess {

    private static final String PACKAGE = DniTypes.INTERNAL_PACKAGE;

    /**
     * Class name of the unsafe accessor class.
     */
    public static final String UNSAFE_ACCESS_TYPE;

    /**
     * Allocator using above accessor class.
     */
    public static final Allocator ALLOCATOR;

    /**
     * Memory accessor using above accessor class.
     */
    public static final MemoryAccessor MEMORY_ACCESSOR;

    /** Prevents instantiation. */
    private MemoryAccess() {
        throw new UnsupportedOperationException();
    }

    static {
        boolean sunMiscUnsafe = false;
        if ((Boolean) DirectNativeInterface.getOption(InitialisationOption.USE_SUN_MISC_UNSAFE)) {
            sunMiscUnsafe = PlatformUtilities.getSunMiscUnsafe() != null;
        }
        if (sunMiscUnsafe) {
            UNSAFE_ACCESS_TYPE = PACKAGE + "/SunMiscUnsafeAccess";
        } else {
            UNSAFE_ACCESS_TYPE = PACKAGE + "/NativeUnsafeAccess";
        }
        String allocatorName = (String) DirectNativeInterface.getOption(InitialisationOption.ALLOCATOR);
        Allocator allocator;
        MemoryAccessor memoryAccessor;
        try {
            String name = UNSAFE_ACCESS_TYPE.replace('/', '.');
            if (allocatorName != null && allocatorName.isEmpty()) {
                Class<?> type = Class.forName(allocatorName, true, Thread.currentThread().getContextClassLoader());
                allocator = (Allocator) type.newInstance();
            } else {
                allocator = (Allocator) Class.forName(name).getMethod("getAllocator").invoke(null);
            }
            memoryAccessor = (MemoryAccessor) Class.forName(name).getMethod("getMemoryAccessor").invoke(null);
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }
        if ((Boolean) DirectNativeInterface.getOption(InitialisationOption.ALLOCATOR_DEBUG_MODE)) {
            allocator = new DebugAllocator(allocator);
        }
        ALLOCATOR = allocator;
        MEMORY_ACCESSOR = memoryAccessor;
    }

    /**
     * Provides simple (inlinable, where possible) memory reading.
     *
     * @author Matthew Wilson
     */
    public interface MemoryAccessor {

        /**
         * Reads an <code>byte</code> from the given pointer.
         *
         * @param pointer
         *            the pointer
         * @return the value
         */
        byte getByte(long pointer);

        /**
         * Writes a <code>byte</code> to the given pointer.
         *
         * @param pointer
         *            the pointer
         * @param value
         *            the value
         */
        void putByte(long pointer, byte value);

        /**
         * Reads an <code>char</code> from the given pointer.
         *
         * @param pointer
         *            the pointer
         * @return the value
         */
        char getChar(long pointer);

        /**
         * Writes a <code>short</code> to the given pointer.
         *
         * @param pointer
         *            the pointer
         * @param value
         *            the value
         */
        void putChar(long pointer, char value);

        /**
         * Reads an <code>int</code> from the given pointer.
         *
         * @param pointer
         *            the pointer
         * @return the value
         */
        int getInt(long pointer);

        /**
         * Writes a <code>int</code> to the given pointer.
         *
         * @param pointer
         *            the pointer
         * @param value
         *            the value
         */
        void putInt(long pointer, int value);

        /**
         * Reads a <code>long</code> from the given pointer.
         *
         * @param pointer
         *            the pointer
         * @return the value
         */
        long getLong(long pointer);

        /**
         * Writes a <code>long</code> to the given pointer.
         *
         * @param pointer
         *            the pointer
         * @param value
         *            the value
         */
        void putLong(long pointer, long value);

        /**
         * Writes a <code>double</code> to the given pointer.
         *
         * @param pointer
         *            the pointer
         * @param value
         *            the value
         */
        void putDouble(long pointer, double value);

        /**
         * Obtains the pointer to the base of the given buffer's memory.
         *
         * @param buffer
         *            the buffer
         * @return the pointer
         */
        long getBufferPointer(final Buffer buffer);

        /**
         * Creates a buffer (native endian).
         *
         * @param pointer
         *            the pointer
         * @param size
         *            the size in bytes
         * @return a buffer
         */
        ByteBuffer getBuffer(long pointer, int size);

        /**
         * Copies memory.
         *
         * @param from
         *            pointer to the source
         * @param to
         *            pointer to the destination
         * @param size
         *            the size in bytes
         */
        void copyMemory(long from, long to, long size);

        /**
         * Zeroes memory.
         *
         * @param pointer
         *            pointer
         * @param size
         *            the size in bytes
         */
        void zeroMemory(long pointer, long size);
    }
}
