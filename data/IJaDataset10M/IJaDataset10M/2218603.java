package com.peterhi;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ThreadManager {

    private static final ThreadManager threadManager = new ThreadManager();

    public static ThreadManager getThreadManager() {
        return threadManager;
    }

    private int executorCount = 5;

    private ExecutorService executor = Executors.newFixedThreadPool(executorCount);

    private int schedulerCount = 5;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(schedulerCount);

    private ExecutorService looper = Executors.newCachedThreadPool();

    private Collection<UncaughtExceptionHandler> handlers = new HashSet<UncaughtExceptionHandler>();

    public ThreadManager() {
    }

    public ExecutorService getExecutor() {
        return new NonShutdownExecutorService(executor);
    }

    public ScheduledExecutorService getScheduler() {
        return new NonShutdownScheduledExecutorService(scheduler);
    }

    public ExecutorService getLooper() {
        return new NonShutdownExecutorService(looper);
    }

    public synchronized void addHandler(UncaughtExceptionHandler h) {
        if (h == null) {
            throw new NullPointerException();
        }
        handlers.add(h);
    }

    public synchronized void removeHandler(UncaughtExceptionHandler h) {
        if (h == null) {
            throw new NullPointerException();
        }
        handlers.remove(h);
    }

    public ScheduledFuture<?> schedule(final Runner r, long t, TimeUnit tu) {
        return scheduler.schedule(new Runnable() {

            public void run() {
                try {
                    r.run();
                } catch (Exception ex) {
                    uncaught(ex);
                }
            }
        }, t, tu);
    }

    public ScheduledFuture scheduleAtFixedRate(final Runner r, long init, long delay, TimeUnit tu) {
        return scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                try {
                    r.run();
                } catch (Exception ex) {
                    uncaught(ex);
                }
            }
        }, init, delay, tu);
    }

    public ScheduledFuture scheduleWithFixedDelay(final Runner r, long init, long delay, TimeUnit tu) {
        return scheduler.scheduleWithFixedDelay(new Runnable() {

            public void run() {
                try {
                    r.run();
                } catch (Exception ex) {
                    uncaught(ex);
                }
            }
        }, init, delay, tu);
    }

    private synchronized void uncaught(Throwable th) {
        for (UncaughtExceptionHandler h : handlers) {
            h.uncaughtException(Thread.currentThread(), th);
        }
    }
}
