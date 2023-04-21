package com.myjavatools.lib.foundation;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

/**
 * FunctionValueList is a helper class that represents List of values
 * of a Function on a specific List
 *
 * Note that this class does not take any additional memory; lazy evaluation uses
 * only the keyset provided
 *
 * @version 5.0, 11/24/04
 *
 * @see Function
 * @see FunctionalMap
 * @see Maps
 * @see java.util.Map
 * @since 5.0
 */
class FunctionValueList<X, Y> extends AbstractList<Y> {

    private Function<X, Y> function;

    private List<X> domain;

    private static <T> boolean equal(final T a, final T b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
   * creates a new FunctionValueList from a function and a collection
   * @param function Function&lt;X,Y>
   * @param domain List&lt;X>
   */
    public FunctionValueList(final Function<X, Y> function, final List<X> domain) {
        this.function = function;
        this.domain = domain;
    }

    /**
   * Returns <tt>true</tt> if the function maps one or more keys from domain
   * to the specified value.
   *
   * @param value value whose presence in this map is to be tested.
   * @return <tt>true</tt> if the function's value on one or more keys is the specified
   *   value.
   * Note that this method is extremely inefficient - as probably any implementation
   * of contains() on an unstructured, non-indexed collection.
   */
    @Override
    public boolean contains(final Object value) {
        for (final X key : domain) {
            if (equal(function.apply(key), value)) {
                return true;
            }
        }
        return false;
    }

    /**
   * Returns the number of elements in this collection.
   *
   * @return the number of elements in this collection.
   */
    @Override
    public int size() {
        return domain.size();
    }

    /**
   * returns an Iterator that scans over the values of function on domain
   * @return Iterator
   */
    @Override
    public Iterator<Y> iterator() {
        return function.apply(domain.iterator());
    }

    /**
   * Returns the element at the specified position in this list.
   *
   * @param index index of element to return.
   * @return the element at the specified position in this list.
   *
   * @throws IndexOutOfBoundsException if the index is out of range (index
   * 		  &lt; 0 || index &gt;= size()).
   */
    @Override
    public Y get(final int index) {
        return function.apply(domain.get(index));
    }
}
