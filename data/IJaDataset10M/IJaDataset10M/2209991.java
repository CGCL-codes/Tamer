package com.jme3.app;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>AppTask</code> is used in <code>AppTaskQueue</code> to manage tasks that have
 * yet to be accomplished. The AppTask system is used to execute tasks either
 * in the OpenGL/Render thread, or outside of it.
 *
 * @author Matthew D. Hicks, lazloh
 */
public class AppTask<V> implements Future<V> {

    private static final Logger logger = Logger.getLogger(AppTask.class.getName());

    private final Callable<V> callable;

    private V result;

    private ExecutionException exception;

    private boolean cancelled, finished;

    private final ReentrantLock stateLock = new ReentrantLock();

    private final Condition finishedCondition = stateLock.newCondition();

    /**
     * Create an <code>AppTask</code> that will execute the given 
     * {@link Callable}.
     * 
     * @param callable The callable to be executed
     */
    public AppTask(Callable<V> callable) {
        this.callable = callable;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        stateLock.lock();
        try {
            if (result != null) {
                return false;
            }
            cancelled = true;
            finishedCondition.signalAll();
            return true;
        } finally {
            stateLock.unlock();
        }
    }

    public V get() throws InterruptedException, ExecutionException {
        stateLock.lock();
        try {
            while (!isDone()) {
                finishedCondition.await();
            }
            if (exception != null) {
                throw exception;
            }
            return result;
        } finally {
            stateLock.unlock();
        }
    }

    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        stateLock.lock();
        try {
            if (!isDone()) {
                finishedCondition.await(timeout, unit);
            }
            if (exception != null) {
                throw exception;
            }
            if (result == null) {
                throw new TimeoutException("Object not returned in time allocated.");
            }
            return result;
        } finally {
            stateLock.unlock();
        }
    }

    public boolean isCancelled() {
        stateLock.lock();
        try {
            return cancelled;
        } finally {
            stateLock.unlock();
        }
    }

    public boolean isDone() {
        stateLock.lock();
        try {
            return finished || cancelled || (exception != null);
        } finally {
            stateLock.unlock();
        }
    }

    public Callable<V> getCallable() {
        return callable;
    }

    public void invoke() {
        try {
            final V tmpResult = callable.call();
            stateLock.lock();
            try {
                result = tmpResult;
                finished = true;
                finishedCondition.signalAll();
            } finally {
                stateLock.unlock();
            }
        } catch (Exception e) {
            logger.logp(Level.SEVERE, this.getClass().toString(), "invoke()", "Exception", e);
            stateLock.lock();
            try {
                exception = new ExecutionException(e);
                finishedCondition.signalAll();
            } finally {
                stateLock.unlock();
            }
        }
    }
}
