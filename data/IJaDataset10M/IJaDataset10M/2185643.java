package org.tnt.ikaixin.exception;

/**
 * It will be thrown when init failed.
 * @author TNT
 * 
 */
public class InitException extends RuntimeException {

    private static final long serialVersionUID = -1352613734254235861L;

    protected Throwable rootCause;

    public InitException(String msg) {
        super(msg);
    }

    public InitException(Throwable rootCause) {
        super();
        this.rootCause = rootCause;
    }

    /**
	     Returns descriptive text on the cause of this exception.
	   */
    public String getMessage() {
        String msg = super.getMessage();
        if (msg == null && rootCause != null) {
            msg = rootCause.getMessage();
        }
        return msg;
    }
}
