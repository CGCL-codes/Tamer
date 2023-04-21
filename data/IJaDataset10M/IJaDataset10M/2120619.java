package org.noranj.formak.server.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.noranj.formak.server.domain.core.Attachment;
import org.noranj.formak.server.domain.core.MailMessage;
import org.noranj.formak.shared.Constants;
import org.noranj.formak.shared.GlobalSettings;
import org.noranj.formak.shared.type.MIMEType;

/**
 * This class gets a MIMEMEssage and then put it in a simpler data structure named MailMessage. 
 * 
 * This module, both source code and documentation, is in the Public Domain, and
 * comes with NO WARRANTY. See http://www.noranj.org for further information.
 * 
 * The code is based on comments at this link
 * http://www.coderanch.com/t/449220/java
 * /java/Getting-Multiple-Attachments-MIMEMessage
 * 
 * @author
 * @since 0.3.2012MAR21
 * @version 0.3.2012MAR21
 * @change
 * 
 */
public class MailHelper {

    private static Logger logger = Logger.getLogger(MailHelper.class.getName());

    /**
	 * 
	 * @param message
	 */
    public static void sendMailMessage(MailMessage mailMessage) throws Exception {
        logger.info("sendMailMessage - sending email - " + mailMessage.toShortString());
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        if (logger.isLoggable(Level.FINE)) logger.fine("sendMailMessage - got the Mail Session");
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(mailMessage.getFrom());
        msg.addRecipients(Message.RecipientType.TO, mailMessage.getTo());
        msg.setSubject(mailMessage.getSubject());
        msg.setText(mailMessage.getBody().getContentAsString());
        if (logger.isLoggable(Level.FINE)) logger.fine("sendMailMessage - The message is ready to be sent");
        Transport.send(msg);
        logger.info("sendMailMessage - The message is sent");
    }

    /**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
    public static MailMessage getMailMessage(Message message) throws Exception {
        MailMessage mailMessage = new MailMessage();
        mailMessage.setSubject(message.getSubject());
        mailMessage.setFrom(message.getFrom());
        mailMessage = handleMessage(message, mailMessage);
        return (mailMessage);
    }

    /**
	 * 
	 * @param message
	 * @param mailMessage
	 * @return
	 * @throws Exception
	 */
    private static MailMessage handleMessage(Message message, MailMessage mailMessage) throws Exception {
        Object content = message.getContent();
        if (content instanceof String) {
            logger.info("content is a String");
            if (mailMessage.getBody() == null) {
                logger.info("Found the BODY");
                mailMessage.setBody(new Attachment(((String) content).getBytes(), Constants.C_MAIL_BODY_PROP_NAME, MIMEType.TXT));
                logger.fine("body [" + (String) content + "]");
            }
        } else if (content instanceof Multipart) {
            logger.info("content is a multipart message");
            Multipart mp = (Multipart) content;
            mailMessage = handleMultipart(mp, mailMessage);
        }
        return (mailMessage);
    }

    /**
	 * It is called when the email is a multipart email.
	 * The class uses the first part as BODY of the email if
	 * there has been no BODY detected yet.
	 * 
	 * All parts are stored as attachment.
	 * 
	 * @param mp
	 * @param mailMessage
	 * @return
	 * @throws Exception
	 */
    private static MailMessage handleMultipart(Multipart mp, MailMessage mailMessage) throws Exception {
        int count = mp.getCount();
        int i = 0;
        if (count > i && (mailMessage.getBody() == null)) {
            logger.info("Found the BODY");
            BodyPart bp = mp.getBodyPart(i);
            mailMessage.setBody(new Attachment(((String) bp.getContent()).getBytes(), Constants.C_MAIL_BODY_PROP_NAME, MIMEType.valueOfWithEncoding(bp.getContentType())));
            logger.fine("body [" + (String) bp.getContent() + "]");
            ++i;
        }
        for (; i < count; i++) {
            BodyPart bp = mp.getBodyPart(i);
            Object content = bp.getContent();
            if (bp.getDisposition() == Part.ATTACHMENT) {
                if (content instanceof String) {
                    logger.info("\tcontent is ATTACHMENT-STRING - part [" + i + "] is [" + (String) content + "]");
                    mailMessage.addAttachment(new Attachment(((String) content).getBytes(), bp.getFileName(), MIMEType.valueOfWithEncoding(bp.getContentType())));
                } else if (content instanceof InputStream) {
                    mailMessage.addAttachment(new Attachment(Utils.readAll((InputStream) content), bp.getFileName(), MIMEType.valueOfWithEncoding(bp.getContentType())));
                    logger.info("\tcontent is ATTACHMENT-InputStream - part [" + i + "] is [" + String.valueOf(Utils.readAll((InputStream) content)) + "]");
                } else if (content instanceof Message) {
                    Message message = (Message) content;
                    mailMessage = handleMessage(message, mailMessage);
                } else if (content instanceof Multipart) {
                    Multipart mp2 = (Multipart) content;
                    mailMessage = handleMultipart(mp2, mailMessage);
                }
            } else {
                logger.warning("part [" + i + "] is INLINE - [" + (String) bp.getContent() + "]");
                mailMessage.addAttachment(new Attachment(((String) content).getBytes(), (bp.getFileName() + "-INLINE-" + i), MIMEType.valueOfWithEncoding(bp.getContentType())));
                logger.info("\tcontent is String - part [" + i + "] is [" + (String) content + "]");
            }
        }
        return (mailMessage);
    }

    /**
	 * Convert an array of type javx.mail.Address to javax.mail.internet.InternetAddress.
	 * This is used when converting email addresses.
	 * 
	 * @param addresses in form of javax.mail.Address which is difficult to work with them.
	 * It contains not only the email address but extra information such as name.
	 * @return email addresses in form of InternetAddress which is easier to work with it.
	 * @throws Exception
	 */
    public static InternetAddress[] convertAddressesToInternetAddresses(Address[] addresses) throws Exception {
        String addressesStr = InternetAddress.toString(addresses);
        return (InternetAddress.parse(addressesStr));
    }
}
