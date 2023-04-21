package org.signserver.module.wsra.common;

/**
 * Exception thrown if someone is performing a WSRA call
 * with insufficient privileges
 * 
 * 
 * @author Philip Vendil 17 okt 2008
 *
 * @version $Id: AuthorizationDeniedException.java 1403 2010-12-11 18:56:58Z netmackan $
 */
public class AuthorizationDeniedException extends Exception {

    private static final long serialVersionUID = 1L;

    public AuthorizationDeniedException(String message) {
        super(message);
    }
}
