package org.servingMathematics.mqat.exceptions;

/**
 * TODO Class description
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 */
public class InvalidDocumentException extends ExtendedExceptionImpl {

    private static final long serialVersionUID = 1L;

    public InvalidDocumentException(String message, Throwable cause, short severity) {
        super(message, cause, severity, ExtendedException.INVALID_DOCUMENT_EXCEPTION);
    }

    public InvalidDocumentException(String message) {
        super(message, null, ExtendedException.ERROR, ExtendedException.INVALID_DOCUMENT_EXCEPTION);
    }
}
