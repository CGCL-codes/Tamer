package com.google.code.xbeejavaapi.api.exception;

import org.apache.log4j.Logger;

/**
 *
 * @author David Miguel Antunes <davidmiguel [ at ] antunes.net>
 */
public class ChecksumFailedException extends Exception {

    private static final Logger logger = Logger.getLogger(ChecksumFailedException.class);

    public ChecksumFailedException(Throwable cause) {
        super(cause);
    }

    public ChecksumFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChecksumFailedException(String message) {
        super(message);
    }

    public ChecksumFailedException() {
        super();
    }
}
