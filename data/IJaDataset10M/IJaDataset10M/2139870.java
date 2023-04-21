package net.sourceforge.smartconversion.serialization.exception;

/**
 * Thrown when serialziation fails for a conversion.
 *
 * @author Ovidiu Dolha
 */
public class ConversionSerializationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConversionSerializationException() {
        super();
    }

    public ConversionSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConversionSerializationException(String message) {
        super(message);
    }

    public ConversionSerializationException(Throwable cause) {
        super(cause);
    }
}
