package org.databene.commons;

/**
 * A basic validator interface.<br/>
 * <br/>
 * Created: 29.08.2006 08:31:19
 * @since 0.1
 * @author Volker Bergmann
 * @param <E> the type that is checked by this validator
 */
public interface Validator<E> {

    /** 
     * Checks if an object is valid.
     * @param object the object to validate
     * @return true if the specified object is valid, otherwise false 
     */
    boolean valid(E object);
}
