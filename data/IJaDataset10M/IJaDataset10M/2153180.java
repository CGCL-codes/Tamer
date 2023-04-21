package de.intarsys.pdf.content;

/**
 * A unsupported operation has been found in a content stream.
 */
public class CSNotSupported extends CSWarning {

    public CSNotSupported(String message, Throwable cause) {
        super(message, cause);
    }

    public CSNotSupported(String message) {
        super(message);
    }
}
