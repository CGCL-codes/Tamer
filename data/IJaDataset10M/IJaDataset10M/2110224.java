package net.wotonomy.foundation;

import EDU.oswego.cs.dl.util.concurrent.ReentrantLock;

/**
* A lock class that allows a thread to re-acquire it's lock 
* recursively.  Currently an API-compliance wrapper around Doug Lea's 
* ReentrantLock, conforming to the API and behavior of 
* com.webobjects.foundation.NSRecursiveLock.
*
* @author cgruber@israfil.net
* @author $Author: cgruber $
* @version $Revision: 893 $
*/
public class NSRecursiveLock extends ReentrantLock implements NSLocking {

    public NSRecursiveLock() {
    }

    /** Acquire the lock, catching the thrown exception to mirror the
     *  behavior of com.webobjects.foundation.NSRecursiveLock.  Note that
     *  ReentrantLock.acquire() performs a notify() when it's interrupted.
     *  
     *  @see edu.oswego.cs.dl.util.concurrent.ReentrantLock#acquire()
     */
    public void lock() {
        try {
            acquire();
        } catch (InterruptedException interruptedexception) {
        }
    }

    /** Pass the buck to tryLock(long), passing zero time as the parameter.
     * 
     *  @see #tryLock(long)
     */
    public boolean tryLock() {
        return tryLock(1);
    }

    /** Attempt to acquire the lock, catching the thrown exception to mirror 
     *  the behavior of com.webobjects.foundation.NSRecursiveLock.  Note that
     *  ReentrantLock.attempt(*) performs a notify() when it's interrupted.
     *  Fail gracefully after the given milliseconds
     *  
     *  @param (long) 
     *  @see edu.oswego.cs.dl.util.concurrent.ReentrantLock#acquire()
     */
    public boolean tryLock(long milliseconds) {
        try {
            return attempt(milliseconds);
        } catch (InterruptedException interruptedexception) {
            return false;
        }
    }

    /**
     *  Attempt to acquire a lock until the timestamp is reached. Add 
     *  1 to the recursion count if the calling thread already owns the
     *  lock.  Otherwise block until free or until the given timestamp 
     *  is reached.
     * 
     *  @see Timestamp
     *  @see ReentrantLock.attempt(long);
     */
    public boolean tryLock(NSTimestamp nstimestamp) {
        return tryLock(nstimestamp.getTime() - System.currentTimeMillis());
    }

    /** Unlock the current lock precisely once.
     */
    public synchronized void unlock() {
        unlock(1);
    }

    /** Unlock the current lock count times.
     */
    public synchronized void unlock(long count) {
        if (owner_ != null && Thread.currentThread() != owner_) throw new IllegalStateException("Illegal Lock usage: unlocking thread not owner.");
        if (owner_ == null || holds_ == 0L) throw new IllegalStateException("Illegal Lock usage: unlock() called without a lock().");
        release(count);
    }

    public synchronized long recursionCount() {
        return holds();
    }

    public String toString() {
        long holds = holds();
        boolean oneHold = (holds == 1);
        boolean noHolds = (holds < 1 || owner_ == null);
        return getClass().getName() + " <" + ((noHolds) ? "Unlocked" : ("Locked " + holds + " time" + (oneHold ? "" : "s") + " by " + owner_)) + ">";
    }
}
