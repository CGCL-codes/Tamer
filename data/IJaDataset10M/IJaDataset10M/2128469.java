package net.sf.parser4j.parser.service.parsenode;

/**
 * throws when parse node have too much alternative ( ambiguous grammar )
 * 
 * @author luc peuvrier
 * 
 */
public class TooMuchAlternativeParserException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5954029929829923704L;

    public TooMuchAlternativeParserException() {
        super();
    }

    public TooMuchAlternativeParserException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TooMuchAlternativeParserException(final String message) {
        super(message);
    }

    public TooMuchAlternativeParserException(final Throwable cause) {
        super(cause);
    }
}
