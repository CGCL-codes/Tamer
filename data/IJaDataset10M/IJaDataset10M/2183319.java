package org.gfix.binding;

import org.gfix.exception.FixException;

public class FixBindingException extends FixException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 32543654654L;

    public FixBindingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FixBindingException(String message) {
        super(message);
    }
}
