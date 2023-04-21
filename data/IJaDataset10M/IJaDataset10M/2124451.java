package eu.pisolutions.collections.iterator;

import java.util.ListIterator;

/**
 * Unmodifiable {@link java.util.ListIterator}.
 *
 * @author Laurent Pireyn
 */
public final class UnmodifiableListIterator<T> extends ListIteratorWrapper<T> {

    public UnmodifiableListIterator(ListIterator<T> iterator) {
        super(iterator);
    }

    @Override
    public void set(T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
