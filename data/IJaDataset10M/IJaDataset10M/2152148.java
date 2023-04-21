package org.hardtokenmgmt.admin.common;

/**
 * 
 * Exception thrown when managing bad administrator data.
 * 
 * @author Philip Vendil 16 mar 2009
 *
 * @version $Id$
 */
public class AdminDataException extends Exception {

    private static final long serialVersionUID = 1L;

    public AdminDataException() {
        super();
    }

    public AdminDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdminDataException(String message) {
        super(message);
    }
}
