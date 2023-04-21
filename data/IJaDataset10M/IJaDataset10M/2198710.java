package com.dukesoftware.utils.thread.pattern.workerthread;

import com.dukesoftware.utils.thread.IShutdownable;

public class ChannelWithThreadPool<T> implements IShutdownable, IChannel<T> {

    private final Object[] requestQueue;

    private final WorkerThreadPool threadPool;

    private int head = 0;

    private int tail = 0;

    private int count = 0;

    public ChannelWithThreadPool(int queSize, int threads) {
        threadPool = new WorkerThreadPool(threads);
        threadPool.init(this);
        requestQueue = new Object[queSize];
    }

    public synchronized void initialize() {
        for (int i = 0; i < requestQueue.length; i++) {
            requestQueue[i] = null;
        }
        head = tail = count = 0;
        threadPool.init(this);
    }

    public boolean isShutdownRequested() {
        return threadPool.isShutdownRequested();
    }

    public final synchronized void startWorkers() {
        if (!isShutdownRequested()) {
            threadPool.start();
        }
    }

    public synchronized void put(T request) {
        if (isShutdownRequested()) return;
        while (count >= requestQueue.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                if (isShutdownRequested()) return;
            }
        }
        requestQueue[tail] = request;
        tail = (tail + 1) % requestQueue.length;
        count++;
        notifyAll();
    }

    public synchronized T get() {
        if (isShutdownRequested()) return null;
        while (count <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                if (isShutdownRequested()) {
                    return null;
                }
            }
        }
        Object request = requestQueue[head];
        head = (head + 1) % requestQueue.length;
        count--;
        notifyAll();
        return (T) request;
    }

    public void shutdown() {
        threadPool.shutdown();
    }
}
