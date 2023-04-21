package org.isokissa.sfw;

/**
 * 
 * @author pt_jr
 */
public class SfwException extends Exception {

    private static final long serialVersionUID = 1L;

    public SfwException(String message, Exception e) {
        super(message, e);
    }

    public SfwException(String message) {
        super(message);
    }
}
