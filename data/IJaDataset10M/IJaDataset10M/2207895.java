package org.chessworks.common.javatools.collections;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import org.chessworks.common.javatools.Filter;

final class FilteredSet<T> extends AbstractSet<T> {

    private final Set<?> set;

    private final Filter<T> filter;

    public FilteredSet(Set<?> set, Filter<T> filter) {
        this.set = set;
        this.filter = filter;
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<?> i = set.iterator();
        Iterator<T> result = CollectionHelper.filter(i, filter);
        return result;
    }

    @Override
    public int size() {
        int size = 0;
        for (Object o : set) {
            T t = filter.filter(o);
            if (t != null) size++;
        }
        return size;
    }
}
