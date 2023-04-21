package net.sf.magicmap.server.exception;

/**
 * author        schweige
 * date          03.12.2004
 * copyright     (C) 2004 Martin Schweigert, Tobias H�bner
 * 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 */
public class AuthenticationException extends Exception {

    /**
     * serial version id
     */
    private static final long serialVersionUID = 5296418720666410120L;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
