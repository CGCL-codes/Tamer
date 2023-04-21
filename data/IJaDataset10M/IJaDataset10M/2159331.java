package com.explosion.utilities.preferences;

import com.explosion.utilities.exception.EnhancedException;

/**
 * @author Stephen Cowx
 * Created on 14-Mar-2005
 */
public class PreferenceCreateException extends EnhancedException {

    /**
     * 
     */
    public PreferenceCreateException() {
        super();
    }

    /**
     * @param message
     * @param originalException
     */
    public PreferenceCreateException(String message, Exception originalException) {
        super(message, originalException);
    }

    /**
     * @param message
     */
    public PreferenceCreateException(String message) {
        super(message);
    }
}
