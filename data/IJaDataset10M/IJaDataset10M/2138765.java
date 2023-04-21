package org.webthree.dictionary.integrity;

import org.webthree.dictionary.message.Message;

/**
 * @author michael.gerzabek@gmx.net
 * 
 */
public class ConversionResult {

    private Message error;

    private boolean successful;

    private Object result;

    /**
         * Constructs a successful ConversionResult.
         */
    public ConversionResult(Object result) {
        this.successful = true;
        this.result = result;
    }

    /**
         * Constructs an unsuccessful ConversionResult.
         */
    public ConversionResult(Message error) {
        this.successful = false;
        this.error = error;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public Message getValidationError() {
        if (successful) {
            throw new IllegalStateException("Cannot call getValidationError() if conversion is successful.");
        }
        return error;
    }

    public Object getResult() {
        if (!successful) {
            throw new IllegalStateException("Cannot call getResult() if conversion is not successful.");
        }
        return result;
    }
}
