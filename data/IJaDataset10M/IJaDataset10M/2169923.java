package net.sf.leechget.filter.filters;

import net.sf.leechget.filter.ObjectFilter;

/**
 * Returns the opposite of any value returned by another filter.
 * 
 * @author Rogiel Josias Sulzbach
 * 
 * @param <T>
 *            the type of the object to filter.
 * @see ObjectFilter
 */
public class NotObjectFilter<T> implements ObjectFilter<T> {

    private final ObjectFilter<T> filter;

    public NotObjectFilter(final ObjectFilter<T> filter) {
        this.filter = filter;
    }

    @Override
    public boolean accept(final T object) {
        return !this.filter.accept(object);
    }
}
