package org.jbfilter.core.fcomps.single;

import org.jbfilter.core.FilterComponent;

/**
 * 
 * A {@link FilterComponent} where a single value describes its current state.
 * 
 * @author Marcus Adrian
 *
 * @param <E> the type of the beans to filter
 * @param <T> the property's type
 */
public interface SingleFilterComponent<E, T> extends FilterComponent<E> {

    /**
	 * The filter components current value.
	 * @return
	 */
    T getValue();

    /**
	 * See getter.
	 * @param value
	 */
    void setValue(T value);

    /**
	 * Defaults to {@code null}.
	 * @return
	 */
    T getValueCleanValue();

    /**
	 * The clean value.
	 * @param valueCleanValue
	 */
    void setValueCleanValue(T valueCleanValue);
}
