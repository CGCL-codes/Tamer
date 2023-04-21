package com.tinywebgears.tuatara.framework.lock;

public class DefaultResourceLockHandle implements ResourceLockHandleIF {

    private DefaultResourceLock owner;

    private boolean released = false;

    protected DefaultResourceLockHandle(DefaultResourceLock owner) {
        this.owner = owner;
    }

    public synchronized void release() throws IllegalStateException {
        if (released) throw new IllegalStateException("Lock already released");
        owner.release();
        released = true;
    }
}
