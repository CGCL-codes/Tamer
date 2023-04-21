package net.sf.jga.algorithms;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import net.sf.jga.fn.UnaryFunctor;
import net.sf.jga.util.ArrayUtils;
import net.sf.jga.util.EmptyIterator;
import net.sf.jga.util.Iterable;

/**
 * Algorithms that iterate over variable-length sets of inputs, ie, arrays, collections, or 
 * iterations, returning the combined contents of all inputs.  For the common case, iterating 
 * over the combined contents of two inputs, the Merge class provides a type-safe wrapper
 * around these algorithms.  These algorithms are more flexible than the append forms of Merge,
 * at the cost of being less type-safe.
 * <p>
 * NOTE: Use of these algorithms <b>will</b> result in type-safety warnings from the compiler: 
 * specifically "A generic array of ... is created for a varargs parameter".  This is pretty much 
 * unavoidable given the mismatch in java between generics and arrays and given that variable length 
 * argument lists are converted to arrays by the compiler. 
 * <p>
 * NOTE: Unlike most other classes in this package, Append does not support the form that puts the 
 * result of the algorithm into a collection passed as the last argument.  The CollectionUtils 
 * class contains an append method that handles one input, and the Merge class contains an append
 * method that handles two inputs.  A varargs form of append to a given collection would be 
 * ambiguous in this class if it had the same name, and it would be ambiguous with the append in
 * either CollectionUtils or Merge (or both) if all were statically imported.  If you need to append
 * three or more inputs to a given collection, the workaround is to call the appropriate method in
 * this class to reduce the inputs to one, then use Collection.addAll (or CollectionUtils.append). 
 * <p>
 * Copyright &copy; 2008  David A. Hall
 */
public class Append {

    @SuppressWarnings("serial")
    private static class Iterablerator<T> extends UnaryFunctor<T[], Iterable<? extends T>> {

        public Iterable<T> fn(T[] input) {
            return ArrayUtils.iterable(input);
        }
    }

    ;

    /**
     * Returns all elements of each array
     */
    public static <T> Iterable<T> append(T[] ts) {
        return ArrayUtils.iterable(ts);
    }

    /**
     * Returns all elements of each array
     */
    public static <T> Iterable<T> append(T[]... ts) {
        return new AppendIterable<T>(ts);
    }

    /**
     * Returns an Iterable that 'contains' all of the elements of the input(s)
     */
    public static <T> Iterable<? extends T> append(Collection<? extends T>... inputs) {
        return new AppendIterable<T>(inputs);
    }

    /**
     * Returns an Iterable that 'contains' all of the elements of the input(s)
     */
    public static <T> Iterable<? extends T> append(Iterable<? extends T>... inputs) {
        return new AppendIterable<T>(inputs);
    }

    /**
     * Returns an Iterator that will return elements from all of the iterators
     */
    public static <T> Iterator<? extends T> append(Iterator<? extends T>... input) {
        return new AppendIterator<T>(input);
    }

    /**
     * Produces iterators that return the contents of a two-dimensional data structure
     */
    @SuppressWarnings("serial")
    public static class AppendIterable<T> implements Iterable<T> {

        private Iterable<? extends Iterable<? extends T>> base;

        private UnaryFunctor<Iterable<? extends T>, Iterator<? extends T>> iteratorator = new UnaryFunctor<Iterable<? extends T>, Iterator<? extends T>>() {

            public Iterator<? extends T> fn(Iterable<? extends T> input) {
                return input.iterator();
            }
        };

        /**
         * Builds a AppendIterable for the given set of inputs
         * @param input
         */
        public AppendIterable(Iterable<? extends T>... input) {
            this(ArrayUtils.iterable(input));
        }

        /**
         * Builds a AppendIterable for the given set of inputs
         * @param input
         */
        public AppendIterable(Collection<? extends T>... input) {
            this(ArrayUtils.iterable(input));
        }

        /**
         * Builds a AppendIterable for the given set of inputs
         */
        public AppendIterable(Iterable<? extends Iterable<? extends T>> input) {
            base = input;
        }

        /**
         * Builds a AppendIterable for the given set of inputs
         */
        public AppendIterable(T[]... input) {
            this.base = Transform.transform(input, new Iterablerator<T>());
        }

        @SuppressWarnings("serial")
        public Iterator<T> iterator() {
            return new AppendIterator<T>(Transform.transform(base.iterator(), iteratorator));
        }
    }

    /**
     * Iterator that returns the contents of a two-dimensional datastructure.
     */
    public static class AppendIterator<T> implements Iterator<T> {

        private Iterator<? extends Iterator<? extends T>> _base;

        private Iterator<? extends T> _crnt = new EmptyIterator<T>();

        private Boolean _hasNext = null;

        /**
         * Builds an Append.AppendIterator for the given base iterators
         */
        public AppendIterator(Iterator<? extends T>... iters) {
            this(ArrayUtils.iterate(iters));
        }

        /**
         * Builds an Append.AppendIterator for the given base iterators
         */
        public AppendIterator(Iterator<? extends Iterator<? extends T>> iters) {
            _base = iters;
        }

        public boolean hasNext() {
            if (_hasNext == null) advance();
            return _hasNext;
        }

        public T next() {
            if (_hasNext == null) advance();
            _hasNext = null;
            return _crnt.next();
        }

        public void remove() {
            _crnt.remove();
        }

        private void advance() {
            if (_hasNext != null) return;
            if (_crnt.hasNext()) {
                _hasNext = Boolean.TRUE;
                return;
            }
            while (!_crnt.hasNext() && _base.hasNext()) {
                _crnt = _base.next();
            }
            _hasNext = _crnt.hasNext();
        }
    }
}
