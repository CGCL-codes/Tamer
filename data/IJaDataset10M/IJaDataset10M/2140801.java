package org.apache.nutch.util.mime;

/**
 * A class to encapsulate MimeType related exceptions.
 *
 * @author Hari Kodungallur
 * @author Jerome Charron - http://frutch.free.fr/
 */
public class MimeTypeException extends Exception {

    /**
     * Constructs a MimeTypeException with no specified detail message.
     */
    public MimeTypeException() {
        super();
    }

    /**
     * Constructs a MimeTypeException with the specified detail message.
     * @param msg the detail message.
     */
    public MimeTypeException(String msg) {
        super(msg);
    }
}
