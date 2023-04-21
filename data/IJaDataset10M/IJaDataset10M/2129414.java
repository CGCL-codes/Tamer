package com.notuvy.thread;

import java.util.concurrent.*;
import org.apache.log4j.Logger;

/**
 * A separate thread engine that executes a given Runnable.
 * The purpose of this is to avoid doing "new Thread()" an arbitrary
 * number of times.  Instead, a single thread is kept and it processes
 * each give Runnable.
 *
 * @author  murali
 */
public class ThreadWorker implements ThreadControllable {

    protected static final Logger LOG = Logger.getLogger(ThreadWorker.class);

    private static final Future<?> DUMMY_FUTURE = new Future<Integer>() {

        public boolean cancel(boolean b) {
            return true;
        }

        public boolean isCancelled() {
            return false;
        }

        public boolean isDone() {
            return true;
        }

        public Integer get() throws InterruptedException, ExecutionException {
            return 0;
        }

        public Integer get(long l, TimeUnit pTimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return 0;
        }
    };

    private final ThreadControl fControl = new ThreadControl();

    private final ExecutorService fService;

    public ThreadWorker(String pName) {
        this(pName, 1);
    }

    public ThreadWorker(String pName, int pNumberThreads) {
        fService = Executors.newFixedThreadPool(pNumberThreads, new NamedThreadFactory(pName));
    }

    public ThreadControl getControl() {
        return fControl;
    }

    public void enqueueSync(Runnable pRunnable) {
        if (getControl().isAlive()) {
            Future<?> future = enqueueAsync(pRunnable);
            try {
                future.get();
            } catch (InterruptedException ie) {
                LOG.error("Interrupted", ie);
            } catch (ExecutionException ee) {
                LOG.error("Thread execution error", ee);
            }
        } else {
            LOG.warn("Worker is shutting down.");
        }
    }

    public Future<?> enqueueAsync(final Runnable pRunnable) {
        Future<?> future = DUMMY_FUTURE;
        if (getControl().isAlive()) {
            Runnable wrapper = new Runnable() {

                public void run() {
                    try {
                        getControl().setRunning(true);
                        pRunnable.run();
                    } finally {
                        getControl().setRunning(false);
                    }
                }
            };
            future = fService.submit(wrapper);
        } else {
            LOG.warn("Worker is shutting down.");
        }
        return future;
    }
}
