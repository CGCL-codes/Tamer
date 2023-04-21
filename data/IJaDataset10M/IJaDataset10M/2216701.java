package org.nightlabs.util;

/**
 * Read-Write-Lock interface.
 * @see RWLock
 * @author Marco Schulze
 */
public interface RWLockable {

    public void acquireReadLock() throws DeadLockException;

    public void acquireWriteLock() throws DeadLockException;

    public void releaseLock();
}
