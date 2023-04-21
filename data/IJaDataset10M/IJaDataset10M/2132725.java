package com.hazelcast.core;

import java.util.EventListener;

public interface ItemListener<E> extends EventListener {

    void itemAdded(E item);

    void itemRemoved(E item);
}
