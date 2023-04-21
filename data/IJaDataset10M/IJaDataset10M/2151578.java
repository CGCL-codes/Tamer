package org.nakedobjects.noa.exceptions;

/**
 * Superclass of exceptions which indicate an attempt to interact
 * with a class member that is in some way disabled or unusable.
 */
public abstract class DisabledException extends AbstractSemanticException {

    private static final long serialVersionUID = 1L;

    public DisabledException(String id) {
        this(id, null);
    }

    public DisabledException(String id, String message) {
        super(id, message);
    }
}
