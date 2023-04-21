package net.sf.afluentes.concurrent.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import net.sf.afluentes.concurrent.ExecutorServiceFactory;

public class FixedThreadPoolFactory implements ExecutorServiceFactory {

    private int nThreads;

    private ThreadFactory threadFactory;

    public FixedThreadPoolFactory() {
        this(Runtime.getRuntime().availableProcessors(), null);
    }

    public FixedThreadPoolFactory(int nThreads) {
        this(nThreads, null);
    }

    public FixedThreadPoolFactory(int nThreads, ThreadFactory threadFactory) {
        if (nThreads <= 0) {
            throw new IllegalArgumentException("nThreads <= 0");
        }
        this.nThreads = nThreads;
        this.threadFactory = threadFactory;
    }

    @Override
    public ExecutorService newExecutorService() {
        ExecutorService executor;
        if (threadFactory == null) {
            executor = Executors.newFixedThreadPool(nThreads);
        } else {
            executor = Executors.newFixedThreadPool(nThreads, threadFactory);
        }
        return executor;
    }
}
