package com.kongur.network.erp.security;

/**
 * 
 * @author fish
 * 
 */
public class ErpAccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = -4757581999998896852L;

    public ErpAccessDeniedException() {
        super();
    }

    public ErpAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErpAccessDeniedException(String message) {
        super(message);
    }

    public ErpAccessDeniedException(Throwable cause) {
        super(cause);
    }
}
