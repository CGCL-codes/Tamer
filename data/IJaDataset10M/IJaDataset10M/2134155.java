package org.opennfc.nfctag;

/**
 * Utility class managing the handle values. Used only internally for the
 * implementation of the framework. package private
 *
 * @hide
 **/
final class SafeHandle extends RuntimeException {

    private static final long serialVersionUID = 5357827968277168621L;

    /** The handle value */
    private int[] mHandle = new int[1];

    /**
     * Creates an new insatnce of safe handle.
     *
     * @param handle the handle value.
     **/
    public SafeHandle(int handle) {
        this.mHandle[0] = handle;
    }

    /**
     * Creates an new insatnce of safe handle.
     **/
    public SafeHandle() {
        this.mHandle[0] = ConstantAutogen.W_NULL_HANDLE;
    }

    /**
     * Returns the handle value.
     *
     * @return the handle value.
     **/
    public int getValue() {
        return this.mHandle[0];
    }

    /**
     * Returns a reference on the handle.
     *
     * @return the handle reference.
     **/
    public int[] getReference() {
        return this.mHandle;
    }

    @Override
    public int hashCode() {
        return this.mHandle[0];
    }

    /**
     * Checks if the handle is null.
     **/
    public boolean isNull() {
        return (this.mHandle[0] == ConstantAutogen.W_NULL_HANDLE);
    }

    /**
     * Releases the handle.
     **/
    public synchronized void release() {
        if (this.mHandle[0] != ConstantAutogen.W_NULL_HANDLE) {
            MethodAutogen.WBasicCloseHandle(this.mHandle[0]);
            this.mHandle[0] = ConstantAutogen.W_NULL_HANDLE;
        }
    }

    /**
     * Finalizer
     **/
    @Override
    protected void finalize() {
        release();
    }
}
