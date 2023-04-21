package net.sf.leechget.filter;

import net.sf.leechget.filter.filters.AndObjectFilter;
import net.sf.leechget.filter.filters.EqualObjectFilter;
import net.sf.leechget.filter.filters.EqualsObjectFilter;
import net.sf.leechget.filter.filters.LessThanObjectFilter;
import net.sf.leechget.filter.filters.MoreThanObjectFilter;
import net.sf.leechget.filter.filters.NotObjectFilter;
import net.sf.leechget.filter.filters.OrObjectFilter;

/**
 * Commonly used filters
 * 
 * @author Rogiel Josias Sulzbach
 */
public final class Filters {

    public static final <T> ObjectFilter<T> and(final ObjectFilter<T>... filters) {
        return new AndObjectFilter<T>(filters);
    }

    public static final <T> ObjectFilter<T> or(final ObjectFilter<T>... filters) {
        return new OrObjectFilter<T>(filters);
    }

    public static final <T> ObjectFilter<T> not(final ObjectFilter<T> filter) {
        return new NotObjectFilter<T>(filter);
    }

    public static final <T extends Comparable<T>> ObjectFilter<T> eqComparable(final T object) {
        return new MoreThanObjectFilter<T>(object);
    }

    public static final <T extends Comparable<T>> ObjectFilter<T> moreThan(final T object) {
        return new MoreThanObjectFilter<T>(object);
    }

    @SuppressWarnings("unchecked")
    public static final <T extends Comparable<T>> ObjectFilter<T> moreEqThan(final T object) {
        return or(moreThan(object), eqComparable(object));
    }

    public static final <T extends Comparable<T>> ObjectFilter<T> lessThan(final T object) {
        return new LessThanObjectFilter<T>(object);
    }

    @SuppressWarnings("unchecked")
    public static final <T extends Comparable<T>> ObjectFilter<T> lessEqThan(final T object) {
        return or(lessThan(object), eqComparable(object));
    }

    /**
	 * Compare two objects using <tt>==</tt> operator
	 * 
	 * @param <T>
	 *            the type of the object
	 * @param object
	 *            the object to compare to
	 * @return the filter
	 */
    public static final <T> ObjectFilter<T> eq(final T object) {
        return new EqualObjectFilter<T>(object);
    }

    /**
	 * Compare two objects using {@link T#equals(Object)} method
	 * 
	 * @param <T>
	 *            the type of the object
	 * @param object
	 *            the object to compare to
	 * @return the filter
	 */
    public static final <T> ObjectFilter<T> equals(final T object) {
        return new EqualsObjectFilter<T>(object);
    }

    private Filters() {
    }
}
