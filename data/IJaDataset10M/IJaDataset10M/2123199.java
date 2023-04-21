package ch.mye.nmapi.lib.exception;

import ch.mye.nmapi.lib.element.error.ErrorSeverity;
import ch.mye.nmapi.lib.element.error.ErrorTag;
import ch.mye.nmapi.lib.element.error.ErrorType;

public class BadElementException extends NetconfException {

    /**
     * 
     */
    private static final long serialVersionUID = -5006601172463296324L;

    public BadElementException(ErrorType type, String badElement, String message) {
        super(type, ErrorTag.BAD_ELEMENT, ErrorSeverity.ERROR, message);
        this.setErrorBadElement(badElement);
    }

    public BadElementException(String badElement, String message) {
        super(ErrorTag.BAD_ELEMENT, ErrorSeverity.ERROR, message);
        this.setErrorBadElement(badElement);
    }
}
