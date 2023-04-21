package org.metaphile.tag;

/**
 * Defines a common type for all value lookups
 * 
 * @author stuart
 * @param <T> the key type
 */
public interface AllowedValue<T> {

    /**
     * Returns the actual value as stored by the tag
     * @return the actual value as stored by the tag
     */
    T getValue();

    /**
     * Returns a description that corresponds to this value
     * @return a description that corresponds to this value
     */
    String getDescription();
}
