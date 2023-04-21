package org.hip.vif.admin.member.mail;

import java.io.IOException;
import org.hip.kernel.exc.VException;
import org.hip.vif.admin.member.Activator;
import org.hip.vif.core.bom.VIFMember;
import org.hip.vif.core.interfaces.IMessages;
import org.hip.vif.core.service.PreferencesHandler;
import org.hip.vif.web.tasks.ForwardTaskRegistry;
import org.hip.vif.web.util.MailWithLink;

/**
 * Mail sent to the new member including userID and password.
 * 
 * Created on 15.08.2003
 * @author Luthiger
 */
public class CreateMemberMail extends MailWithLink {

    private static final String TMPL_LINK = "<a href=\"%s\">%s</a>";

    private static final String KEY_PASSWORD = "ui.member.editor.label.password";

    private static final String KEY_USER_ID = "ui.member.editor.label.userid";

    private static final String KEY_MAIL1 = "mail.member.mail1";

    private static final String KEY_MAIL2 = "mail.member.mail2";

    private static final String KEY_FORUM_URL = "mail.member.forum";

    private static final String KEY_PWCHNGE_URL = "mail.member.pwrd.change";

    private static final String INDENT = "     ";

    private String userID;

    private String password;

    private String forumName;

    private String urlForumChangePW;

    private String urlForumMain;

    /**
	 * CreateMemberMail default constructor.
	 * 
	 * @param inMember
	 * @throws VException
	 * @throws IOException
	 */
    public CreateMemberMail(VIFMember inMember, String inUserID, String inPassword) throws VException, IOException {
        super(inMember);
        userID = inUserID;
        password = inPassword;
        forumName = PreferencesHandler.INSTANCE.get(PreferencesHandler.KEY_FORUM_NAME);
        urlForumMain = getForumAppURL();
        urlForumChangePW = createRequestedURL(ForwardTaskRegistry.ForwardPWChangeForm.class, true);
    }

    /**
	 * @see org.hip.vif.mail.AbstractMail#getBody()
	 */
    protected StringBuilder getBody() {
        IMessages lMessages = Activator.getMessages();
        StringBuilder outBody = new StringBuilder(getFormattedMessage(lMessages, KEY_MAIL1, forumName));
        outBody.append("\n\n").append(getMessage(lMessages, KEY_MAIL2)).append("\n");
        outBody.append(INDENT).append(getMessage(lMessages, KEY_USER_ID)).append(": ").append(userID).append("\n");
        outBody.append(INDENT).append(getMessage(lMessages, KEY_PASSWORD)).append(": ").append(password).append("\n\n");
        outBody.append(getFormattedMessage(lMessages, KEY_FORUM_URL, urlForumMain)).append("\n");
        outBody.append(getFormattedMessage(lMessages, KEY_PWCHNGE_URL, urlForumChangePW)).append("\n");
        return outBody;
    }

    protected StringBuilder getBodyHtml() {
        IMessages lMessages = Activator.getMessages();
        StringBuilder outBody = new StringBuilder("<p>").append(getFormattedMessage(lMessages, KEY_MAIL1, forumName));
        outBody.append("</p><p>").append(getMessage(lMessages, KEY_MAIL2)).append("<br/>");
        outBody.append(INDENT).append(getMessage(lMessages, KEY_USER_ID)).append(": ").append(userID).append("<br/>");
        outBody.append(INDENT).append(getMessage(lMessages, KEY_PASSWORD)).append(": ").append(password).append("</p><p>");
        outBody.append(getFormattedMessage(lMessages, KEY_FORUM_URL, renderClickable(urlForumMain, urlForumMain))).append("<br/>");
        outBody.append(getFormattedMessage(lMessages, KEY_PWCHNGE_URL, renderClickable(urlForumChangePW, getMessage(lMessages, "mail.member.pwrd.label")))).append("</p>");
        return outBody;
    }

    private String renderClickable(Object... inArgs) {
        return String.format(TMPL_LINK, inArgs);
    }
}
