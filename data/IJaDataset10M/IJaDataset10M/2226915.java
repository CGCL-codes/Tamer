package net.sf.magicmap.server.facade.exception;

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
public class SessionException extends Exception {

    public SessionException(String message) {
        super(message);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
