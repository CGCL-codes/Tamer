package com.google.gwt.view.client;

/**
 * Simple passthrough implementation of {@link ProvidesKey}.
 * 
 * @param <T> the data type of records 
 */
public class SimpleKeyProvider<T> implements ProvidesKey<T> {

    /**
   * Return the passed-in item.
   */
    public Object getKey(T item) {
        return item;
    }
}
