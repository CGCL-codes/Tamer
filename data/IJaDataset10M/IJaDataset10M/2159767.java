package au.org.tpac.portal.gwt.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class ValidationResponse.
 */
public class ValidationResponse implements IsSerializable {

    /** The is valid. */
    private boolean isValid = true;

    /** The message. */
    private String message = "default message";

    /**
     * Checks if is valid.
     * 
     * @return true, if is valid
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Sets the valid.
     * 
     * @param isValid the new valid
     */
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * Gets the message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     * 
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
