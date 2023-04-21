package gnu.trove.decorator;

import gnu.trove.set.TLongSet;
import gnu.trove.iterator.TLongIterator;
import java.io.*;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Wrapper class to make a TLongSet conform to the <tt>java.util.Set</tt> API.
 * This class simply decorates an underlying TLongSet and translates the Object-based
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
 * @author Robert D. Eden
 * @author Jeff Randall
 */
public class TLongSetDecorator extends AbstractSet<Long> implements Set<Long>, Externalizable {

    static final long serialVersionUID = 1L;

    /** the wrapped primitive set */
    protected TLongSet _set;

    /**
     * FOR EXTERNALIZATION ONLY!!
     */
    public TLongSetDecorator() {
    }

    /**
     * Creates a wrapper that decorates the specified primitive set.
     *
     * @param set the <tt>TLongSet</tt> to wrap.
     */
    public TLongSetDecorator(TLongSet set) {
        super();
        this._set = set;
    }

    /**
     * Returns a reference to the set wrapped by this decorator.
     *
     * @return the wrapped <tt>TLongSet</tt> instance.
     */
    public TLongSet getSet() {
        return _set;
    }

    /**
     * Inserts a value into the set.
     *
     * @param value true if the set was modified by the insertion
     */
    public boolean add(Long value) {
        return value != null && _set.add(value.longValue());
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
                    if (val instanceof Long) {
                        long v = ((Long) val).longValue();
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
        return value instanceof Long && _set.remove(((Long) value).longValue());
    }

    /**
     * Creates an iterator over the values of the set.
     *
     * @return an iterator with support for removals in the underlying set
     */
    public Iterator<Long> iterator() {
        return new Iterator<Long>() {

            private final TLongIterator it = _set.iterator();

            public Long next() {
                return Long.valueOf(it.next());
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
        return this._set.size() == 0;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Long)) return false;
        return _set.contains(((Long) o).longValue());
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        _set = (TLongSet) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeObject(_set);
    }
}
