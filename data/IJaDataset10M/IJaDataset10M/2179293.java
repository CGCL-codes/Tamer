package org.jbfilter.core;

/**
 * The common contract for all types of filters.
 * A filter is a grouping of {@link FilterComponent}s which compose the filter criteria to apply.
 * @author Marcus Adrian
 * @param <E> the type of the objects the filter is conceived for
 */
public interface Filter<E> extends FilterComponentContainer<E> {

    /**
	 * Creates a new filter handler for this filter. Filtering can be delegated in a generic way to
	 * this handler. This is necessary for facilities which use reflection like the jbfilter-jsp library.
	 * @return
	 */
    FilterHandler<E> newFilterHandler();

    /**
	 * Cleans all {@link FilterComponent}'s.
	 * This means resetting to the components default values, whatever
	 * that means for the specific component.
	 * <p>
	 * Concretely that means, calling {@link FilterComponent#clean()} on all components.
	 */
    void clean();
}
