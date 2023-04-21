package com.dukesoftware.utils.thread.pattern.workerthread;

/**
 * 
 * 
 * 
 * 
 *
 *
 */
public class Channel<T> implements IChannel<T> {

    private final Object[] buff;

    private int head = 0;

    private int tail = 0;

    private int count = 0;

    public Channel(int size) {
        buff = new Command[size];
    }

    public synchronized void initialize() {
        for (int i = 0; i < buff.length; i++) {
            buff[i] = null;
        }
        head = tail = count = 0;
    }

    public synchronized void put(T elem) {
        while (count >= buff.length) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        buff[tail] = elem;
        tail = (tail + 1) % buff.length;
        count++;
        notifyAll();
    }

    public synchronized T get() {
        while (count <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        Object elem = buff[head];
        head = (head + 1) % buff.length;
        count--;
        notifyAll();
        return (T) elem;
    }
}
