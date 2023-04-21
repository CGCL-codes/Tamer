package vq.codec.exception;

/**
 * This exception must be throw when any error occurs in the process of decode
 * an encoded input.
 * 
 * @author Bruno Cartaxo
 * 
 * @since SEP 2009
 */
public class DecodingException extends Exception {

    private static final long serialVersionUID = -8875476799775431310L;

    /**
	 * 
	 * @see java.lang.Exception
	 */
    public DecodingException(String message) {
        super(message);
    }
}
