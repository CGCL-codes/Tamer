package com.miniTwitter.exceptions;

public class NullTweetException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public NullTweetException() {
        super("El nombre tweet es nulo.");
    }

    public NullTweetException(String message) {
        super(message);
    }
}
