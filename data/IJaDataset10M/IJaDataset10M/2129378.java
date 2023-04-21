package net.sf.parser4j.parser.service;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class ParserRunTimeException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4951475970161316362L;

    public ParserRunTimeException() {
        super();
    }

    public ParserRunTimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ParserRunTimeException(final String message) {
        super(message);
    }

    public ParserRunTimeException(final Throwable cause) {
        super(cause);
    }
}
