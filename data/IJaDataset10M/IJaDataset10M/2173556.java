package net.sf.iqser.plugin.mail.content;

import java.util.ArrayList;
import java.util.Collection;
import com.iqser.core.model.Content;

/**
 * the mail content bean.
 * @author alexandru.galos
 *
 */
public class MailContent {

    /**
	 * the content of the mail.
	 */
    private Content content;

    /**
	 * the content of the attachments.
	 */
    private Collection<Content> attachmentContents = new ArrayList<Content>();

    /**
	 * sets the mail content.
	 * @param content the mail content
	 */
    public void setContent(Content content) {
        this.content = content;
    }

    /**
	 * gets the mail content.
	 * @return  the mail content
	 */
    public Content getContent() {
        return content;
    }

    /**
	 * sets a collection of attachment contents.
	 * @param attachmentContents a list of attachment contents
	 */
    public void setAttachmentContents(Collection<Content> attachmentContents) {
        this.attachmentContents = attachmentContents;
    }

    /**
	 * gets the attachment contents.
	 * @return a collection of attachment contents
	 */
    public Collection<Content> getAttachmentContents() {
        return attachmentContents;
    }

    /**
	 * adds a new element to the attachment contents.
	 * @param messageContent the message content that is added
	 */
    public void addAttachmentContent(Content messageContent) {
        attachmentContents.add(messageContent);
    }
}
