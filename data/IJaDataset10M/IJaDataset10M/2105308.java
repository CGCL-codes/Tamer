package org.posterita.exceptions;

public class InvalidBankAccountException extends ApplicationException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public InvalidBankAccountException(String message) {
        super(message);
    }
}
