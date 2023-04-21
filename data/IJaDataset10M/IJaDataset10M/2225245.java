package com.jogamp.common.util.locks;

import jogamp.common.Debug;
import java.security.AccessController;

/**
 * Specifying a thread blocking lock implementation
 */
public interface Lock {

    /** Enable via the property <code>jogamp.debug.Lock</code> */
    public static final boolean DEBUG = Debug.debug("Lock");

    /** Defines the default {@link #TIMEOUT} value */
    public static final long DEFAULT_TIMEOUT = 5000;

    /** 
     * Defines the <code>TIMEOUT</code> for {@link #lock()} in ms,
     * and defaults to {@link #DEFAULT_TIMEOUT}.<br>
     * It can be overriden via the system property <code>jogamp.common.utils.locks.Lock.timeout</code>.
     */
    public static final long TIMEOUT = Debug.getLongProperty("jogamp.common.utils.locks.Lock.timeout", true, AccessController.getContext(), DEFAULT_TIMEOUT);

    /**
     * Blocking until the lock is acquired by this Thread or {@link #TIMEOUT} is reached.
     *
     * @throws RuntimeException in case of {@link #TIMEOUT}
     */
    void lock() throws RuntimeException;

    /**
     * Blocking until the lock is acquired by this Thread or <code>maxwait</code> in ms is reached.
     *
     * @param maxwait Maximum time in ms to wait to acquire the lock. If this value is zero,
     *                the call returns immediately either without being able
     *                to acquire the lock, or with acquiring the lock directly while ignoring any scheduling order.
     * @return true if the lock has been acquired within <code>maxwait</code>, otherwise false
     *
     * @throws RuntimeException in case of {@link #TIMEOUT}
     */
    boolean tryLock(long maxwait) throws RuntimeException;

    /**
     * Unblocking.
     *
     * @throws RuntimeException in case the lock is not acquired by this thread.
     */
    void unlock() throws RuntimeException;

    boolean isLocked();
}
