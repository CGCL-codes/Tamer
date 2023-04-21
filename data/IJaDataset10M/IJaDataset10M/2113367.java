package com.beesphere.products.throttling.executor.impl;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WaitPolicy implements RejectedExecutionHandler {

    private final long time;

    private final TimeUnit timeUnit;

    public WaitPolicy() {
        this(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

    public WaitPolicy(long time, TimeUnit timeUnit) {
        super();
        this.time = (time < 0 ? Long.MAX_VALUE : time);
        this.timeUnit = timeUnit;
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        try {
            if (e.isShutdown() || !e.getQueue().offer(r, time, timeUnit)) {
                throw new RejectedExecutionException();
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException(ie);
        }
    }
}
