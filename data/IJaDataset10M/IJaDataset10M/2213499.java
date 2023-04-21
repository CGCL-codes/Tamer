package jaxlib.col.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import jaxlib.thread.Threads;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: ReferenceQueueThread.java 1511 2005-12-11 14:13:40Z joerg_wassmer $
 */
final class ReferenceQueueThread extends Thread {

    private static final Object lock = new Object();

    private static ReferenceQueueThread instance;

    private static long instanceCounter;

    @SuppressWarnings("unchecked")
    static ReferenceQueueThread.Registration register(Object userObject) {
        synchronized (ReferenceQueueThread.lock) {
            ReferenceQueueThread instance = ReferenceQueueThread.instance;
            if (instance == null) {
                instance = AccessController.doPrivileged(new PrivilegedAction<ReferenceQueueThread>() {

                    public ReferenceQueueThread run() {
                        ReferenceQueueThread t;
                        try {
                            t = new ReferenceQueueThread(Threads.getAccessibleTopThreadGroup());
                        } catch (SecurityException ex) {
                            t = new ReferenceQueueThread(null);
                            Logger.global.log(Level.WARNING, "Insufficient permissions, but can continue", ex);
                        }
                        ReferenceQueueThread.instance = t;
                        t.start();
                        return t;
                    }
                });
            }
            return new Registration(userObject, instance);
        }
    }

    private ReferenceQueue<Object> queue;

    private int mapInstanceCounter;

    private ReferenceQueueThread(ThreadGroup threadGroup) {
        super(threadGroup, ReferenceQueueThread.class.getName() + "-" + (++instanceCounter));
        if (ReferenceQueueThread.instance != null) throw new SecurityException();
        setDaemon(true);
        setPriority(Thread.MAX_PRIORITY);
        this.queue = new ReferenceQueue<Object>();
        try {
            setContextClassLoader(getClass().getClassLoader());
        } catch (SecurityException ex) {
            Logger.global.log(Level.WARNING, "Insufficient permissions, but can continue", ex);
        }
    }

    /**
   * Overriden to avoid unncessary InterruptedExceptions caused by malicious callers.
   * This thread implementation completely ignores interruptions.
   */
    @Override
    public final void interrupt() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void run() {
        synchronized (ReferenceQueueThread.lock) {
            if (ReferenceQueueThread.instance != this) throw new SecurityException();
        }
        boolean ok = false;
        try {
            run0();
            ok = true;
        } catch (Exception ex) {
            Logger.global.log(Level.SEVERE, "Internal error!", ex);
            throw new Error(ex);
        } finally {
            if (!ok) {
                synchronized (ReferenceQueueThread.lock) {
                    if (ReferenceQueueThread.instance == this) ReferenceQueueThread.instance = null;
                }
            }
            final ReferenceQueue<Object> queue = this.queue;
            this.queue = null;
            while (queue.poll() != null) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void run0() {
        final ReferenceQueue<Object> queue = this.queue;
        while (true) {
            Reference r;
            try {
                r = queue.remove();
            } catch (InterruptedException ex) {
                continue;
            }
            if (r instanceof ReferenceQueueThread.WeakEntry) {
                ((ReferenceQueueThread.WeakEntry) r).referenceCleared();
                r = null;
                continue;
            } else if (r instanceof ReferenceQueueThread.SoftEntry) {
                ((ReferenceQueueThread.SoftEntry) r).referenceCleared();
                r = null;
                continue;
            } else if (r instanceof ReferenceQueueThread.Registration) {
                r = null;
                synchronized (ReferenceQueueThread.lock) {
                    if (--this.mapInstanceCounter <= 0) {
                        if (ReferenceQueueThread.instance == this) ReferenceQueueThread.instance = null;
                        return;
                    }
                }
                continue;
            } else {
                Logger.global.severe("Assertion failed: " + r);
            }
        }
    }

    static final class Registration extends PhantomReference<Object> {

        private static ReferenceQueue<Object> getQueue(ReferenceQueueThread queueThread) {
            if (!Thread.holdsLock(ReferenceQueueThread.lock)) throw new SecurityException();
            return queueThread.queue;
        }

        final ReferenceQueue<Object> queue;

        private Registration(Object userObject, ReferenceQueueThread queueThread) {
            super(userObject, getQueue(queueThread));
            this.queue = queueThread.queue;
            queueThread.mapInstanceCounter++;
        }

        /**
     * Overwritten to ensure this PhantomReference gets enqueued even if the reference to this 
     * PhantomReference has been lost.
     */
        @Override
        protected void finalize() throws Throwable {
            clear();
            enqueue();
            super.finalize();
        }
    }

    abstract static class SoftEntry<T> extends SoftReference<T> {

        SoftEntry(T referent, ReferenceQueue<? super T> queue) {
            super(referent, queue);
        }

        abstract void referenceCleared();
    }

    abstract static class WeakEntry<T> extends WeakReference<T> {

        WeakEntry(T referent, ReferenceQueue<? super T> queue) {
            super(referent, queue);
        }

        abstract void referenceCleared();
    }
}
