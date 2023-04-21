package net.tsg.ui.helpers.security;

import org.apache.log4j.Logger;

public class EncryptionException extends Exception {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(EncryptionException.class);

    public EncryptionException(String msg, Exception cause) {
        super(msg, cause);
    }
}
