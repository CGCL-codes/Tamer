package test.hover.tools.concurrent.framework.threadpackage.semaphore;

/**
 * This class implements a simple Mutex with acquire and release capabilities.
 *
 * @author Karthik Rangaraju
 */
public class Mutex {

    private boolean mAcquired = false;

    /**
     * Tries to acquire the mutex. If the mutex is already acquired, it blocks till the Mutex
     * is released by some other thread
     * Warning: If a thread that has acquired a mutex tries to reacquire it, it will result in a
     * deadlock
     * @throws InterruptedException if the thread is interrupted when blocked
     */
    public synchronized void acquire() throws InterruptedException {
        while (mAcquired == true) {
            wait();
        }
        mAcquired = true;
    }

    /**
     * This method releases the Mutex held by the thread. A thread other than the one that
     * acquired the Mutex can release it.
     */
    public synchronized void release() {
        mAcquired = false;
        notify();
    }
}
