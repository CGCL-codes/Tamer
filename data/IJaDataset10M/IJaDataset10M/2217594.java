package com.db4o.foundation;

/**
 * @exclude
 */
public interface Predicate4<T> {

    public boolean match(T candidate);
}
