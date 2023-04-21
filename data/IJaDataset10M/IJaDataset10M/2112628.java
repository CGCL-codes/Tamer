package net.entropysoft.transmorph;

/**
 * Exception thrown when something goes wrong during conversion
 * 
 * @author Cedric Chabanois (cchabanois at gmail.com)
 * 
 */
public class ConverterException extends Exception {

    private static final long serialVersionUID = 393641758490474660L;

    public ConverterException(String message) {
        super(message);
    }

    public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
