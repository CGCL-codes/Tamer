package com.rapidminer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ProcessStoppedException;

/** Helper class to schedule {@link AsynchronousTask}s and wait for their completion.
 *  This class maintains a thread pool, lock to synchronize any work that cannot be
 *  parallelized, and an atomic counter for pending jobs.
 *  
 *  To use this class, implement the {@link #run(Object)} for doing the actual work.
 *  As a side effect, this method will schedule various tasks by making calls to
 *  {@link #executeAsynchronously(AsynchronousTask)}. Every work done by this class 
 *  will be synchronized on the {@link #getLock()}.
 *  It is the responsibility of the {@link AsynchronousTask} to release the lock
 *  at an appropriate time in order to actually be able to make use of concurrency. 
 * 
 * @author Simon Fischer
 *
 */
public abstract class ConcurrentOperationHelper<T> {

    private ExecutorService threadPool;

    private Lock lock = new ReentrantLock();

    private AtomicInteger completionCount;

    private Throwable caughtException;

    private Operator operator;

    private boolean rethrowOnCompletion;

    private boolean mustLock;

    public ConcurrentOperationHelper(Operator operator) {
        this(operator, true);
    }

    public ConcurrentOperationHelper(Operator operator, boolean mustLock) {
        this.operator = operator;
        this.mustLock = mustLock;
        ConcurrencyTools.installThreadPoolParameters(operator);
    }

    /** Initializes a counter for pending tasks and a thread pool.
	 *  The actual work is delegated to {@link #run(T)}.
	 *  After execution, the thread pool is shut down, and we wait for all tasks to complete.
	 *  If any of the tasks has thrown an exception, it is rethrown if desired.
	 */
    public void run(T argument, boolean rethrowOnCompletion) throws OperatorException {
        this.rethrowOnCompletion = rethrowOnCompletion;
        caughtException = null;
        completionCount = new AtomicInteger();
        try {
            run(argument);
        } finally {
            if (threadPool != null) {
                threadPool.shutdown();
                threadPool = null;
            }
        }
        ConcurrentOperationHelper.waitForZero(completionCount, lock, operator.getLogger());
        if (this.rethrowOnCompletion) {
            ConcurrentOperationHelper.rethrow(caughtException);
        }
    }

    protected abstract void run(T argument) throws OperatorException;

    private ExecutorService getThreadPool() throws OperatorException {
        if (this.threadPool == null) {
            this.threadPool = ConcurrencyTools.getThreadPool(operator);
        }
        return this.threadPool;
    }

    public void executeAsynchronously(final AsynchronousTask task) throws OperatorException {
        if (rethrowOnCompletion && (caughtException != null)) {
            operator.getLogger().info("Ignoring asynchronous task, I already got an exception.");
            return;
        }
        operator.checkForStop();
        completionCount.incrementAndGet();
        getThreadPool().execute(new Runnable() {

            @Override
            public void run() {
                try {
                    if (mustLock) {
                        lock.lock();
                    }
                    operator.checkForStop();
                    task.run();
                } catch (RuntimeException e) {
                    operator.getLogger().log(Level.WARNING, "Error executing task asynchronously: " + e, e);
                    if (caughtException == null) {
                        caughtException = e;
                    }
                } catch (OperatorException e) {
                    if (!(e instanceof ProcessStoppedException)) {
                        operator.getLogger().log(Level.WARNING, "Error executing task asynchronously: " + e, e);
                    }
                    if (caughtException == null) {
                        caughtException = e;
                    }
                } catch (OutOfMemoryError e) {
                    operator.getLogger().log(Level.WARNING, "Error executing task asynchronously: " + e, e);
                    if (caughtException == null) {
                        caughtException = e;
                    }
                } finally {
                    if (mustLock) {
                        try {
                            lock.unlock();
                        } catch (IllegalMonitorStateException e) {
                            operator.getLogger().warning("Lock already unlocked.");
                        }
                    }
                    synchronized (lock) {
                        completionCount.decrementAndGet();
                        lock.notify();
                    }
                }
            }
        });
    }

    public Lock getLock() {
        return lock;
    }

    public Operator getOperator() {
        return operator;
    }

    /** Synchronizes on the given lock and waits for the counter to become zero (or less). */
    public static void waitForZero(AtomicInteger counter, Object lock, Logger logger) {
        boolean continueWaiting = true;
        while (continueWaiting) {
            synchronized (lock) {
                int value = counter.get();
                if (value > 0) {
                    logger.fine("Waiting for " + value + " iterations to complete.");
                    continueWaiting = true;
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        logger.log(Level.WARNING, "Interrupted while waiting for counter: " + e, e);
                    }
                } else {
                    continueWaiting = false;
                }
            }
        }
    }

    /** Re-throws this exception if it is not null and either an unchecked exception
	 *  or an operator exception. */
    public static void rethrow(Throwable exception) throws OperatorException {
        if (exception instanceof OperatorException) {
            throw (OperatorException) exception;
        } else if (exception instanceof RuntimeException) {
            throw (RuntimeException) exception;
        } else if (exception instanceof Error) {
            throw (Error) exception;
        } else if (exception != null) {
            throw new RuntimeException("Caught unknown exception type: " + exception.getClass(), exception);
        }
    }
}
