package net.sf.milky.collections;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import net.sf.milky.functional.UnaryFunction;
import net.sf.milky.iterators.FilterIterator;
import net.sf.milky.iterators.IteratorUtilities;

public class SortedSetView<E> extends AbstractSet<E> implements SortedSet<E> {

    SortedSet<E> set;

    E fromElement;

    E toElement;

    public SortedSetView(SortedSet<E> s, E fromElement, E toElement) {
        if (s == null) {
            throw new NullPointerException();
        }
        set = s;
        this.fromElement = fromElement;
        this.toElement = toElement;
    }

    public boolean add(E element) {
        if (!canContain(element)) {
            throw new IllegalArgumentException();
        }
        return set.add(element);
    }

    public void clear() {
        try {
            IteratorUtilities.removeAll(iterator());
        } catch (UnsupportedOperationException e) {
            set.removeAll(new ArrayList(this));
        }
    }

    public boolean contains(Object o) {
        return canContain((E) o) && set.contains(o);
    }

    public Iterator<E> iterator() {
        return new FilterIterator(set.iterator(), new UnaryFunction<Boolean, E>() {

            public Boolean call(E element) {
                return canContain(element);
            }
        });
    }

    public boolean remove(Object o) {
        return canContain((E) o) && set.remove(o);
    }

    public int size() {
        return IteratorUtilities.size(iterator());
    }

    public Comparator<? super E> comparator() {
        return set.comparator();
    }

    public E first() {
        return IteratorUtilities.first(iterator());
    }

    public SortedSet<E> headSet(E toElement) {
        return subSetImpl(null, toElement);
    }

    public E last() {
        return IteratorUtilities.last(iterator());
    }

    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSetImpl(fromElement, toElement);
    }

    public SortedSet<E> tailSet(E fromElement) {
        return subSetImpl(fromElement, null);
    }

    SortedSet<E> subSetImpl(E fromElement, E toElement) {
        return new SortedSetView(this, fromElement, toElement);
    }

    int compare(E first, E second) {
        final Comparator<? super E> comparator = set.comparator();
        return (comparator != null) ? comparator.compare(first, second) : ((Comparable<E>) first).compareTo(second);
    }

    boolean canContain(E element) {
        return (fromElement == null || compare(element, fromElement) >= 0) || (toElement == null || compare(element, toElement) < 0);
    }
}
