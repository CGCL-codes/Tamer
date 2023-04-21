package org.jtools.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class QueuedAsyncWorker<O, I, C> implements Async.Left<O> {

    private final Queue<I> inbound;

    private final Queue<O> results = new ConcurrentLinkedQueue<O>();

    private final int workerCount;

    private final AtomicBoolean stop = new AtomicBoolean();

    private final AtomicInteger running = new AtomicInteger();

    private final AtomicInteger working = new AtomicInteger();

    private final boolean checkWorking;

    protected abstract O process(I i, C context);

    private final String name;

    protected void shutdown(C context, boolean regular) {
    }

    protected C newContext() {
        return null;
    }

    public QueuedAsyncWorker(String name, int workerCount, Queue<I> inbound, boolean checkWorking) {
        this.name = name == null ? getClass().getSimpleName() : name;
        this.workerCount = workerCount;
        this.inbound = inbound;
        this.checkWorking = checkWorking;
    }

    public QueuedAsyncWorker(int workerCount, Queue<I> inbound, boolean checkWorking) {
        this(null, workerCount, inbound, checkWorking);
    }

    public final Queue<I> getInbound() {
        return inbound;
    }

    public final Queue<O> getOutbound() {
        return results;
    }

    private void sleep() {
        try {
            Thread.sleep(50L);
        } catch (InterruptedException e) {
        }
    }

    protected void push(O o) {
        if (o != null) while (!results.offer(o)) sleep();
    }

    public Queue<O> start() {
        for (int i = 0; i < workerCount; i++) new Thread(new Runnable() {

            @Override
            public void run() {
                I item;
                C context = null;
                boolean shutdown = false;
                try {
                    context = newContext();
                    item = inbound.poll();
                    while (item != null || (!stop.get()) || (checkWorking && working.get() > 0)) {
                        if (item == null) sleep(); else {
                            working.incrementAndGet();
                            try {
                                push(process(item, context));
                            } catch (Exception e) {
                                System.err.println(Thread.currentThread().getName() + ": Exception caught. See below");
                                e.printStackTrace();
                            } finally {
                                working.decrementAndGet();
                            }
                        }
                        item = inbound.poll();
                    }
                    shutdown = true;
                    shutdown(context, true);
                } finally {
                    if (!shutdown) shutdown(context, false);
                    running.decrementAndGet();
                    synchronized (stop) {
                        stop.notifyAll();
                    }
                }
            }
        }, name + "[" + (1 + i) + "/" + workerCount + "]").start();
        running.set(workerCount);
        return results;
    }

    public void complete() {
        stop.set(true);
        @SuppressWarnings("unused") int r;
        while ((r = running.get()) > 0) {
            synchronized (stop) {
                try {
                    stop.wait(1000L);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
