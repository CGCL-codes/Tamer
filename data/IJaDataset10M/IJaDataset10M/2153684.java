package com.rhythm.commons.validation;

import com.rhythm.base.*;

/**
 * Implementing this inteface indicates that the object will serve as a point
 * of validation for a given object (T)
 * <p>
 * <u><strong>IMPLEMENTATION RULES</strong></u><br/>
 * 1.) When <code>check(T)</code> returns an empty instance of <code>Messages</code>
 * then it is assumed that all validation has passed and the given object (T) is valid.
 * 2.) When <code>check(T)</code> returns a NON empty instance of <code>Messages</code>
 * then it is assumed that validation has failed and the given object (T) is NOT valid. 
 * The returned non empty <code>Messages</code> should contain all the broken validation
 * rules in human readable format.
 * </p>
 * @param <T>
 * @see Validatable
 * @author Michael J. Lee
 * @since 1.0
 */
public interface Validator<T> {

    /**
     * Retuns a new instance of <code>Messages</code> that contains any reasons
     * why the current state would not be valid.  It is assumed that if the returned
     * <code>Messages</code> isEmpty then no validation rules have been broken.
     * 
     * @param obj
     * @throws FailedValidationException
     * @see isValid
     */
    public void checkState(T obj) throws InvalidStateException;
}
