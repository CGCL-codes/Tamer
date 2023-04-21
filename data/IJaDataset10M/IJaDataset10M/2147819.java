package rat.document;

import java.io.IOException;

/**
 * Indicates that the document is a composite archive 
 * and cannot be read.
 */
public class CompositeDocumentException extends IOException {

    private static final long serialVersionUID = -8874256971728263295L;

    public CompositeDocumentException() {
        super("This document must be read as an archive.");
    }

    public CompositeDocumentException(String s) {
        super(s);
    }
}
