package com.googlecode.dni.internal.library;

import java.util.logging.Level;
import com.googlecode.dni.DirectNativeInterface;
import com.googlecode.dni.InitialisationOption;
import com.googlecode.dni.internal.DniInternal;
import com.googlecode.dni.type.Pointer;

/**
 * <p>
 *  Encapsulates scratch memory for library calls.
 * </p>
 * <p>
 *  This class acts as a sort of stack, because Java doesn't have access to the
 *  native stack.  (And rightly so.)  It's thread local.
 * </p>
 * <h3>Usage model</h3>
 * <pre>
 *   ScratchMemory scratchMemory = ScratchMemory.reserve( 128 );
 *   long ptr = scratchMemory.getPointer64();
 *   try
 *   {
 *       MEMORY_ACCESS.putInt( ptr, 42 );  // etc.
 *
 *       someNativeMethod( ptr );
 *   }
 *   finally
 *   {
 *       scratchMemory.release( 128 );
 *   }
 * </pre>
 *
 * @author Matthew Wilson
 */
public final class ScratchMemory {

    private static final int DEFAULT_SIZE;

    static {
        int size = (Integer) DirectNativeInterface.getOption(InitialisationOption.SCRATCH_MEMORY_SIZE);
        int actualSize = 16 * Math.max(1, size / 16);
        DEFAULT_SIZE = actualSize * 1024;
    }

    private static final ThreadLocal<ScratchMemory> INSTANCE = new ThreadLocal<ScratchMemory>() {

        @Override
        protected ScratchMemory initialValue() {
            return new ScratchMemory(null, DEFAULT_SIZE);
        }
    };

    /** The previous block in the stack. */
    private final ScratchMemory previous;

    /** Pointer to the base of the buffer. */
    private final long basePointer;

    /** Bytes allocated. */
    private final int allocatedSize;

    /** The position of the currently allocated block. */
    private int currentStartPosition;

    /** The position of the end of the currently allocated block. */
    private int currentEndPosition;

    private boolean freed;

    private ScratchMemory(final ScratchMemory previous, final int allocatedSize) {
        this.previous = previous;
        this.allocatedSize = allocatedSize;
        Pointer pointer = DniInternal.PLATFORM.allocatePages(this.allocatedSize, false, true);
        this.basePointer = pointer.address();
    }

    /**
     * Obtains the scratch memory for this thread, reserving the given number of
     * bytes.
     *
     * @param size
     *            the size in bytes to reserve
     * @return the scratch memory
     */
    public static ScratchMemory reserve(final int size) {
        assert (size & 31) == 0 : size;
        ScratchMemory scratchMemory = INSTANCE.get();
        int currentEndPosition = scratchMemory.currentEndPosition;
        if (scratchMemory.allocatedSize - currentEndPosition < size) {
            scratchMemory = scratchMemory.reallocate(size);
            currentEndPosition = scratchMemory.currentEndPosition;
        }
        scratchMemory.currentStartPosition = currentEndPosition;
        scratchMemory.currentEndPosition = currentEndPosition + size;
        return scratchMemory;
    }

    /**
     * Obtains the pointer to the current scratch memory region.  This method
     * can only be called (and expected to return a valid value) immediately
     * after a call to {@link #reserve(int)}, and certainly not after a call to
     * {@link #release(int)}.
     *
     * @return the pointer (for 64-bit address space)
     */
    public long getPointer64() {
        return this.basePointer + this.currentStartPosition;
    }

    /**
     * Obtains the pointer to the current scratch memory region.  This method
     * can only be called (and expected to return a valid value) immediately
     * after a call to {@link #reserve(int)}, and certainly not after a call to
     * {@link #release(int)}.
     *
     *
     * @return the pointer (for 32-bit address space)
     */
    public int getPointer32() {
        return (int) this.basePointer + this.currentStartPosition;
    }

    /**
     * Release memory that was reserved and used.
     *
     * @param size
     *            the size in bytes reserved
     */
    public void release(final int size) {
        assert !this.freed;
        this.currentEndPosition -= size;
        this.currentStartPosition = this.currentEndPosition;
        assert this.currentStartPosition >= 0 : this;
        if (this.currentStartPosition == 0 && this.previous != null) {
            INSTANCE.set(this.previous);
            free();
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ScratchMemory[0x" + Long.toHexString(this.basePointer) + "+0x" + Long.toHexString(this.allocatedSize) + ";" + "pos=" + this.currentStartPosition + ";end=" + this.currentEndPosition + "]";
    }

    /**
     * Resets this thread's memory.  Used for testing only.
     */
    public static void reset() {
        if (DniInternal.TESTING == null) {
            throw new AssertionError();
        }
        ScratchMemory scratchMemory = INSTANCE.get();
        if (scratchMemory != null) {
            try {
                scratchMemory.free();
            } finally {
                INSTANCE.set(new ScratchMemory(null, DEFAULT_SIZE));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() {
        try {
            free();
        } catch (Throwable throwable) {
            DniInternal.LOGGER.log(Level.SEVERE, "Failed to free scratch memory", throwable);
        }
    }

    private ScratchMemory reallocate(final int size) {
        ScratchMemory replacement;
        int newSize = 16 * 1024 * Math.max(1, size / 16 / 1024);
        if (this.currentEndPosition == 0) {
            replacement = new ScratchMemory(null, newSize);
            INSTANCE.set(replacement);
            free();
        } else {
            replacement = new ScratchMemory(this, newSize);
            INSTANCE.set(replacement);
        }
        return replacement;
    }

    private synchronized void free() {
        if (!this.freed) {
            this.freed = true;
            DniInternal.PLATFORM.unallocatePages(Pointer.fromAddress(this.basePointer), this.allocatedSize);
        }
    }
}
