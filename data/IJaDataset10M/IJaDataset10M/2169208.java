package ca.ubc.jquery.api.tyruba;

import ca.ubc.jquery.api.JQueryException;

/**
 * A Miscellaneous Exception class
 * 
 * @author lmarkle
 */
public class JQueryTyRuBaException extends JQueryException {

    protected JQueryTyRuBaException(String message) {
        super(message);
    }

    protected JQueryTyRuBaException(String message, Throwable e) {
        super(message, e);
    }
}
