package net.sf.alc.cfpj.iterators;

import java.util.ListIterator;

/**
 * @author Alain Caron
 * @version 0.0 2000/01/26
 */
class ReadOnlyListIterator<E> extends ReadOnlyIterator<E> implements ListIterator<E> {

    ReadOnlyListIterator(ListIterator<E> iter) {
        super(iter);
    }

    @Override
    public boolean hasPrevious() {
        return ((ListIterator<E>) _iterator).hasPrevious();
    }

    @Override
    public E previous() {
        return ((ListIterator<E>) _iterator).previous();
    }

    @Override
    public int nextIndex() {
        return ((ListIterator<E>) _iterator).nextIndex();
    }

    @Override
    public int previousIndex() {
        return ((ListIterator<E>) _iterator).previousIndex();
    }

    @Override
    public void set(E o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(E o) {
        throw new UnsupportedOperationException();
    }
}
