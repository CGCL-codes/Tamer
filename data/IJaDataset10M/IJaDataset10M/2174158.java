package org.semanticweb.skos;

/**
 * Author: Simon Jupp<br>
 * Date: Apr 25, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSStorageException extends SKOSException {

    public SKOSStorageException(String message) {
        super(message);
    }

    public SKOSStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public SKOSStorageException(Throwable cause) {
        super(cause);
    }
}
