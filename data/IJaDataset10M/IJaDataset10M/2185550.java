package local.sma.common.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Steve Ash
 * 
 */
public class PausableThreadPoolExecutor extends ThreadPoolExecutor implements PausableExecutorService {

    /**
	 * Thrown whenever a task is waiting to execute while the executor is paused and 
	 * the task is interrupted (presumably during a shutdownNow event); must be runtime 
	 * because of the contract of beforeExecute
	 * @author Steve Ash
	 */
    public static class PausedTaskInterruptedException extends RuntimeException {

        private static final long serialVersionUID = -3069562134819805339L;

        public PausedTaskInterruptedException(Throwable cause) {
            super(cause);
        }
    }

    private boolean isPaused;

    private int runningTasks = 0;

    private final ReentrantLock pauseLock = new ReentrantLock();

    private final Condition unpaused = pauseLock.newCondition();

    private final Condition noRunningTasks = pauseLock.newCondition();

    public static PausableThreadPoolExecutor makeFixedThreadPool(int workerCount) {
        return new PausableThreadPoolExecutor(workerCount, workerCount, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused) {
                unpaused.await();
            }
            runningTasks += 1;
        } catch (InterruptedException e) {
            throw new PausedTaskInterruptedException(e);
        } finally {
            pauseLock.unlock();
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        pauseLock.lock();
        try {
            runningTasks -= 1;
            if (runningTasks <= 0) noRunningTasks.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    /**
	 * Begins the pause process and returns a future for the caller to determine
	 * when the future is complete;
	 * NOTE if the executor is in a paused state when it is shutdown any tasks held waiting for
	 * the executor to resume will throw PausedTaskInterruptedExceptions which will be caught by
	 * the Java Thread default exception handler, which writes the stack trace to stderr; unfortunately
	 * there is no way to prevent this without just allowing the task to start running, which
	 * seems potentially worse.  Thus, probably prefer to not shutdown paused executors
	 * @return
	 */
    public Future<Void> pause() {
        pauseLock.lock();
        try {
            isPaused = true;
            return makePauseFuture();
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    public boolean isPaused() {
        pauseLock.lock();
        try {
            return isPaused;
        } finally {
            pauseLock.unlock();
        }
    }

    private Future<Void> makePauseFuture() {
        return new Future<Void>() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                resume();
                return true;
            }

            @Override
            public Void get() throws InterruptedException, ExecutionException {
                pauseLock.lock();
                try {
                    while (runningTasks > 0) noRunningTasks.await();
                    return null;
                } finally {
                    pauseLock.unlock();
                }
            }

            @Override
            public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                pauseLock.lock();
                try {
                    while (runningTasks > 0) {
                        boolean completed = noRunningTasks.await(timeout, unit);
                        if (!completed) throw new TimeoutException();
                    }
                    return null;
                } finally {
                    pauseLock.unlock();
                }
            }

            @Override
            public boolean isCancelled() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isDone() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
