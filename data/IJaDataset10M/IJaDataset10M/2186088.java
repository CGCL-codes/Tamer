package org.hip.vif.forum.groups.mail;

import java.io.IOException;
import java.util.Collection;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.exc.VException;
import org.hip.kernel.mail.MailGenerationException;
import org.hip.kernel.util.ListJoiner;
import org.hip.vif.core.bom.VIFMember;
import org.hip.vif.core.interfaces.IMessages;
import org.hip.vif.forum.groups.Activator;
import org.hip.vif.web.tasks.ForwardTaskRegistry;
import org.hip.vif.web.util.MailWithLink;

/**
 * Sends mails to the group administration when a participant requests
 * a question's state changed to "answered" or "open".
 * 
 * @author Benno Luthiger
 * Created on Apr 15, 2005
 */
public class RequestForStateChangeMail extends MailWithLink {

    private static final String KEY_SUBJECT = "mail.change.state.subject";

    private static final String KEY_INTRO = "mail.change.state.intro";

    private static final String KEY_LINK = "mail.change.state.link";

    private String groupName = "";

    private String message;

    private IMessages messages = Activator.getMessages();

    /**
	 * RequestForSettlementMail constructor.
	 * 
	 * @param inAdmin VIFMember
	 * @param inAdditional Collection<GeneralDomainObject> of additional group admins (VIFMember)
	 * @param inRequestor VIFMember
	 * @param inGroupName String
	 * @param inMessage String
	 * @throws VException
	 * @throws IOException
	 */
    public RequestForStateChangeMail(VIFMember inAdmin, Collection<GeneralDomainObject> inAdditional, VIFMember inRequestor, String inGroupName, String inMessage) throws VException, IOException {
        super(inAdmin, inRequestor);
        addMailToAddress(getAdditionAddresses(inAdditional));
        groupName = inGroupName;
        message = inMessage;
        baseURL = createRequestedURL(ForwardTaskRegistry.INSTANCE.getTask(ForwardTaskRegistry.FORWARD_GROUP_ADMIN_PENDING), true);
    }

    /**
	 * @see org.hip.vif.core.mail.AbstractMail#getBody()
	 */
    protected StringBuilder getBody() {
        StringBuilder outBody = new StringBuilder(message);
        outBody.append("\n").append(getFormattedMessage(messages, KEY_LINK, baseURL)).append("\n");
        return outBody;
    }

    protected StringBuilder getBodyHtml() {
        StringBuilder outBody = new StringBuilder("<p>").append(message);
        outBody.append("<br/>").append(getFormattedMessage(messages, KEY_LINK, baseURL)).append("</p>");
        return outBody;
    }

    /**
	 * Returns the subject text, i.e. the text after the subject ID.
	 * 
	 * @return String
	 */
    protected String getSubjectText() {
        return getMessage(messages, KEY_SUBJECT);
    }

    /**
	 * Override the super classes method.
	 */
    protected StringBuilder createMailAddress() throws MailGenerationException {
        return new StringBuilder(getFormattedMessage(messages, KEY_INTRO, groupName));
    }

    private String getAdditionAddresses(Collection<GeneralDomainObject> inAdditional) throws VException {
        ListJoiner outAddresses = new ListJoiner();
        for (GeneralDomainObject lObject : inAdditional) {
            outAddresses.addEntry(((VIFMember) lObject).getMailAddress());
        }
        return outAddresses.joinSpaced(",");
    }
}
