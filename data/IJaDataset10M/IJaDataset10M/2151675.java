package net.sf.alc.cfpj.iterators;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Iterators over a single object. This is a slightly more efficient
 * implementation than
 * <code>Collections.singletonList(object).listIterator() or Iterators.arrayIterator(object)</code>
 * .
 *
 * @author Alain Caron
 * @version 1.0
 */
class SingletonIterator<T> implements ListIterator<T> {

    private final T _object;

    private int _index = 0;

    SingletonIterator(T object) {
        _object = object;
    }

    /**
    * Returns <tt>true</tt> if <tt>next</tt> would return an element rather than
    * throwing an exception.
    *
    * @return <tt>true</tt> if <tt>next</tt> would return an element rather than
    *         throwing an exception.
    */
    @Override
    public boolean hasNext() {
        return _index == 0;
    }

    @Override
    public T next() {
        if (_index != 0) throw new NoSuchElementException();
        _index = 1;
        return _object;
    }

    @Override
    public boolean hasPrevious() {
        return _index == 1;
    }

    @Override
    public T previous() {
        if (_index != 1) throw new NoSuchElementException();
        _index = 0;
        return _object;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("SingletonIterator does not support the remove method");
    }

    @Override
    public int nextIndex() {
        return _index;
    }

    @Override
    public int previousIndex() {
        return _index - 1;
    }

    @Override
    public void add(T t) {
        throw new UnsupportedOperationException("singletonIterator does not support the add method.");
    }

    @Override
    public void set(T t) {
        throw new UnsupportedOperationException("singletonIterator does not support the set method.");
    }
}
