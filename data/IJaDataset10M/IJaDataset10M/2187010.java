package com.hazelcast.impl;

public interface DistributedRunnableAdapter<V> {

    V getResult();

    Runnable getRunnable();
}
