package com.mycila.sandbox.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class RunInParallel {

    private final CountDownLatch ready;

    private final CountDownLatch finished;

    private final Thread[] threads;

    private final Task task;

    public RunInParallel(int nThreads, Task task) {
        ready = new CountDownLatch(1);
        finished = new CountDownLatch(nThreads);
        threads = new Thread[nThreads];
        this.task = task;
    }

    public void start() {
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Runnable() {

                public void run() {
                    try {
                        ready.await();
                        try {
                            task.run();
                        } catch (Exception e) {
                            if (e instanceof RuntimeException) throw (RuntimeException) e;
                            throw new RuntimeException(e.getMessage(), e);
                        } finally {
                            finished.countDown();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }, "RunInParallel-" + i);
            threads[i].start();
        }
        ready.countDown();
    }

    public void stop() {
        for (Thread thread : threads) thread.interrupt();
    }

    public void awaitTermination() {
        try {
            finished.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static interface Task {

        void run() throws Exception;
    }
}
