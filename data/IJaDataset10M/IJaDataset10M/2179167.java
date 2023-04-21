package com.sonic.error.logger;

import com.sonic.error.base.WrappingException;

/**
 * 
 * ErrorCodeException class.<br>
 *
 * @author <b>Adam Dec</b><br><i>Copyright &#169; 2008</i>
 * @since 2008-10-05
 * @version 1.0
 * @category com.sonic.error.logger
 * @project mQuotes-Middleware
 */
@SuppressWarnings("serial")
public class ErrorCodeException extends WrappingException {

    private int errorCode = -1;

    public ErrorCodeException() {
        super();
    }

    /**
	 * @param message
	 */
    public ErrorCodeException(String message) {
        super(message);
    }

    /**
	 * @param message
	 * @param errorCode
	 */
    public ErrorCodeException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
	 * @param message
	 * @param rootCause
	 */
    public ErrorCodeException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    /**
	 * @param message
	 * @param errorCode
	 * @param rootCause
	 */
    public ErrorCodeException(String message, int errorCode, Throwable rootCause) {
        super(message, rootCause);
        this.errorCode = errorCode;
    }

    /**
	 * @param rootCause
	 */
    public ErrorCodeException(Throwable rootCause) {
        super(rootCause);
    }

    /**
	 * @param rootCause
	 */
    public ErrorCodeException(ErrorCodeException rootCause) {
        super(rootCause);
        this.errorCode = rootCause.errorCode;
    }

    /**
	 * @return Kod bledu lub 0 jesli kod nie zostal zapisany
	 */
    public int getErrorCode() {
        return errorCode;
    }

    /**
	 * Ustawia kod bledu
	 * 
	 * @param errorCode
	 *            Nowa wartosc kodu bledu
	 */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
