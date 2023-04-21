package common.elearning.mail.event.request;

import common.elearning.event.EventsHierarchy;
import common.elearning.mail.event.MailElearningEvent;

/**
 * The Class MailGetAttachEvent.
 */
public class MailGetAttachEvent extends MailElearningEvent {

    /** The attach name. */
    private String messageId, attachName;

    /**
	 * Instantiates a new mail get attach event.
	 * 
	 * @param messageId
	 *            the message id
	 * @param attachName
	 *            the attach name
	 */
    public MailGetAttachEvent(String messageId, String attachName) {
        super();
        this.messageId = messageId;
        this.attachName = attachName;
    }

    /**
	 * Gets the message id.
	 * 
	 * @return the message id
	 */
    public String getMessageId() {
        return messageId;
    }

    /**
	 * Gets the attach name.
	 * 
	 * @return the attach name
	 */
    public String getAttachName() {
        return attachName;
    }

    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    @Override
    public long getId() {
        return EventsHierarchy.EVENT_REQUEST_MAIL_GET_ATTACH;
    }
}
