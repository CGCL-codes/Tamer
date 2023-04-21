package com.j2biz.compote.exceptions;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class PluginInstallException extends CompoteException {

    /**
     * @param message
     */
    public PluginInstallException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public PluginInstallException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public PluginInstallException(String message, Throwable cause) {
        super(message, cause);
    }
}
