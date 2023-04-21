package com.google.android.util;

/**
 * A procedure.
 */
public interface Procedure<T> {

    /**
     * Applies this procedure to the given parameter.
     */
    void apply(T t);
}
