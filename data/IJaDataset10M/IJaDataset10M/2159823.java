package net.sourceforge.xconf.toolbox.generic;

/**
 * Generic filter/predicate interface.
 */
public interface Filter<T> {

    /**
     * Returns <code>true</code> if the item is accepted.
     */
    boolean accept(T item);
}
