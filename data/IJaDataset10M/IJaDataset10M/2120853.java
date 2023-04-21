package com.hazelcast.core;

public interface ICollection<E> extends Instance {

    String getName();

    void addItemListener(ItemListener<E> listener, boolean includeValue);

    void removeItemListener(ItemListener<E> listener);
}
