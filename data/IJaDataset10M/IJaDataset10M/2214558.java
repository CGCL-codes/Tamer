package org.chessworks.common.javatools.collections;

import java.util.Iterator;

final class ReadOnlyIterable<T> implements Iterable<T> {

    private final Iterable<T> list;

    public ReadOnlyIterable(Iterable<T> list) {
        this.list = list;
    }

    public Iterator<T> iterator() {
        Iterator<T> i = list.iterator();
        Iterator<T> result = CollectionHelper.readOnly(i);
        return result;
    }
}
