package de.ah7.imp.mail07;

import de.ah7.lib.mail.MailAttachment;
import de.ah7.lib.mail.MailException;
import de.ah7.lib.mail.MailView;
import de.ah7.lib.mail.MailRecipient;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andreas Huber <dev@ah7.de>
 */
public class MimeMessageWrapper implements MailView {

    /**
     * The log to send logging messages to.
     */
    private static Log log = LogFactory.getLog(MimeMessageWrapper.class);

    /**
     * The wrapped message.
     */
    private final MimeMessage msg;

    /**
     * Creates a MailInWrapper which wraps the given message.
     * @param msg the message to wrap
     */
    public MimeMessageWrapper(MimeMessage msg) {
        this.msg = msg;
    }

    /**
     * Returns the List of attachments of this message.
     * @return the List of attachments of this message
     * @throws de.ah7.ah2007.mail.MailException 
     */
    public List<MailAttachment> getAttachments() throws MailException {
        List<MailAttachment> result = new Vector<MailAttachment>();
        try {
            if (msg.getContent() instanceof MimeMultipart) {
                MimeMultipart mpart = (MimeMultipart) msg.getContent();
                addAttachmentsToList(result, mpart);
            }
        } catch (IOException ex) {
            throw new MailException(ex);
        } catch (MessagingException ex) {
            throw new MailException(ex);
        }
        return result;
    }

    private void addAttachmentsToList(List<MailAttachment> result, MimeMultipart mpart) throws MessagingException {
        for (int i = 0; i < mpart.getCount(); i++) {
            BodyPart bpart = mpart.getBodyPart(i);
            result.add(new MimeAttachmentWrapper(bpart));
        }
    }

    /**
     * Returns the body of this message.
     * @return the body of this message
     * @throws de.ah7.ah2007.mail.MailException 
     */
    public String getBody() throws MailException {
        try {
            if (msg.getContent() instanceof MimeMultipart) {
                MimeMultipart mpart = (MimeMultipart) msg.getContent();
                BodyPart bpart = mpart.getBodyPart(0);
                if (bpart.getContent() instanceof String) {
                    return (String) bpart.getContent();
                }
                throw new MailException("first body part is not a String!");
            } else if (msg.getContent() instanceof String) {
                return (String) msg.getContent();
            }
            throw new MailException("first part is not a String and no MimeMultipart!");
        } catch (IOException ex) {
            throw new MailException(ex);
        } catch (MessagingException ex) {
            throw new MailException(ex);
        }
    }

    /**
     * Returns the sender of this message.
     * @return the sender's Address of this message
     * @throws de.ah7.ah2007.mail.MailException 
     */
    public Address getFrom() throws MailException {
        try {
            Address result = null;
            Address[] from = this.msg.getFrom();
            if (from.length > 0) {
                result = from[0];
            }
            return result;
        } catch (MessagingException ex) {
            throw new MailException(ex);
        }
    }

    /**
     * Returns the sender(s) of this message.
     * @return the sender's Address of this message
     * @throws de.ah7.ah2007.mail.MailException 
     */
    public Address[] getFromArray() throws MailException {
        try {
            return this.msg.getFrom();
        } catch (MessagingException ex) {
            throw new MailException(ex);
        }
    }

    /**
     * Returns the recipients of this message.
     * @return the recipients addresses of this message
     * @throws de.ah7.ah2007.mail.MailException 
     */
    public Collection<MailRecipient> getRecipients() throws MailException {
        Collection<MailRecipient> result = new HashSet<MailRecipient>();
        try {
            Address[] recipients = this.msg.getRecipients(RecipientType.TO);
            for (int i = 0; i < recipients.length; i++) {
                result.add(new MailRecipientBean(MailRecipient.TO, recipients[i]));
            }
            recipients = this.msg.getRecipients(RecipientType.CC);
            for (int i = 0; i < recipients.length; i++) {
                result.add(new MailRecipientBean(MailRecipient.CC, recipients[i]));
            }
        } catch (MessagingException ex) {
            throw new MailException(ex);
        }
        return result;
    }

    /**
     * Returns the subject of this message.
     * @return the subject of this message
     * @throws de.ah7.ah2007.mail.MailException 
     */
    public String getSubject() throws MailException {
        try {
            return this.msg.getSubject();
        } catch (MessagingException ex) {
            throw new MailException(ex);
        }
    }
}
