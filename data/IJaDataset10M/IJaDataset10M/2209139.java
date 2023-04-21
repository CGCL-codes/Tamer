package org.jeevang.hotwire.exceptions;

/**
 * @(#) ConnectionClosingException.java    0.1 Jul 20, 2004
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the SUN PUBLIC LICENSE
 * as published by Sun Microsystems.
 *
 * You should have recieved a copy of SUN PUBLIC LICENSE along with
 * this source code. If not, you can view it at www.sun.com
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 *
 *
 * @author Jeevan Gheevarghese Joseph A-1681
 *
 *
 * Revision History
 *
 * Version        Date            Author          Description
 *============================================================
 *  0.1          Jul 20, 2004      Jeevan G Joseph       First draft
 *
 *
 */
public class ConnectionClosingException extends Exception {

    public ConnectionClosingException() {
    }

    public ConnectionClosingException(String message) {
        super(message);
    }

    public ConnectionClosingException(Throwable cause) {
        super(cause);
    }

    public ConnectionClosingException(String message, Throwable cause) {
        super(message, cause);
    }
}
