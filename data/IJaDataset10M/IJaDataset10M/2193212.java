package common.elearning.mail.event.response;

import java.util.Vector;
import common.elearning.event.ElearningResponseEvent;
import common.elearning.event.EventsHierarchy;
import common.elearning.util.Mail;

public class MailGetMessageResponse extends ElearningResponseEvent {

    private Mail mail;

    public MailGetMessageResponse(String session, Mail mail) {
        super(session);
        this.mail = mail;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = -1104376761209442527L;

    @Override
    public String getCategory() {
        return EventsHierarchy.CATEGORY_APPLICATION_MAIL;
    }

    @Override
    public long getId() {
        return EventsHierarchy.EVENT_RESPONSE_MAIL_GET_MESSAGE;
    }

    public Mail getMail() {
        return mail;
    }
}
