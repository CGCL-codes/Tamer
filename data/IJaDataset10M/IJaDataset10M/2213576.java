package net.sourceforge.jsfannonition.common.exception;

public class BusinessException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7542432734933010480L;

    private String errorCode;

    private String errorMessage;

    private String messageParts[] = new String[0];

    public BusinessException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    public BusinessException(String errorCode, String[] messageParts) {
        super();
        this.errorCode = errorCode;
        this.messageParts = messageParts;
    }

    /**
	 * @return the errorCode
	 */
    public String getErrorCode() {
        return errorCode;
    }

    /**
	 * @return the errorMessage
	 */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
	 * @return the messageParts
	 */
    public String[] getMessageParts() {
        return messageParts;
    }
}
