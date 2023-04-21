package edu.rice.cs.plt.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.rice.cs.plt.lambda.Predicate;
import edu.rice.cs.plt.object.Composite;
import edu.rice.cs.plt.object.ObjectUtil;
import edu.rice.cs.plt.tuple.Option;
import edu.rice.cs.plt.tuple.OptionUnwrapException;

/**
 * An Iterator that only returns the values in another Iterator ({@code i}) for which some
 * predicate ({@code p}) holds.  Does not support {@link #remove()}.
 */
public class FilteredIterator<T> extends ReadOnlyIterator<T> implements Composite {

    private final Predicate<? super T> _p;

    private final Iterator<? extends T> _i;

    private Option<T> _lookahead;

    public FilteredIterator(Iterator<? extends T> i, Predicate<? super T> p) {
        _p = p;
        _i = i;
        advanceLookahead();
    }

    public int compositeHeight() {
        return ObjectUtil.compositeHeight(_i) + 1;
    }

    public int compositeSize() {
        return ObjectUtil.compositeSize(_i) + 1;
    }

    public boolean hasNext() {
        return _lookahead.isSome();
    }

    public T next() {
        try {
            T result = _lookahead.unwrap();
            advanceLookahead();
            return result;
        } catch (OptionUnwrapException e) {
            throw new NoSuchElementException();
        }
    }

    /**
   * Finds the next value in {@code _i} for which {@code _p} holds.
   * Ignores the previous value of {@code _lookahead}.  If a value is
   * found, sets {@code _lookahead} to that value; otherwise, sets it to 
   * {@code Option.none()}.
   */
    private void advanceLookahead() {
        _lookahead = Option.none();
        while (_i.hasNext() && _lookahead.isNone()) {
            T next = _i.next();
            if (_p.contains(next)) {
                _lookahead = Option.some(next);
            }
        }
    }

    /** Call the constructor (allows {@code T} to be inferred) */
    public static <T> FilteredIterator<T> make(Iterator<? extends T> i, Predicate<? super T> p) {
        return new FilteredIterator<T>(i, p);
    }
}
