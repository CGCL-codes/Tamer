package com.cell.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import com.cell.CUtil;

public class ThreadPool implements ThreadPoolService {

    /** temp workaround for VM issue */
    private static final long MAX_DELAY = Long.MAX_VALUE / 1000000 / 2;

    public final String name;

    private ThreadPoolExecutor gameThreadPool;

    private ScheduledThreadPoolExecutor gameScheduledThreadPool;

    private ShutDownHook shutdown_hook = new ShutDownHook();

    private boolean shutdown = false;

    public ThreadPool(String pool_name, int scheduled_corePoolSize, int threadpool_corePoolSize, int threadpool_maximumPoolSize, int priority, long keep_alive_time_sec, BlockingQueue<Runnable> executor_queue) {
        this.name = pool_name;
        if (scheduled_corePoolSize > 0) {
            gameScheduledThreadPool = new ScheduledThreadPoolExecutor(scheduled_corePoolSize, new PriorityThreadFactory(name + " Scheduled", priority));
        }
        if (threadpool_corePoolSize > 0) {
            gameThreadPool = new ThreadPoolExecutor(threadpool_corePoolSize, threadpool_maximumPoolSize, keep_alive_time_sec, TimeUnit.SECONDS, executor_queue, new PriorityThreadFactory(name + " Executor", priority));
        }
        Runtime.getRuntime().addShutdownHook(shutdown_hook);
    }

    public ThreadPool(String pool_name, int scheduled_corePoolSize, int threadpool_corePoolSize, int threadpool_maximumPoolSize, int priority, long keep_alive_time_sec) {
        this(pool_name, scheduled_corePoolSize, threadpool_corePoolSize, threadpool_maximumPoolSize, Thread.NORM_PRIORITY, keep_alive_time_sec, new LinkedBlockingQueue<Runnable>());
    }

    public ThreadPool(String pool_name, int scheduled_corePoolSize, int threadpool_corePoolSize, int threadpool_maximumPoolSize, int priority) {
        this(pool_name, scheduled_corePoolSize, threadpool_corePoolSize, threadpool_maximumPoolSize, Thread.NORM_PRIORITY, 60L, new LinkedBlockingQueue<Runnable>());
    }

    public ThreadPool(String pool_name, int scheduled_corePoolSize, int threadpool_corePoolSize, int threadpool_maximumPoolSize) {
        this(pool_name, scheduled_corePoolSize, threadpool_corePoolSize, threadpool_maximumPoolSize, Thread.NORM_PRIORITY);
    }

    public ThreadPool(String pool_name) {
        this(pool_name, Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() + 1);
    }

    public ThreadPoolExecutor getExecutor() {
        return gameThreadPool;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        shutdownNow();
    }

    public ScheduledThreadPoolExecutor getScheduledExecutor() {
        return gameScheduledThreadPool;
    }

    public static long validateDelay(long delay) {
        if (delay < 0) {
            delay = 0;
        } else if (delay > MAX_DELAY) {
            delay = MAX_DELAY;
        }
        return delay;
    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        try {
            delay = ThreadPool.validateDelay(delay);
            return gameScheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long initial, long period) {
        try {
            period = ThreadPool.validateDelay(period);
            initial = ThreadPool.validateDelay(initial);
            return gameScheduledThreadPool.scheduleAtFixedRate(r, initial, period, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Future<?> executeTask(Runnable r) {
        return gameThreadPool.submit(r);
    }

    public String getStats() {
        StringBuilder lines = new StringBuilder();
        if (gameScheduledThreadPool != null) {
            lines.append("[" + name + "] Scheduled:" + "\n");
            lines.append(getStatus(gameScheduledThreadPool));
            CUtil.toStatusSeparator(lines);
        }
        if (gameThreadPool != null) {
            lines.append("[" + name + "] Executor:" + "\n");
            lines.append(getStatus(gameThreadPool));
            CUtil.toStatusSeparator(lines);
        }
        ;
        lines.append("[heap status]\n");
        lines.append(getHeapStatus());
        CUtil.toStatusSeparator(lines);
        return lines.toString();
    }

    /**
	 *
	 */
    public void shutdown() {
        synchronized (shutdown_hook) {
            if (!shutdown) {
                Runtime.getRuntime().removeShutdownHook(shutdown_hook);
                shutdown = true;
                try {
                    if (gameScheduledThreadPool != null) {
                        gameScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);
                        gameScheduledThreadPool.shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (gameThreadPool != null) {
                        gameThreadPool.awaitTermination(1, TimeUnit.SECONDS);
                        gameThreadPool.shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("ThreadPool \"" + name + "\" : All Threads are now stoped");
            }
        }
    }

    public void shutdownNow() {
        synchronized (shutdown_hook) {
            if (!shutdown) {
                Runtime.getRuntime().removeShutdownHook(shutdown_hook);
                shutdown = true;
                try {
                    if (gameScheduledThreadPool != null) {
                        gameScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);
                        gameScheduledThreadPool.shutdownNow();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (gameThreadPool != null) {
                        gameThreadPool.awaitTermination(1, TimeUnit.SECONDS);
                        gameThreadPool.shutdownNow();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("ThreadPool \"" + name + "\" : All Threads are now stoped");
            }
        }
    }

    public boolean isShutdown() {
        return shutdown;
    }

    /**
	 * 尝试从工作队列移除所有已取消的Future任务。
	 */
    public void purge() {
        if (gameScheduledThreadPool != null) {
            gameScheduledThreadPool.purge();
        }
        if (gameThreadPool != null) {
            gameThreadPool.purge();
        }
    }

    private class PriorityThreadFactory implements ThreadFactory {

        private int _prio;

        private String _name;

        private AtomicInteger _threadNumber = new AtomicInteger(1);

        private ThreadGroup _group;

        public PriorityThreadFactory(String name, int prio) {
            _prio = prio;
            _name = name;
            _group = new ThreadGroup(_name);
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(_group, r);
            t.setName(_name + "-" + _threadNumber.getAndIncrement());
            t.setPriority(_prio);
            return t;
        }
    }

    private class ShutDownHook extends Thread {

        public void run() {
            System.out.println("ThreadPool \"" + name + "\" : ShutdownHook running...");
            purge();
            try {
                if (gameScheduledThreadPool != null) {
                    gameScheduledThreadPool.awaitTermination(1, TimeUnit.SECONDS);
                    gameScheduledThreadPool.shutdownNow();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (gameThreadPool != null) {
                    gameThreadPool.awaitTermination(1, TimeUnit.SECONDS);
                    gameThreadPool.shutdownNow();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("ThreadPool \"" + name + "\" : All Threads are now stoped");
        }
    }

    public static String getStatus(ScheduledThreadPoolExecutor tp) {
        StringBuilder lines = new StringBuilder();
        CUtil.toStatusLine("ActiveThreads", tp.getActiveCount(), lines);
        CUtil.toStatusLine("CorePoolSize", tp.getCorePoolSize(), lines);
        CUtil.toStatusLine("PoolSize", tp.getPoolSize(), lines);
        CUtil.toStatusLine("MaximumPoolSize", tp.getMaximumPoolSize(), lines);
        CUtil.toStatusLine("CompletedTasks", tp.getCompletedTaskCount(), lines);
        CUtil.toStatusLine("ScheduledTasks", (tp.getTaskCount() - tp.getCompletedTaskCount()), lines);
        return lines.toString();
    }

    public static String getStatus(ThreadPoolExecutor tp) {
        StringBuilder lines = new StringBuilder();
        CUtil.toStatusLine("ActiveThreads", tp.getActiveCount(), lines);
        CUtil.toStatusLine("CorePoolSize", tp.getCorePoolSize(), lines);
        CUtil.toStatusLine("MaximumPoolSize", tp.getMaximumPoolSize(), lines);
        CUtil.toStatusLine("LargestPoolSize", tp.getLargestPoolSize(), lines);
        CUtil.toStatusLine("PoolSize", tp.getPoolSize(), lines);
        CUtil.toStatusLine("CompletedTasks", tp.getCompletedTaskCount(), lines);
        CUtil.toStatusLine("QueuedTasks", tp.getQueue().size(), lines);
        return lines.toString();
    }

    public static String getHeapStatus() {
        Runtime runtime = Runtime.getRuntime();
        long free_memory = runtime.freeMemory();
        long total_memory = runtime.totalMemory();
        long max_memory = runtime.maxMemory();
        StringBuilder lines = new StringBuilder();
        CUtil.toStatusLine("UsedMemory", CUtil.toBytesSizeString(total_memory - free_memory), lines);
        CUtil.toStatusLine("FreeMemory", CUtil.toBytesSizeString(free_memory), lines);
        CUtil.toStatusLine("TotalMemory", CUtil.toBytesSizeString(total_memory), lines);
        CUtil.toStatusLine("MaxMemory", CUtil.toBytesSizeString(max_memory), lines);
        return lines.toString();
    }
}
