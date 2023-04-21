package com.sunsoft.sample.concurrent;

public class BoundedBuffer2<V> extends BaseBoundedBuffer<V> {

    public BoundedBuffer2(int size) {
        super(size);
    }

    public synchronized void put(V v) throws InterruptedException {
        while (isFull()) wait();
        doPut(v);
        notifyAll();
    }

    public synchronized V take() throws InterruptedException {
        while (isEmpty()) wait();
        V v = doTake();
        notifyAll();
        return v;
    }
}
