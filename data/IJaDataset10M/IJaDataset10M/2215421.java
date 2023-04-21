package org.hip.vif.groups.mail;

import javax.mail.internet.InternetAddress;
import org.hip.vif.groups.Activator;
import org.hip.vif.mail.AbstractNotification;

/**
 * Notifies the subscribers about changed in the discussion group.
 * 
 * @author Benno Luthiger
 * Created on Feb 22, 2004
 */
public class SubscribersNotification extends AbstractNotification {

    private static final String KEY_SUBJECT = "org.hip.vif.msg.notification.subject2";

    private String groupName = "";

    private StringBuffer body;

    /**
	 * SubscribersNotification constructor
	 * 
	 * @param inReceiverMails InternetAddress[]
	 * @param inBody StringBuffer
	 * @param inGroupName String
	 * @param inLanguage String
	 * @param isBcc boolean
	 */
    public SubscribersNotification(InternetAddress[] inReceiverMails, StringBuffer inBody, String inGroupName, String inLanguage, boolean isBcc) {
        super(inReceiverMails, isBcc);
        setLanguage(inLanguage);
        body = inBody;
        groupName = inGroupName;
    }

    /**
	 * @see org.hip.vif.mail.AbstractNotification#getBody()
	 */
    protected StringBuffer getBody() {
        return body;
    }

    /**
	 * @see org.hip.vif.mail.AbstractNotification#getSubjectText()
	 */
    protected String getSubjectText() {
        StringBuffer outSubject = getSubjectID();
        outSubject.append(" ").append(getFormattedMessage(Activator.getMessages(), KEY_SUBJECT, groupName));
        return new String(outSubject);
    }
}
