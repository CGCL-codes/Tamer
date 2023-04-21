package com.explosion.expfmodules.wizard.standard.load;

import com.explosion.utilities.exception.EnhancedException;

/**
 * @author Stephen Cowx
 * Created on 22-Dec-2004
 */
public class ExpectedElementNotFoundException extends EnhancedException {

    /**
     * 
     */
    public ExpectedElementNotFoundException() {
        super();
    }

    /**
     * @param message
     * @param originalException
     */
    public ExpectedElementNotFoundException(String message, Exception originalException) {
        super(message, originalException);
    }

    /**
     * @param message
     */
    public ExpectedElementNotFoundException(String message) {
        super(message);
    }
}
