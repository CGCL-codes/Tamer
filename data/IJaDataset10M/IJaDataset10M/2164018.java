package com.once.server.security;

import com.once.BaseException;

/**
 * Access denied exception.
 * 
 */
public class AccessDeniedException extends BaseException {

    static final long serialVersionUID = 1;

    public AccessDeniedException() {
        super();
    }

    /**
         * @param message
         *                Error message.
         */
    public AccessDeniedException(String message) {
        super(message);
    }
}
