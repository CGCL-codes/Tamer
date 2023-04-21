package xades4j.providers;

import xades4j.XAdES4jException;

/**
 * Base for exceptions during time-stamp token verification.
 * @author Luís
 */
public class TimeStampTokenVerificationException extends XAdES4jException {

    public TimeStampTokenVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeStampTokenVerificationException(String msg) {
        super(msg);
    }
}
