package org.skuebeck.ooc.examples.dataflow;

import org.skuebeck.ooc.annotations.Blocking;
import org.skuebeck.ooc.annotations.Concurrent;
import org.skuebeck.ooc.annotations.ReleaseCondition;

@Concurrent
public interface Variable<T> {

    public abstract void set(T content);

    @ReleaseCondition
    public abstract boolean isset();

    @Blocking
    public abstract T get();
}
