package org.activebpel.rt.bpel.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.activebpel.rt.bpel.AeBusinessProcessException;

/**
 * This class implements a simple in-memory process def lock manager.
 */
public class AeInMemoryLockManager extends AeAbstractLockManager {

    /** Contains all currently acquired locks. */
    private Set mLocks;

    /**
    * Constructs a new in-memory lock manager.
    * 
    * @param aConfig The configuration map for this manager.
    */
    public AeInMemoryLockManager(Map aConfig) {
    }

    /**
    * @see org.activebpel.rt.bpel.impl.IAeManager#create()
    */
    public void create() throws Exception {
        super.create();
        mLocks = new HashSet();
    }

    /**
    * @see org.activebpel.rt.bpel.impl.IAeManager#stop()
    */
    public void stop() {
        mLocks.clear();
    }

    /**
    * @see org.activebpel.rt.bpel.impl.AeAbstractLockManager#acquireLockInternal(org.activebpel.rt.bpel.impl.IAeLockManager.IAeLock)
    */
    protected boolean acquireLockInternal(IAeLock aLock) throws AeBusinessProcessException {
        synchronized (mLocks) {
            if (!mLocks.contains(aLock)) {
                mLocks.add(aLock);
                return true;
            }
        }
        return false;
    }

    /**
    * @see org.activebpel.rt.bpel.impl.AeAbstractLockManager#releaseLockInternal(org.activebpel.rt.bpel.impl.IAeLockManager.IAeLock)
    */
    protected void releaseLockInternal(IAeLock aLock) {
        synchronized (mLocks) {
            mLocks.remove(aLock);
        }
    }
}
