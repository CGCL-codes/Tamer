package at.arcsmed.mpower.communicator.service.call;

/**
 * The call Status.<br>
 * It's like the call Status in the soap call service but to be independent of this module 
 * our own Status implementation.
 * @author msi
 * @version 1.0.1
 */
public class Status {

    private int messageId = 1;

    private int result;

    private String errorCause;

    private long timestamp;

    /**
	 * @return the messageId
	 */
    public int getMessageId() {
        return messageId;
    }

    /**
	 * @param messageId the messageId to set
	 */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
	 * @return the result
	 */
    public int getResult() {
        return result;
    }

    /**
	 * @param result the result to set
	 */
    public void setResult(int result) {
        this.result = result;
    }

    /**
	 * @return the errorCause
	 */
    public String getErrorCause() {
        return errorCause;
    }

    /**
	 * @param errorCause the errorCause to set
	 */
    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    /**
	 * @return the timestamp
	 */
    public long getTimestamp() {
        return timestamp;
    }

    /**
	 * @param timestamp the timestamp to set
	 */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
