package org.wikiup.modules.worms.exception;

public class EntityDescriptionNotFoundException extends RuntimeException {

    public EntityDescriptionNotFoundException() {
        super();
    }

    public EntityDescriptionNotFoundException(String message) {
        super(message);
    }

    public EntityDescriptionNotFoundException(Throwable cause) {
        super(cause);
    }

    public EntityDescriptionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
