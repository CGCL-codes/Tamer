package org.dago.wecommand.wiimote.messages;

/**
 * ButtonsReport implementation.
 */
public class ButtonsReport extends AbstractReport {

    /**
	 * Constructor from a source message.
	 * @param message the source message
	 */
    public ButtonsReport(Message message) {
        super(message);
    }

    @Override
    public MessageType getType() {
        return MessageType.BUTTONS_REPORT;
    }
}
