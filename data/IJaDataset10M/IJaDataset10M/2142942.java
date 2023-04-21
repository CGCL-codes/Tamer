package com.rapidminer.tools;

/**
 * Indicates that an exception during XML parsing occurred.
 * 
 * @author Ingo Mierswa, Simon Fischer
 */
public class XMLException extends Exception {

    private static final long serialVersionUID = 2067739218636324872L;

    /** Creates a new XML exception. */
    public XMLException(String msg) {
        super(msg);
    }

    /** Creates a new XML exception. */
    public XMLException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
