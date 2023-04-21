package com.hs.mail.imap;

public class ImapException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String responseCode;

    /**
	 * Constructor for ImapException
	 * 
	 * @param message
	 */
    public ImapException(String message) {
        this(null, message);
    }

    /**
	 * Constructor for ImapException
	 * 
	 * @param responseCode
	 * @param message
	 */
    public ImapException(String responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public String getResponseCode() {
        return responseCode;
    }
}
