package lcd_api.messages.inbound;

import lcd_api.infrastructure.exceptions.IllegalAttributeException;

/**
 * The LISTEN message is received when a screen goes active on the server. This
 * is generally an unsolicited message and can arrive at any time. It will
 * contain the ScreenID of the screen that is currently active.
 * 
 * @author Robert Derelanko
 */
public class Listen extends InboundMessage {

    /**
	 * A field containing the enumerated representation of the inbound message
	 * type. This will be set by a subclass constructor.
	 * 
	 * Each subclass will statically define their own message type.
	 */
    private static InboundMessageType messageType = InboundMessageType.LISTEN;

    /**
	 * Contains the scree_id of the active screen.
	 */
    private String screen_id = null;

    /**
	 * The main constructor.
	 */
    public Listen() {
        super();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void addAttribute(InboundMessageAttributes theAttribute, Object theValue) throws IllegalAttributeException {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public InboundMessageType getMessageType() {
        return messageType;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected void processMessageText(String theMessageText) throws IllegalArgumentException {
        if (theMessageText == null || theMessageText.length() == 0) {
            StringBuilder exceptionBuilder = new StringBuilder();
            exceptionBuilder.append("The supplied ");
            exceptionBuilder.append(getMessageType().toString());
            exceptionBuilder.append(" message text null or empty.");
            throw new IllegalArgumentException(exceptionBuilder.toString());
        }
        String[] messageComponents = theMessageText.split(" ");
        if (messageComponents == null || messageComponents.length == 0) {
            StringBuilder exceptionBuilder = new StringBuilder();
            exceptionBuilder.append("The supplied ");
            exceptionBuilder.append(getMessageType().toString());
            exceptionBuilder.append(" message text is not in the correct ");
            exceptionBuilder.append("format.");
            throw new IllegalArgumentException(exceptionBuilder.toString());
        }
        if (!messageComponents[0].equals(getMessageType().getTag())) {
            StringBuilder exceptionBuilder = new StringBuilder();
            exceptionBuilder.append("The supplied message text does not ");
            exceptionBuilder.append("appear to be a ");
            exceptionBuilder.append(getMessageType().toString());
            exceptionBuilder.append(" message!");
            throw new IllegalArgumentException(exceptionBuilder.toString());
        }
        this.screen_id = messageComponents[1];
    }

    /**
	 * Method to return the screen_id as reported by the server.
	 * 
	 * @return The screen_id string.
	 */
    public String getScreenID() {
        return this.screen_id;
    }
}
