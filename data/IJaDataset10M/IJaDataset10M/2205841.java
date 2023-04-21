package com.dukesoftware.utils.data;

public interface QueueOperator<T> {

    void put(T elem);

    T get() throws InterruptedException;

    int elemNum();

    int size();

    void clear();
}
