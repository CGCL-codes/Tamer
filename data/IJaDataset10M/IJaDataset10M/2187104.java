package com.tencent.tendon.convert;

/**
 *
 * @author nbzhang
 */
public interface ConvertAttribute<T, V> {

    public String field();

    public V get(T obj);

    public void set(T obj, V value);
}
