package org.tigr.antware.shared.exceptions;

public class DuplicateHashCodeException extends RuntimeException {

    private static final long serialVersionUID = 3256439201015870519L;

    public DuplicateHashCodeException(String message) {
        super(message);
    }
}
