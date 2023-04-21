package net.sf.molae.pipe.sorted;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Utility methods for {@link Comparator Comparators}.
 * @version 2.0
 * @author Ralph Wagner
 */
public final class ComparatorUtil {

    private ComparatorUtil() {
    }

    /**
     * Checks the specified objects for equality. 
     * The objects are also considered equal if they are both 
     * <code>null</code>.
     * @param o1 first object to compare
     * @param o2 second object to compare
     * @return <code>true</code> iff both objects are <code>null</code> or
     * both objects are equal.
     */
    public static boolean areEqual(Object o1, Object o2) {
        return (o1 == null ? (o2 == null) : (o1.equals(o2)));
    }

    /**
     * Compares two objects using the specified comparator.
     * @param o1 first object to compare
     * @param o2 second object to compare
     * @param c comparator to use for comparison
     * or <code>null</code> if natural ordering is used
     * @return a negative integer, zero, or a positive integer as the
     *  first argument is less than, equal to, or greater than the second.
     * @throws ClassCastException if <code>o1</code> and <code>o2</code>
     *  cannot be compared to one another using
     *  the comparator (or, if it is <code>null</code>,
     *  using natural ordering).
     * @throws NullPointerException if <code>o1</code> or <code>o2</code>
     *  is <tt>null</tt> and natural order is used,
     *  or if the comparator does not tolerate <tt>null</tt> elements.
     */
    @SuppressWarnings("unchecked")
    public static <T> int compare(T o1, T o2, Comparator<T> c) {
        int reply = 0;
        if (c != null) {
            reply = c.compare(o1, o2);
        } else {
            reply = ((Comparable) o1).compareTo(o2);
        }
        return reply;
    }

    /**
     * Compares the two specified objects and returns the greater one.
     * @param o1 first object to compare
     * @param o2 second object to compare
     * @param comparator comparator to use for comparison
     * or <code>null</code> if natural ordering is used
     * @return The maximum of <code>o1</code> and <code>o2</code>.
     *  If both are equal, <code>o1</code> is returned.
     * @throws ClassCastException if <code>o1</code> and <code>o2</code>
     *  cannot be compared to one another using
     *  the comparator (or, if it is <code>null</code>,
     *  using natural ordering).
     * @throws NullPointerException if <code>o1</code> or <code>o2</code>
     *  is <tt>null</tt> and natural order is used,
     *  or if the comparator does not tolerate <tt>null</tt> elements.
     */
    public static <T> T max(T o1, T o2, Comparator<? super T> comparator) {
        return (compare(o1, o2, comparator) >= 0 ? o1 : o2);
    }

    /**
     * Compares the two specified objects and returns the smaller one.
     * @param o1 first object to compare
     * @param o2 second object to compare
     * @param comparator comparator to use for comparison
     * or <code>null</code> if natural ordering is used
     * @return The minimum of <code>o1</code> and <code>o2</code>.
     *         If both are equal, <code>o1</code> is returned.
     * @throws ClassCastException if <code>o1</code> and <code>o2</code>
     *  cannot be compared to one another using
     *  the comparator (or, if it is <code>null</code>,
     *  using natural ordering).
     * @throws NullPointerException if <code>o1</code> or <code>o2</code>
     *  is <tt>null</tt> and natural order is used,
     *  or if the comparator does not tolerate <tt>null</tt> elements.
     */
    public static <T> T min(T o1, T o2, Comparator<? super T> comparator) {
        return (compare(o1, o2, comparator) <= 0 ? o1 : o2);
    }

    /**
     * Compares the two specified objects and returns the greater one.
     * @param o1 first object to compare
     * @param o2 second object to compare
     * @return The maximum of <code>o1</code> and <code>o2</code>.
     *  If both are equal, <code>o1</code> is returned.
     * @throws ClassCastException if <code>o1</code> and <code>o2</code>
     *  cannot be compared to one another.
     * @throws NullPointerException if <code>o1</code> or <code>o2</code>
     *  is <tt>null</tt>.
     */
    public static <T> T max(T o1, T o2) {
        return max(o1, o2, null);
    }

    /**
     * Compares the two specified objects and returns the smaller one.
     * @param o1 first object to compare
     * @param o2 second object to compare
     * @return The minimum of <code>o1</code> and <code>o2</code>.
     *  If both are equal, <code>o1</code> is returned.
     * @throws ClassCastException if <code>o1</code> and <code>o2</code>
     *  cannot be compared to one another.
     * @throws NullPointerException if <code>o1</code> or <code>o2</code>
     *  is <tt>null</tt>.
     */
    public static <T> T min(T o1, T o2) {
        return min(o1, o2, null);
    }

    /**
     * Checks if the specified iterator is in sorted order
     * of the specified comparator.
     * @param it iterator to be examined
     * @param comparator comparator to use for comparison
     * or <code>null</code> if natural ordering is used
     * @return <code>true</code> if the specified iterator is in sorted order
     * of the specified comparator.
     * @throws ClassCastException if the elements of the iterator cannont
     *  be compared to one another using.
     *  the comparator (or, if it is <code>null</code>,
     *  using natural ordering).
     * @throws NullPointerException the iterator contains a <code>null</code>
     * element and natural order is used,
     *  or if the comparator does not tolerate <tt>null</tt> elements.
     */
    public static <T> boolean isSorted(Iterator<T> it, Comparator<? super T> comparator) {
        boolean reply = true;
        if (it.hasNext()) {
            T last = it.next();
            while (it.hasNext()) {
                T next = it.next();
                reply &= (compare(last, next, comparator) <= 0);
                last = next;
            }
        }
        return reply;
    }
}
