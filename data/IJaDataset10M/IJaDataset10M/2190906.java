package com.sun.j2ee.blueprints.customer.dao;

/**
 * AccountInvalidCharException is thrown by the the account component when there
 * is some failure because of user error
 */
public class AccountInvalidCharException extends AccountException {

    /**
	 * Constructor
	 * 
	 * @param str
	 *            a string that explains what the exception condition is
	 */
    public AccountInvalidCharException(String str) {
        super(str);
    }

    public AccountInvalidCharException() {
        super();
    }
}
