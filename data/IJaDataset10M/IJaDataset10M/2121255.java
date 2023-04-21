package de.dhke.projects.cutil.collections.immutable;

import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @param <Collection<T>>
 * @author Peter Wullinger <peter.wullinger@uni-bamberg.de>
 */
public class ImmutableCollection<T> implements Collection<T> {

    private final Collection<T> _backCollection;

    private final Transformer<T, T> _valueTransformer;

    protected ImmutableCollection(final Collection<T> backColl, final Transformer<T, T> valueTransformer) {
        _backCollection = backColl;
        _valueTransformer = valueTransformer;
    }

    public static <T> ImmutableCollection<T> decorate(final Collection<T> backColl) {
        return new ImmutableCollection<T>(backColl, null);
    }

    public static <T> ImmutableCollection<T> decorate(final Collection<T> backColl, final Transformer<T, T> valueTransformer) {
        return new ImmutableCollection<T>(backColl, valueTransformer);
    }

    public int size() {
        return _backCollection.size();
    }

    public boolean isEmpty() {
        return _backCollection.isEmpty();
    }

    public boolean contains(Object o) {
        return _backCollection.contains(o);
    }

    public Iterator<T> iterator() {
        return ImmutableIterator.decorate(_backCollection.iterator(), _valueTransformer);
    }

    public Object[] toArray() {
        return _backCollection.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return _backCollection.toArray(a);
    }

    public boolean add(T e) {
        throw new UnsupportedOperationException("Cannot modify ImmutableCollectionSet.");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Cannot modify ImmutableCollectionSet.");
    }

    public boolean containsAll(Collection<?> c) {
        return _backCollection.containsAll(c);
    }

    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("Cannot modify ImmutableCollectionSet.");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Cannot modify ImmutableCollectionSet.");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Cannot modify ImmutableCollectionSet.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Cannot modify ImmutableCollectionSet.");
    }
}
