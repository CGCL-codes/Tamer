package net.sf.joafip.java.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;

@NotStorableClass
@DoNotTransform
public class SetVersusList<E> implements List<E>, Set<E> {

    private final Set<E> set;

    public SetVersusList(final Set<E> set) {
        super();
        this.set = set;
    }

    public Set<E> getSet() {
        return set;
    }

    public boolean add(final E element) {
        return set.add(element);
    }

    public void add(final int index, final E element) {
        throw new NoTestableException();
    }

    public boolean addAll(final Collection<? extends E> collection) {
        return set.addAll(collection);
    }

    public boolean addAll(final int index, final Collection<? extends E> collection) {
        throw new NoTestableException();
    }

    public void clear() {
        set.clear();
    }

    public boolean contains(final Object object) {
        return set.contains(object);
    }

    public boolean containsAll(final Collection<?> collection) {
        return set.containsAll(collection);
    }

    public E get(final int index) {
        throw new NoTestableException();
    }

    public int indexOf(final Object object) {
        throw new NoTestableException();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public Iterator<E> iterator() {
        return set.iterator();
    }

    public int lastIndexOf(final Object object) {
        throw new NoTestableException();
    }

    public ListIterator<E> listIterator() {
        throw new NoTestableException();
    }

    public ListIterator<E> listIterator(final int index) {
        throw new NoTestableException();
    }

    public E remove(final int index) {
        throw new NoTestableException();
    }

    public boolean remove(final Object object) {
        return set.remove(object);
    }

    public boolean removeAll(final Collection<?> collection) {
        return set.removeAll(collection);
    }

    public boolean retainAll(final Collection<?> collection) {
        return set.retainAll(collection);
    }

    public E set(final int index, final E element) {
        throw new NoTestableException();
    }

    public int size() {
        return set.size();
    }

    public List<E> subList(final int fromIndex, final int toIndex) {
        throw new NoTestableException();
    }

    public Object[] toArray() {
        return set.toArray();
    }

    public <T> T[] toArray(final T[] aTs) {
        return set.toArray(aTs);
    }

    @SuppressWarnings("unchecked")
    public boolean equals(final Object object) {
        final boolean equals;
        if (object == null) {
            equals = false;
        } else if (object instanceof SetVersusList) {
            equals = set.equals(((SetVersusList<E>) object).getSet());
        } else {
            equals = false;
        }
        return equals;
    }

    public int hashCode() {
        return set.hashCode();
    }
}
