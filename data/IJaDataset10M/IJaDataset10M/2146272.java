package org.toobs.framework.jms.email;

import org.toobs.framework.exception.BaseException;

public class JmsEmailException extends BaseException {

    /**
   * 
   */
    private static final long serialVersionUID = -2663045491414775927L;

    public JmsEmailException() {
    }

    public JmsEmailException(String message) {
        super(message);
    }

    public JmsEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public JmsEmailException(Throwable cause) {
        super(cause);
    }
}
