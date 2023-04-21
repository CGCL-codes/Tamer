package org.fest.util;

import java.util.Collection;
import java.util.List;

/**
 * Understands a filter for elements in a collection.
 * @param <T> the generic type of the elements to return by the filter.
 *
 * @author Yvonne Wang
 */
public interface CollectionFilter<T> {

    /**
   * Filters a given collection.
   * @param target the collection to filter.
   * @return a list containing the filtered elements.
   */
    List<T> filter(Collection<?> target);
}
