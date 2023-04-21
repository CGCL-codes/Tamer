package org.xblackcat.rojac.service.options.converter;

/**
 * @author xBlackCat
 */
public interface IConverter<T> {

    /**
     * Converts a string to Object.
     *
     * @param s stringified object.
     * @return Generated object or <code>null</code> if passed value is either <code>null</code> or have invalid format
     *         and can not be represented as an object
     */
    T convert(String s);

    /**
     * Returns string representation of the object.
     *
     * @param o
     * @return Object representation or <code>null</code> if passed value is null.
     */
    String toString(T o);
}
