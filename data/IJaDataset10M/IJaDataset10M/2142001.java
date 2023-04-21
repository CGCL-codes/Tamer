package org.impalaframework.module.spi;

/**
 * Functionality for coarse grained framework locking, which is invoked when
 * module reload operations are invoked.
 * 
 * @author Phil Zoio
 */
public interface FrameworkLockHolder {

    /**
     * Locks framework for writing. Invoked when module operations are invoked
     */
    void writeLock();

    /**
     * Releases write lock. Invoked when module operations are completed
     */
    void writeUnlock();

    /**
     * Obtains read lock, which prevents framework from handling out a write lock until 
     * this lock is release
     */
    void readLock();

    /**
     * Releases read lock. This allows write lock to be obtained
     */
    void readUnlock();

    /**
     * Returns true if framework operations is not write locked by another thread. 
     */
    boolean isAvailable();
}
