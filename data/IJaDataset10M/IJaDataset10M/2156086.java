package oracle.toplink.essentials.internal.helper;

import java.io.*;
import java.util.*;
import oracle.toplink.essentials.exceptions.*;
import oracle.toplink.essentials.internal.localization.*;
import oracle.toplink.essentials.internal.identitymaps.CacheKey;
import oracle.toplink.essentials.logging.*;

/**
 * INTERNAL:
 * <p>
 * <b>Purpose</b>: To maintain concurrency for a paticular task.
 * It is a wrappers of a semaphore that allows recursive waits by a single thread.
 * <p>
 * <b>Responsibilities</b>:
 * <ul>
 * <li> Keep track of the active thread.
 * <li> Wait all other threads until the first thread is done.
 * <li> Maintain the depth of the active thread.
 * </ul>
 */
public class ConcurrencyManager implements Serializable {

    protected int numberOfReaders;

    protected int depth;

    protected int numberOfWritersWaiting;

    protected transient Thread activeThread;

    public static Hashtable deferredLockManagers;

    protected boolean lockedByMergeManager;

    /** Cachkey owner set when ConcurrencyMananger is used within an cachekey on an idenity map
     * Used to store the owner so that the object involved can be retrieved from the cachekey
     */
    protected CacheKey ownerCacheKey;

    /**
     * Initialize the newly allocated instance of this class.
     * Set the depth to zero.
     */
    public ConcurrencyManager() {
        this.depth = 0;
        this.numberOfReaders = 0;
        this.numberOfWritersWaiting = 0;
    }

    /**
     * Initialize a new ConcurrencyManger, seting depth to zero and setting the
     * owner cacheKey.
     */
    public ConcurrencyManager(CacheKey cacheKey) {
        this();
        this.ownerCacheKey = cacheKey;
    }

    /**
     * Wait for all threads except the active thread.
     * If the active thread just increament the depth.
     * This should be called before entering a critical section.
     */
    public synchronized void acquire() throws ConcurrencyException {
        this.acquire(false);
    }

    /**
     * Wait for all threads except the active thread.
     * If the active thread just increament the depth.
     * This should be called before entering a critical section.
     * called with true from the merge process, if true then the refresh will not refresh the object
     */
    public synchronized void acquire(boolean forMerge) throws ConcurrencyException {
        while (!((getActiveThread() == Thread.currentThread()) || ((getActiveThread() == null) && (getNumberOfReaders() == 0)))) {
            try {
                setNumberOfWritersWaiting(getNumberOfWritersWaiting() + 1);
                wait();
                setNumberOfWritersWaiting(getNumberOfWritersWaiting() - 1);
            } catch (InterruptedException exception) {
                throw ConcurrencyException.waitWasInterrupted(exception.getMessage());
            }
        }
        if (getActiveThread() == null) {
            setActiveThread(Thread.currentThread());
        }
        setIsLockedByMergeManager(forMerge);
        setDepth(getDepth() + 1);
    }

    /**
     * If the lock is not acquired allready acquire it and return true.
     * If it has been acquired allready return false
     * Added for CR 2317
     */
    public synchronized boolean acquireNoWait() throws ConcurrencyException {
        if (!isAcquired() || getActiveThread() == Thread.currentThread()) {
            acquire(false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * If the lock is not acquired allready acquire it and return true.
     * If it has been acquired allready return false
     * Added for CR 2317
     * called with true from the merge process, if true then the refresh will not refresh the object
     */
    public synchronized boolean acquireNoWait(boolean forMerge) throws ConcurrencyException {
        if (!isAcquired() || getActiveThread() == Thread.currentThread()) {
            acquire(forMerge);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add deferred lock into a hashtable to avoid deadlock
     */
    public void acquireDeferredLock() throws ConcurrencyException {
        Thread currentThread = Thread.currentThread();
        DeferredLockManager lockManager = getDeferredLockManager(currentThread);
        if (lockManager == null) {
            lockManager = new DeferredLockManager();
            putDeferredLock(currentThread, lockManager);
        }
        lockManager.incrementDepth();
        synchronized (this) {
            while (!(getNumberOfReaders() == 0)) {
                try {
                    setNumberOfWritersWaiting(getNumberOfWritersWaiting() + 1);
                    wait();
                    setNumberOfWritersWaiting(getNumberOfWritersWaiting() - 1);
                } catch (InterruptedException exception) {
                    throw ConcurrencyException.waitWasInterrupted(exception.getMessage());
                }
            }
            if ((getActiveThread() == currentThread) || (!isAcquired())) {
                lockManager.addActiveLock(this);
                acquire();
            } else {
                lockManager.addDeferredLock(this);
                Object[] params = new Object[2];
                params[0] = this.getOwnerCacheKey().getObject();
                params[1] = currentThread.getName();
                AbstractSessionLog.getLog().log(SessionLog.FINER, "acquiring_deferred_lock", params, true);
            }
        }
    }

    /**
     * Check the lock state, if locked, acquire and release a read lock.
     * This optimizes out the normal read-lock check if not locked.
     */
    public void checkReadLock() throws ConcurrencyException {
        if (getActiveThread() == null) {
            return;
        }
        acquireReadLock();
        releaseReadLock();
    }

    /**
     * Wait on any writer.
     * Allow concurrent reads.
     */
    public synchronized void acquireReadLock() throws ConcurrencyException {
        while (!((getActiveThread() == Thread.currentThread()) || (getActiveThread() == null))) {
            try {
                wait();
            } catch (InterruptedException exception) {
                throw ConcurrencyException.waitWasInterrupted(exception.getMessage());
            }
        }
        setNumberOfReaders(getNumberOfReaders() + 1);
    }

    /**
     * If this is acquired return false otherwise acquire readlock and return true
     */
    public synchronized boolean acquireReadLockNoWait() {
        if (!isAcquired()) {
            acquireReadLock();
            return true;
        } else {
            return false;
        }
    }

    /**
         * Return the active thread.
         */
    public Thread getActiveThread() {
        return activeThread;
    }

    /**
     * Return the deferred lock manager from the thread
     */
    public static synchronized DeferredLockManager getDeferredLockManager(Thread thread) {
        return (DeferredLockManager) getDeferredLockManagers().get(thread);
    }

    /**
     * Return the deferred lock manager hashtable (thread - DeferredLockManager).
     */
    protected static Hashtable getDeferredLockManagers() {
        if (deferredLockManagers == null) {
            deferredLockManagers = new Hashtable(50);
        }
        return deferredLockManagers;
    }

    /**
     * Return the current depth of the active thread.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Number of writer that want the lock.
     * This is used to ensure that a writer is not starved.
     */
    public int getNumberOfReaders() {
        return numberOfReaders;
    }

    /**
     * Number of writers that want the lock.
     * This is used to ensure that a writer is not starved.
     */
    public int getNumberOfWritersWaiting() {
        return numberOfWritersWaiting;
    }

    /**
     * Returns the owner cache key for this concurrency manager
     */
    public CacheKey getOwnerCacheKey() {
        return this.ownerCacheKey;
    }

    /**
     * Return if a thread has aquire this manager.
     */
    public boolean isAcquired() {
        return depth > 0;
    }

    /**
     * INTERNAL:
     * Used byt the refresh process to determine if this concurrency manager is locked by
     * the merge process.  If it is then the refresh should not refresh the object
     */
    public boolean isLockedByMergeManager() {
        return this.lockedByMergeManager;
    }

    /**
     * Check if the deferred locks of a thread are all released
     */
    public static synchronized boolean isBuildObjectOnThreadComplete(Thread thread, IdentityHashtable recursiveSet) {
        if (recursiveSet.containsKey(thread)) {
            return true;
        }
        recursiveSet.put(thread, thread);
        DeferredLockManager lockManager = getDeferredLockManager(thread);
        if (lockManager == null) {
            return true;
        }
        Vector deferredLocks = lockManager.getDeferredLocks();
        for (Enumeration deferredLocksEnum = deferredLocks.elements(); deferredLocksEnum.hasMoreElements(); ) {
            ConcurrencyManager deferedLock = (ConcurrencyManager) deferredLocksEnum.nextElement();
            Thread activeThread = null;
            if (deferedLock.isAcquired()) {
                activeThread = deferedLock.getActiveThread();
                if (activeThread != null) {
                    DeferredLockManager currentLockManager = getDeferredLockManager(activeThread);
                    if (currentLockManager == null) {
                        return false;
                    } else if (currentLockManager.isThreadComplete()) {
                        activeThread = deferedLock.getActiveThread();
                        if (activeThread != null) {
                            if (!isBuildObjectOnThreadComplete(activeThread, recursiveSet)) {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Return if this manager is within a nested aquire.
     */
    public boolean isNested() {
        return depth > 1;
    }

    public synchronized void putDeferredLock(Thread thread, DeferredLockManager lockManager) {
        getDeferredLockManagers().put(thread, lockManager);
    }

    /**
     * Decrement the depth for the active thread.
     * Assume the current thread is the active one.
     * Raise an error if the depth become < 0.
     * The notify will release the first thread waiting on the object,
     * if no threads are waiting it will do nothing.
     */
    public synchronized void release() throws ConcurrencyException {
        if (getDepth() == 0) {
            throw ConcurrencyException.signalAttemptedBeforeWait();
        } else {
            setDepth(getDepth() - 1);
        }
        if (getDepth() == 0) {
            setActiveThread(null);
            setIsLockedByMergeManager(false);
            notifyAll();
        }
    }

    /**
     * Release the deferred lock.
     * This uses a deadlock detection and resoultion algorthm to avoid cache deadlocks.
     * The deferred lock manager keeps track of the lock for a thread, so that other
     * thread know when a deadlock has occured and can resolve it.
     */
    public void releaseDeferredLock() throws ConcurrencyException {
        Thread currentThread = Thread.currentThread();
        DeferredLockManager lockManager = getDeferredLockManager(currentThread);
        if (lockManager == null) {
            return;
        }
        int depth = lockManager.getThreadDepth();
        if (depth > 1) {
            lockManager.decrementDepth();
            return;
        }
        if (!lockManager.hasDeferredLock()) {
            lockManager.releaseActiveLocksOnThread();
            removeDeferredLockManager(currentThread);
            return;
        }
        lockManager.setIsThreadComplete(true);
        while (true) {
            IdentityHashtable recursiveSet = new IdentityHashtable();
            if (isBuildObjectOnThreadComplete(currentThread, recursiveSet)) {
                lockManager.releaseActiveLocksOnThread();
                removeDeferredLockManager(currentThread);
                Object[] params = new Object[1];
                params[0] = currentThread.getName();
                AbstractSessionLog.getLog().log(SessionLog.FINER, "deferred_locks_released", params, true);
                return;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignoreAndContinue) {
                }
            }
        }
    }

    /**
     * Decrement the number of readers.
     * Used to allow concurrent reads.
     */
    public synchronized void releaseReadLock() throws ConcurrencyException {
        if (getNumberOfReaders() == 0) {
            throw ConcurrencyException.signalAttemptedBeforeWait();
        } else {
            setNumberOfReaders(getNumberOfReaders() - 1);
        }
        if (getNumberOfReaders() == 0) {
            notifyAll();
        }
    }

    /**
     * Remove the deferred lock manager for the thread
     */
    public static synchronized DeferredLockManager removeDeferredLockManager(Thread thread) {
        return (DeferredLockManager) getDeferredLockManagers().remove(thread);
    }

    /**
     * Set the active thread.
     */
    public void setActiveThread(Thread activeThread) {
        this.activeThread = activeThread;
    }

    /**
     * Set the current depth of the active thread.
     */
    protected void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * INTERNAL:
     * Used by the mergemanager to let the read know not to refresh this object as it is being
     * loaded by the merge process.
     */
    public void setIsLockedByMergeManager(boolean state) {
        this.lockedByMergeManager = state;
    }

    /**
     * Track the number of readers.
     */
    protected void setNumberOfReaders(int numberOfReaders) {
        this.numberOfReaders = numberOfReaders;
    }

    /**
     * Number of writers that want the lock.
     * This is used to ensure that a writer is not starved.
     */
    protected void setNumberOfWritersWaiting(int numberOfWritersWaiting) {
        this.numberOfWritersWaiting = numberOfWritersWaiting;
    }

    /**
     * Print the nested depth.
     */
    public String toString() {
        Object[] args = { new Integer(getDepth()) };
        return Helper.getShortClassName(getClass()) + ToStringLocalization.buildMessage("nest_level", args);
    }
}
