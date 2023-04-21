package org.op4j.exceptions;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public class TargetCastException extends RuntimeException {

    private static final long serialVersionUID = 7306992779858446095L;

    public TargetCastException(final Class<?> targetClass, final String cast) {
        super("Target of class " + targetClass.getSimpleName() + " cannot be casted as: " + cast);
    }
}
