package gov.nist.applet.phone.ua;

/**
 * This class represents an instant message
 * 
 * @author Jean Deruelle
 *
 * <a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
 */
public class InstantMessage {

    private String message = null;

    private String sender = null;

    /**
	 * Constructor creating an instant message with a content and a sender
	 */
    public InstantMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    /**
	 * Get the content of the instant message
	 * @return the content of the instant message
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * Set the content of the instant message
	 * @param message - message received
	 */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
	 * Get the sender of the instant message
	 * @return the sender of the instant message
	 */
    public String getSender() {
        return sender;
    }

    /**
	 * Set the sender of the instant message
	 * @param sender - sender of the instant message
	 */
    public void setSender(String sender) {
        this.sender = sender;
    }
}
