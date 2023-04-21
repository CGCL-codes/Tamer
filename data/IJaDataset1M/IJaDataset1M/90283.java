package org.freehold.jukebox.sem;

import org.freehold.jukebox.logger.Logger;

/**
 * Mutual exclusive, or mutex, semaphore.
 *
 * <p>
 *
 * Frankly speaking, not required at all, because Java already has a
 * mechanism which is <b>absolutely identical</b> to what the mutex
 * semaphores use, <code>synchronized</code> blocks. But on the other hand,
 * it's sometimes very painful to think about those things all the time,
 * and, besides, the concept of a mutex semaphore allows to engage the
 * object-oriented approach in a more clear, readable and understandable way
 * than the synchronized blocks.
 *
 * <p>
 *
 * <h3>Behavior Description</h3>
 *
 * Mutex Semaphore (<b>mutex</b> hereinafter), from a logical standpoint,
 * has two main methods:
 *
 * <ul>
 *
 * <li> Request and acquire the lock;
 *
 * <li> Release the lock.
 *
 * </ul>
 *
 * Implementation complications are that the threads come into play, and
 * every thread may request the lock more than once, so I have to keep the
 * record about those who requested (and acquired) the lock.
 *
 * <p>
 *
 * Fortunately, only one thread may acquire the lock simultaneously, so all
 * I have to do is just to keep track about the number of lock acquisition
 * attempts (stack depth, in other words).
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 1995-1998
 * @version $Id: MutexSemaphore.java,v 1.1 2000-12-15 06:41:49 vtt Exp $
 */
public class MutexSemaphore extends Semaphore {

    /**
     * Log facility to use.
     */
    public static final String CH_MUTEX = "MUTEX";

    protected Thread currentOwner = null;

    protected int depth = 0;

    public MutexSemaphore() {
        super(null);
    }

    public MutexSemaphore(Logger logger, Object owner) {
        super(logger, owner);
    }

    public MutexSemaphore(Logger logger, Object owner, String qualifier) {
        super(logger, owner, qualifier);
    }

    public MutexSemaphore(Logger logger, String name) {
        super(logger, name);
    }

    /**
     * Acquire the lock.
     * 
     * Wait for it forever.
     * <br>
     *
     * Multiple lock acquisition by the same thread is possible, the lock
     * will be released when the <code>release()</code> method will be
     * called the same number of times as the <code>waitFor()</code> and/or
     * <code>waitFor(millis)</code> was called.
     *
     * @return <code>true</code>.
     *
     * @exception InterruptedException if the thread was interrupted.
     */
    public synchronized boolean waitFor() throws InterruptedException {
        Thread current = Thread.currentThread();
        if (current == currentOwner) {
            depth++;
            return true;
        }
        while (currentOwner != null) {
            wait();
        }
        depth++;
        currentOwner = current;
        return true;
    }

    /**
     * Acquire the lock within a specified timeout.
     *
     * <p>
     *
     * Multiple lock acquisition by the same thread is possible, the lock
     * will be released when the <code>release()</code> method will be
     * called the same number of times as the <code>waitFor()</code> and/or
     * <code>waitFor(millis)</code> was called.
     *
     * @param millis Time to wait for the lock, in milliseconds.
     *
     * @return <code>true</code>.
     *
     * @exception InterruptedException if the thread was interrupted.
     *
     * @exception SemaphoreTimeoutException if the lock wasn't acquired
     * within a specified timeout.
     */
    public synchronized boolean waitFor(long millis) throws InterruptedException, SemaphoreTimeoutException {
        Thread current = Thread.currentThread();
        if (current == currentOwner) {
            depth++;
            return true;
        }
        if (currentOwner == null) {
            currentOwner = current;
            depth++;
            return true;
        }
        wait(millis);
        if (currentOwner == null) {
            currentOwner = current;
            depth++;
            return true;
        }
        throw new SemaphoreTimeoutException(Long.toString(millis));
    }

    public synchronized void release() {
        if (currentOwner == null) {
            throw new IllegalAccessError("Not owned by anybody");
        }
        Thread current = Thread.currentThread();
        if (current != currentOwner) {
            throw new IllegalAccessError("Not owner");
        }
        depth--;
        if (depth == 0) {
            currentOwner = null;
            notify();
        }
    }
}
