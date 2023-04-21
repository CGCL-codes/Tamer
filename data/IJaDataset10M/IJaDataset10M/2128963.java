package org.chessworks.common.javatools;

/**
 * Used to filter objects, replacing them with safe or otherwise filtered
 * versions. If no acceptable replacement exists, null is returned.
 */
public interface Filter<T> {

    public T filter(Object o);
}
