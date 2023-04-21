package org.netxilia.api.impl.concurrent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * This is a copy the Google concurrent SameThreadExecutor. See {@link MoreExecutors#sameThreadExecutor()}. The only
 * difference is that the submitted tasks are {@link ListenableFutureTask}
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class SameThreadExecutor extends AbstractExecutorService {

    /**
	 * Lock used whenever accessing the state variables (runningTasks, shutdown, terminationCondition) of the executor
	 */
    private final Lock lock = new ReentrantLock();

    /** Signaled after the executor is shutdown and running tasks are done */
    private final Condition termination = lock.newCondition();

    private int runningTasks = 0;

    private boolean shutdown = false;

    public void execute(Runnable command) {
        startTask();
        try {
            command.run();
        } finally {
            endTask();
        }
    }

    public boolean isShutdown() {
        lock.lock();
        try {
            return shutdown;
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();
        try {
            shutdown = true;
        } finally {
            lock.unlock();
        }
    }

    public List<Runnable> shutdownNow() {
        shutdown();
        return Collections.emptyList();
    }

    public boolean isTerminated() {
        lock.lock();
        try {
            return shutdown && runningTasks == 0;
        } finally {
            lock.unlock();
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            for (; ; ) {
                if (isTerminated()) {
                    return true;
                } else if (nanos <= 0) {
                    return false;
                } else {
                    nanos = termination.awaitNanos(nanos);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
	 * Checks if the executor has been shut down and increments the running task count.
	 * 
	 * @throws RejectedExecutionException
	 *             if the executor has been previously shutdown
	 */
    private void startTask() {
        lock.lock();
        try {
            if (isShutdown()) {
                throw new RejectedExecutionException("Executor already shutdown");
            }
            runningTasks++;
        } finally {
            lock.unlock();
        }
    }

    /**
	 * Decrements the running task count.
	 */
    private void endTask() {
        lock.lock();
        try {
            runningTasks--;
            if (isTerminated()) {
                termination.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new ListenableFutureTask<T>(callable);
    }

    @Override
    protected <T extends Object> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new ListenableFutureTask<T>(runnable, value);
    }

    ;
}
