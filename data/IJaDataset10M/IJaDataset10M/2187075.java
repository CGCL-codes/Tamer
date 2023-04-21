package net.sourceforge.jsfannonition.common.exception;

public class TechnicalException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5680611967857164171L;

    public TechnicalException() {
        super();
    }

    public TechnicalException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public TechnicalException(String arg0) {
        super(arg0);
    }

    public TechnicalException(Throwable arg0) {
        super(arg0);
    }
}
