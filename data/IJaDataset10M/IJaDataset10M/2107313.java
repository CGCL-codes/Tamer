package de.javagimmicks.collections.transformer;

import java.util.Iterator;
import java.util.NavigableSet;

class TransformingNavigableSet<F, T> extends TransformingSortedSet<F, T> implements NavigableSet<T> {

    /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
    @Deprecated
    public TransformingNavigableSet(NavigableSet<F> set, Transformer<F, T> transformer) {
        super(set, transformer);
    }

    public T ceiling(T e) {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> descendingIterator() {
        return TransformerUtils.decorate(getNavigableSet().descendingIterator(), getTransformer());
    }

    public NavigableSet<T> descendingSet() {
        return TransformerUtils.decorate(getNavigableSet().descendingSet(), getTransformer());
    }

    public T floor(T e) {
        throw new UnsupportedOperationException();
    }

    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    public T higher(T e) {
        throw new UnsupportedOperationException();
    }

    public T lower(T e) {
        throw new UnsupportedOperationException();
    }

    public T pollFirst() {
        F first = getNavigableSet().pollFirst();
        return first != null ? transform(first) : null;
    }

    public T pollLast() {
        F last = getNavigableSet().pollLast();
        return last != null ? transform(last) : null;
    }

    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        throw new UnsupportedOperationException();
    }

    protected NavigableSet<F> getNavigableSet() {
        return (NavigableSet<F>) _internalSet;
    }
}
