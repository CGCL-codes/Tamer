package de.searchworkorange.indexcrawler.fileDocument.exceptions;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class NotDefinedObjectForFileDocumentException extends FileDocumentException {

    public NotDefinedObjectForFileDocumentException() {
    }

    /**
     *
     * @param message
     */
    public NotDefinedObjectForFileDocumentException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public NotDefinedObjectForFileDocumentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 
     * @param cause
     */
    public NotDefinedObjectForFileDocumentException(Throwable cause) {
        super(cause);
    }
}
