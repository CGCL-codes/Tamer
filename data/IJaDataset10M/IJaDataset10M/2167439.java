package ch.iserver.ace.util;

/**
 *
 */
public class ReentrantLock implements Lock {

    private edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock lock;

    public ReentrantLock() {
        this.lock = new edu.emory.mathcs.backport.java.util.concurrent.locks.ReentrantLock();
    }

    /**
	 * @see ch.iserver.ace.util.Lock#isOwner(java.lang.Thread)
	 */
    public boolean isOwner(Thread thread) {
        return lock.isHeldByCurrentThread();
    }

    /**
	 * @see ch.iserver.ace.util.Lock#lock()
	 */
    public void lock() {
        lock.lock();
    }

    /**
	 * @see ch.iserver.ace.util.Lock#unlock()
	 */
    public void unlock() {
        lock.unlock();
    }
}
