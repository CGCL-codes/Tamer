package net.java.nioserver.utils;

/**
 * @author Leonid Shlyapnikov
 */
public interface Pool<T> {

    T take() throws InterruptedException;

    void release(T resource);

    int getPoolCapacity();

    int getFree();
}
