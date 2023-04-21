package net.sf.clairv.p2p.search.util;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author qiuyin
 *
 */
public class Counter {

    private ReentrantLock lock = new ReentrantLock();

    private int value;

    public Counter(int initial) {
        this.value = initial;
    }

    public void decrement() {
        try {
            lock.lock();
            value--;
        } finally {
            lock.unlock();
        }
    }

    public void increment() {
        try {
            lock.lock();
            value++;
        } finally {
            lock.unlock();
        }
    }

    public int getValue() {
        return value;
    }
}
