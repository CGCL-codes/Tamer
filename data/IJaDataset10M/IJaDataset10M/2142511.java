package org.basegen.base.business.logic;

/**
 * Empty data business logic exception
 */
public class EmptyDataBusinessLogicException extends BusinessLogicException {

    /**
     * Holds message key property
     */
    private String messageKey;

    /**
     * Constructor using msg and new message key 
     * @param msg message
     * @param newMessageKey new message key
     */
    public EmptyDataBusinessLogicException(String msg, String newMessageKey) {
        super(msg);
        setMessageKey(newMessageKey);
    }

    /**
     * @return Returns the messageKey.
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * @param newMessageKey The messageKey to set.
     */
    public void setMessageKey(String newMessageKey) {
        messageKey = newMessageKey;
    }
}
