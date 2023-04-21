package com.dokumentarchiv.plugins;

/**
 * @author Carsten Burghardt
 * @version $Id: PluginServiceException.java 613 2008-03-12 21:40:35Z carsten $
 */
public class PluginServiceException extends Exception {

    /**
     * serial id 
     */
    private static final long serialVersionUID = -5400565675987945966L;

    /**
     * Default constructor
     */
    public PluginServiceException() {
    }

    /**
     * Constructor with parent exception
     * @param cause
     */
    public PluginServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with message and parent exception
     * @param msg
     * @param cause
     */
    public PluginServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructor with message
     * @param msg
     */
    public PluginServiceException(String msg) {
        super(msg);
    }
}
