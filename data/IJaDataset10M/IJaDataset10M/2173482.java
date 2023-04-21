package org.openofficesearch.io;

/**
 * Thrown when a document can't be read<br />
 * Created: 2005
 * @author Connor Garvey
 * @version 0.1.1
 * @since 0.0.1
 */
public class DocumentParseException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
   * Creates a new document parse exception with a message
   * @param message A message describing the reason the exception was thrown
   */
    public DocumentParseException(String message) {
        super(message);
    }
}
