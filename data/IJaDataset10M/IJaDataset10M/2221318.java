package jp.go.aist.six.util.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A constraint on the number of objects returned by a search query.
 * The Limit consists of one or two integer properties:
 * "count" and "offset".
 *
 * <ul>
 *   <li><b>count</b>:
 *   the maximum number of objects to return.</li>
 *   <li><b>offset</b> (option):
 *   the offset of the first object to return.
 *   The default value is 0.
 *   </li>
 * </ul>
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: Limit.java 300 2011-03-02 05:45:46Z nakamura5akihito $
 */
public class Limit implements Serializable {

    private static final long serialVersionUID = 3659280320264336923L;

    /**
     * The maximum number of result objects.
     */
    private int _count = -1;

    /**
     * The offset of the first object (option).
     */
    private int _offset = 0;

    /**
     * Default constructor.
     */
    public Limit() {
    }

    /**
     * Constructs a Limit with the specified count.
     *
     * @param   count
     *  the maximum number of result objects.
     */
    public Limit(final int count) {
        this(count, 0);
    }

    /**
     * Constructs a Limit with the specified count and offset.
     *
     * @param   count
     *  the maximum number of result objects.
     * @param   offset
     *  the offset of the first object.
     */
    public Limit(final int count, final int offset) {
        setCount(count);
        setOffset(offset);
    }

    /**
     * Sets the maximum number of result objects.
     *
     * @param   count
     *  the maximum number of result objects.
     * @throws  IllegalArgumentException
     *  if the specified count is negative.
     */
    public void setCount(final int count) {
        if (count < 0) {
            throw new IllegalArgumentException("negative count");
        }
        _count = count;
    }

    /**
     * Returns the maximum number of result objects.
     *
     * @return
     *  the maximum number of result objects.
     */
    public int getCount() {
        return _count;
    }

    /**
     * Sets the offset of the first object.
     *
     * @param   offset
     *  the offset of the first object.
     * @throws  IllegalArgumentException
     *  if the specified offset is negative.
     */
    public void setOffset(final int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("negative offset");
        }
        _offset = offset;
    }

    /**
     * Returns the offset of the first object.
     *
     * @return
     *  the offset of the first object.
     */
    public int getOffset() {
        return _offset;
    }

    /**
     * Extracts a range from the specified object list
     * according to this LIMIT constraint.
     *
     * @param   objects
     *  the object list.
     * @return
     *  the result range (not null).
     */
    public <T> List<T> apply(final List<T> objects) {
        List<T> results = new ArrayList<T>();
        if (objects == null || objects.size() == 0) {
            return results;
        }
        int size = objects.size();
        int fromIndex = getOffset();
        int toIndex = fromIndex + getCount();
        if (size < toIndex) {
            toIndex = size;
        }
        if (fromIndex >= size || toIndex == fromIndex) {
        } else {
            results.addAll(objects.subList(fromIndex, toIndex));
        }
        return results;
    }

    /**
     * Determines whether another object is equal to this Limit.
     * The result is true if and only if the argument is not null
     * and is a Limit object that has the same count and offset.
     *
     * @param   obj
     *  the object to test for equality with this Limit.
     * @return
     *  true if the given object equals this one;
     *  false otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Limit)) {
            return false;
        }
        final Limit other = (Limit) obj;
        if (getCount() == other.getCount() && getOffset() == other.getOffset()) {
            return true;
        }
        return false;
    }

    /**
     * Computes the hash code for this Limit.
     *
     * @return
     *  a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 37;
        int result = 17;
        result = prime * result + getCount();
        result = prime * result + getOffset();
        return result;
    }

    /**
     * Returns a string representation of this Limit.
     * This method is intended to be used only for debugging purposes.
     * The content and format of the returned string might not
     * conform to any query language syntax.
     *
     * @return
     *  a string representation of this Limit.
     */
    @Override
    public String toString() {
        return "Limit[count=" + _count + ",offset=" + _offset + "]";
    }
}
