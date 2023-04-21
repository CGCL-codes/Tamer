package alto.sys.lock;

/**
 * <p> A reentrant read- write lock using only CAS and synchros.
 * Appears to perform better than the java concurrent reentrant read
 * write lock.  </p>
 * 
 * <h3>Usage</h3>
 * <pre>
 *  light.lockEnter();
 *  try {
 *    //(critical region)
 *  }
 *  finally {
 *    light.lockExit();
 *  }
 * </pre>
 * <p> Assymmetric calls to enter and exit is a user bug that will put
 * this object into an inconsistent (dysfunctional) state, and should
 * lead to a dead lock.  </p>
 * 
 * <h3>Implementation</h3>
 * 
 * <p> This class subclasses the atomic boolean for space efficiency,
 * but is only intended to be used from the {@link
 * alto.sys.Lock} interface.  </p>
 * 
 * <p> This class manages reentries by counting symmetric entries and
 * exits, to change state on the outermost exit in the write lock
 * case. </p>
 * 
 * <h3>Operation</h3>
 * 
 * <p> When the atomic boolean is true, a writer may enter.  </p>
 * 
 * @author jdp
 */
public class Light extends java.util.concurrent.atomic.AtomicBoolean implements alto.sys.Lock.Advanced {

    private volatile java.lang.Thread writer;

    private volatile int enterRead, enterWrite;

    public Light() {
        super();
        this.set(true);
    }

    /**
     * @return Double counts lock reentries
     */
    public final int lockReadLockCount() {
        return this.enterRead;
    }

    public final boolean lockReadEnterTry() {
        if (this.compareAndSet(true, true)) {
            this.enterRead += 1;
            return true;
        } else {
            java.lang.Thread T = java.lang.Thread.currentThread();
            if (T == this.writer) {
                this.enterRead += 1;
                return true;
            } else return false;
        }
    }

    public final boolean lockReadEnterTry(long millis) throws java.lang.InterruptedException {
        if (this.compareAndSet(true, true)) {
            this.enterRead += 1;
            return true;
        } else {
            java.lang.Thread T = java.lang.Thread.currentThread();
            if (T == this.writer) {
                this.enterRead += 1;
                return true;
            } else {
                try {
                    long time = java.lang.System.currentTimeMillis();
                    long end = (time + millis);
                    while (time < end) {
                        if (this.compareAndSet(true, true)) {
                            this.enterRead += 1;
                            return true;
                        } else {
                            synchronized (this) {
                                this.wait(3);
                                time = java.lang.System.currentTimeMillis();
                            }
                        }
                    }
                    return false;
                } catch (java.lang.InterruptedException exc) {
                    throw new java.lang.RuntimeException(exc);
                }
            }
        }
    }

    public final void lockReadEnter() {
        if (this.compareAndSet(true, true)) {
            this.enterRead += 1;
            return;
        } else {
            java.lang.Thread T = java.lang.Thread.currentThread();
            if (T == this.writer) {
                this.enterRead += 1;
                return;
            } else {
                try {
                    while (true) {
                        if (this.compareAndSet(true, true)) {
                            this.enterRead += 1;
                            return;
                        } else {
                            synchronized (this) {
                                this.wait();
                            }
                        }
                    }
                } catch (java.lang.InterruptedException exc) {
                    throw new java.lang.RuntimeException(exc);
                }
            }
        }
    }

    public final synchronized void lockReadExit() {
        this.enterRead -= 1;
        this.notify();
    }

    /**
     */
    public final int lockWriteHoldCount() {
        if (null != this.writer) return 1; else return 0;
    }

    public final boolean lockWriteEnterTry() {
        java.lang.Thread T = java.lang.Thread.currentThread();
        if (T == this.writer) {
            this.enterWrite += 1;
            return true;
        } else if (this.compareAndSet(true, false)) {
            try {
                synchronized (this) {
                    while (true) {
                        if (0 == this.enterRead) {
                            this.writer = T;
                            this.enterWrite += 1;
                            return true;
                        } else {
                            this.wait();
                        }
                    }
                }
            } catch (java.lang.InterruptedException exc) {
                throw new java.lang.RuntimeException(exc);
            }
        } else return false;
    }

    public final boolean lockWriteEnterTry(long millis) throws java.lang.InterruptedException {
        if (this.lockWriteEnterTry()) {
            return true;
        } else {
            try {
                long time = java.lang.System.currentTimeMillis();
                long end = (time + millis);
                while (time < end) {
                    if (this.lockWriteEnterTry()) {
                        return true;
                    } else {
                        synchronized (this) {
                            this.wait(3);
                            time = java.lang.System.currentTimeMillis();
                        }
                    }
                }
                return false;
            } catch (java.lang.InterruptedException exc) {
                throw new java.lang.RuntimeException(exc);
            }
        }
    }

    public final void lockWriteEnter() {
        if (this.lockWriteEnterTry()) return; else {
            try {
                while (true) {
                    if (this.lockWriteEnterTry()) return; else {
                        synchronized (this) {
                            this.wait();
                        }
                    }
                }
            } catch (java.lang.InterruptedException exc) {
                throw new java.lang.RuntimeException(exc);
            }
        }
    }

    public final synchronized void lockWriteExit() {
        java.lang.Thread W = this.writer;
        java.lang.Thread T = java.lang.Thread.currentThread();
        if (T == W) {
            this.enterWrite -= 1;
            if (0 == this.enterWrite) {
                if (this.compareAndSet(false, true)) {
                    this.writer = null;
                    this.notify();
                } else throw new alto.sys.Error.State();
            } else return;
        } else if (null == W) throw new alto.sys.Error.State(); else throw new alto.sys.Error.State();
    }

    public final boolean isWriteLocked() {
        return (!this.get());
    }

    public final boolean isNotWriteLocked() {
        return this.get();
    }

    public final boolean isWriteLocker() {
        java.lang.Thread T = java.lang.Thread.currentThread();
        return (T == this.writer);
    }

    public final boolean isNotWriteLocker() {
        java.lang.Thread T = java.lang.Thread.currentThread();
        return (T != this.writer);
    }
}
