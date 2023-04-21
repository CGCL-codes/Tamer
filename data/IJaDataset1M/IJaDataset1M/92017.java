package net.sourceforge.processdash.util.lock;

public interface ConcurrencyLockApprover {

    public void approveLock(ConcurrencyLock lock, String extraInfo) throws LockFailureException;
}
