package org.op4j.exceptions;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public class NonUniqueTargetException extends RuntimeException {

    private static final long serialVersionUID = 2917055239442829002L;

    public NonUniqueTargetException() {
        super("Operator has more than one target");
    }
}
