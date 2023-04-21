package gnu.trove.decorator;

import gnu.trove.TDoubleHashSet;
import gnu.trove.TDoubleIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Wrapper class to make a TDoubleHashSet conform to the <tt>java.util.Set</tt> API.
 * This class simply decorates an underlying TDoubleHashSet and translates the Object-based
 * APIs into their Trove primitive analogs.
 * <p/>
 * <p/>
 * Note that wrapping and unwrapping primitive values is extremely inefficient.  If
 * possible, users of this class should override the appropriate methods in this class
 * and use a table of canonical values.
 * </p>
 * <p/>
 * Created: Tue Sep 24 22:08:17 PDT 2002
 *
 * @author Eric D. Friedman
 */
public class TDoubleHashSetDecorator extends AbstractSet<Double> implements Set<Double> {

    /** the wrapped primitive set */
    protected TDoubleHashSet _set;

    /**
     * Creates a wrapper that decorates the specified primitive set.
     */
    public TDoubleHashSetDecorator(TDoubleHashSet set) {
        super();
        this._set = set;
    }

    /**
     * Returns a reference to the set wrapped by this decorator.
     */
    public TDoubleHashSet getSet() {
        return _set;
    }

    /**
     * Clones the underlying trove collection and returns the clone wrapped in a new
     * decorator instance.  This is a shallow clone except where primitives are
     * concerned.
     *
     * @return a copy of the receiver
     */
    public TDoubleHashSetDecorator clone() {
        try {
            TDoubleHashSetDecorator copy = (TDoubleHashSetDecorator) super.clone();
            copy._set = (TDoubleHashSet) _set.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    /**
     * Inserts a value into the set.
     *
     * @param value true if the set was modified by the insertion
     */
    public boolean add(Double value) {
        return _set.add(unwrap(value));
    }

    /**
     * Compares this set with another set for equality of their stored
     * entries.
     *
     * @param other an <code>Object</code> value
     * @return true if the sets are identical
     */
    public boolean equals(Object other) {
        if (_set.equals(other)) {
            return true;
        } else if (other instanceof Set) {
            Set that = (Set) other;
            if (that.size() != _set.size()) {
                return false;
            } else {
                Iterator it = that.iterator();
                for (int i = that.size(); i-- > 0; ) {
                    Object val = it.next();
                    if (val instanceof Double) {
                        double v = unwrap(val);
                        if (_set.contains(v)) {
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Empties the set.
     */
    public void clear() {
        this._set.clear();
    }

    /**
     * Deletes a value from the set.
     *
     * @param value an <code>Object</code> value
     * @return true if the set was modified
     */
    public boolean remove(Object value) {
        return _set.remove(unwrap(value));
    }

    /**
     * Creates an iterator over the values of the set.
     *
     * @return an iterator with support for removals in the underlying set
     */
    public Iterator<Double> iterator() {
        return new Iterator<Double>() {

            private final TDoubleIterator it = _set.iterator();

            public Double next() {
                return wrap(it.next());
            }

            public boolean hasNext() {
                return it.hasNext();
            }

            public void remove() {
                it.remove();
            }
        };
    }

    /**
     * Returns the number of entries in the set.
     *
     * @return the set's size.
     */
    public int size() {
        return this._set.size();
    }

    /**
     * Indicates whether set has any entries.
     *
     * @return true if the set is empty
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Wraps a value
     *
     * @param k value in the underlying set
     * @return an Object representation of the value
     */
    protected Double wrap(double k) {
        return new Double(k);
    }

    /**
     * Unwraps a value
     *
     * @param value wrapped value
     * @return an unwrapped representation of the value
     */
    protected double unwrap(Object value) {
        return ((Double) value).doubleValue();
    }
}
