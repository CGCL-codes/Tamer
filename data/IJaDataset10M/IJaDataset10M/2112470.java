package com.ibm.wala.util;

import com.ibm.wala.util.collections.ArrayIterator;

/**
 * Iterator that only returns non-null elements of the array 
 * 
 * hasNext() return
 * true when there is a non-null element, false otherwise 
 * 
 * next() returns the
 * current element and advances the counter up to the next non-null element or
 * beyond the limit of the array
 * 
 * @author Stephen Fink
 * @author Eran Yahav
 * 
 */
public class ArrayNonNullIterator<T> extends ArrayIterator<T> {

    public ArrayNonNullIterator(T[] elts) {
        super(elts, 0);
    }

    public ArrayNonNullIterator(T[] elts, int start) {
        super(elts, start);
    }

    @Override
    public boolean hasNext() {
        return _cnt < _elts.length && _elts[_cnt] != null;
    }

    @Override
    public T next() {
        T result = _elts[_cnt];
        do {
            _cnt++;
        } while (_cnt < _elts.length && _elts[_cnt] == null);
        return result;
    }
}
