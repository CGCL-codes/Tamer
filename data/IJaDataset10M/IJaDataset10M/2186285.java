package de.ah7.imp.mail07;

import de.ah7.lib.mail.MailCompose;
import de.ah7.lib.mail.MailAttachment;
import de.ah7.lib.mail.MailDraft;
import de.ah7.lib.mail.MailRecipient;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;

/**
 * @author Andreas Huber <dev@ah7.de>
 */
public class MailMessage extends AbstractDraft implements MailDraft, MailCompose {

    private Address from = null;

    public Address getFrom() {
        return this.from;
    }

    public Address[] getFromArray() {
        return new Address[] { this.from };
    }

    public void setFrom(Address from) {
        this.from = from;
    }

    private final Collection<MailRecipient> recipients = new HashSet<MailRecipient>();

    public Collection<MailRecipient> getRecipients() {
        return this.recipients;
    }

    private String subject = null;

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    private final StringBuffer body = new StringBuffer();

    public String getBody() {
        return this.body.toString();
    }

    public StringBuffer getBodyBuffer() {
        return this.body;
    }

    public void clearBuffer() {
        this.body.setLength(0);
    }

    private final List<MailAttachment> attachments = new Vector<MailAttachment>();

    public List<MailAttachment> getAttachments() {
        return this.attachments;
    }

    public MailMessage() {
    }

    public void addAttachment(MailAttachment attachment) {
        getAttachments().add(attachment);
    }

    public void addRecipient(MailRecipient recipient) {
        this.recipients.add(recipient);
    }

    public void addRecipients(int recipientType, InternetAddress[] recipients) {
        for (int i = 0; i < recipients.length; i++) {
            addRecipient(new MailRecipientBean(recipientType, recipients[i]));
        }
    }

    public void addRecipients(int recipientType, Address[] recipients) {
        for (int i = 0; i < recipients.length; i++) {
            addRecipient(new MailRecipientBean(recipientType, recipients[i]));
        }
    }

    public FileAttachment attachFile(File attachment, String mimeType) {
        FileAttachment result = new FileAttachment(attachment, mimeType);
        addAttachment(result);
        return result;
    }

    public MailDraft toDraft() {
        return this;
    }
}
