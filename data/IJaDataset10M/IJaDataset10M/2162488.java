package org.mariella.rcp.util;

public interface ListObserver<E> {

    void listModified();

    void elementRemoved(E element);

    void elementAdded(int index, E element);
}
