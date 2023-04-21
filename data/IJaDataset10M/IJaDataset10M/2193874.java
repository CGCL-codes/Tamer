package org.basegen.base.business.logic;

/**
 * Not found business logic exception
 */
public class NotFoundBusinessLogicException extends BusinessLogicException {

    /** 
     * Holds message key
     */
    private String messageKey;

    /**
     * Constructor using msg and message key
     * @param msg message
     * @param newMessageKey new message key 
     */
    public NotFoundBusinessLogicException(String msg, String newMessageKey) {
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
