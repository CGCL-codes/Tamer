package com.senn.magic.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * {@link TreeSet} implementation of {@link ChainableSet}
 * 
 * @author Bart Thierens
 * 
 * <br>
 * 
 * Last modification: 04/05/2011
 *
 * @since 3.5
 */
public class ChainableTreeSet<E> extends TreeSet<E> implements ChainableSet<E> {

    private static final long serialVersionUID = -1264466035621291090L;

    public ChainableTreeSet() {
        super();
    }

    public ChainableTreeSet(SortedSet<E> s) {
        super(s);
    }

    public ChainableTreeSet(Collection<? extends E> c) {
        super(c);
    }

    public ChainableTreeSet(Comparator<? super E> comparator) {
        super(comparator);
    }

    public ChainableTreeSet<E> delete(Object o) {
        remove(o);
        return this;
    }

    public ChainableTreeSet<E> deleteAll(Collection<?> c) {
        removeAll(c);
        return this;
    }

    public ChainableTreeSet<E> empty() {
        clear();
        return this;
    }

    public ChainableTreeSet<E> keepAll(Collection<?> c) {
        retainAll(c);
        return this;
    }

    public ChainableTreeSet<E> put(E e) {
        add(e);
        return this;
    }

    public ChainableTreeSet<E> putAll(Collection<? extends E> c) {
        addAll(c);
        return this;
    }
}
