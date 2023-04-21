package net.java.nioserver.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import net.jcip.annotations.ThreadSafe;

/**
 * @author Leonid Shlyapnikov
 */
@ThreadSafe
public class NonBlockingPool<T> implements Pool<T> {

    private final ConcurrentLinkedQueue<T> pool;

    private final int poolCapacity;

    public NonBlockingPool(int poolCapacity, ResourceFactory<T> resourceFactory) {
        this.poolCapacity = poolCapacity;
        this.pool = new ConcurrentLinkedQueue<T>();
        for (int indx = 0; indx < poolCapacity; indx++) {
            this.pool.offer(resourceFactory.create());
        }
    }

    public T take() {
        T result = pool.poll();
        if (null == result) {
            throw new IllegalStateException("Pool ran dry: " + this);
        }
        return result;
    }

    public void release(T resource) {
        pool.offer(resource);
    }

    public String toString() {
        return toStringInternal();
    }

    private String toStringInternal() {
        return this.getClass().getSimpleName() + "{" + "poolCapacity=" + poolCapacity + ", free=" + pool.size() + '}';
    }

    public int getPoolCapacity() {
        return poolCapacity;
    }

    public int getFree() {
        return pool.size();
    }

    public static interface ResourceFactory<T> {

        T create();
    }
}
