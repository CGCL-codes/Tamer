package ch.unibe.a3ubAdmin.util;

import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

/**
 * This Class is a simple wrapper of the commons-email-1.0-api It offers some
 * prebuilt Methods to send Email in the University of Berne-Context. The Class
 * is implemented as singleton and can be called from anywhere. The requirde
 * jars are: - activation.jar - ﻿smtp.jar -﻿ mail.jar -﻿ commons-email-1.0.jar
 * 
 * You can get this .jars from the Project zz.further_stuff under /lib/mail
 * 
 * @author daniel marthaler
 * @version 1.1 / letzte aenderung: 29.11.2005
 * @since JDK 1.4.2
 */
public class EmailSender {

    private final String HOST = "ubecx.unibe.ch";

    private Log log = LogFactory.getLog(getClass());

    /** the singleton stuff */
    private static EmailSender instance = null;

    public static EmailSender getInstance() {
        if (instance == null) instance = new EmailSender();
        return instance;
    }

    private EmailSender() {
    }

    public void sendMail(String sender, String recipient, String subject, String text) throws Exception {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", this.HOST);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        message.setHeader("Content-Transfer-Encoding", "base64");
        message.setText(text, "iso-8859-1");
        message.setFrom(new InternetAddress(sender, sender));
        message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient, recipient));
        message.setSubject(subject);
        Transport.send(message);
        this.log.info("Mail successfully sent to: " + recipient);
    }

    public void sendMailWithAttachement(String sender, String recipient, String subject, String text, String filePath) throws Exception {
        MultiPartEmail mail = new MultiPartEmail();
        mail.attach(this.createAttachment(filePath));
        mail.setFrom(sender);
        mail.setHostName(this.HOST);
        mail.addTo(recipient);
        mail.setSubject(subject);
        mail.setMsg(text);
        mail.send();
    }

    public void sendMailWithMultipleAttachement(String sender, String recipient, String subject, String text, String[] filePathes) throws Exception {
        MultiPartEmail mail = new MultiPartEmail();
        for (int i = 0; i < filePathes.length; i++) {
            mail.attach(this.createAttachment(filePathes[i]));
        }
        mail.setFrom(sender);
        mail.setHostName(this.HOST);
        mail.addTo(recipient);
        mail.setSubject(subject);
        mail.setMsg(text);
        mail.send();
    }

    private EmailAttachment createAttachment(String filePath) {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(filePath);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        String fileName = filePath;
        String[] splittedByBackslash = filePath.split("\\\\");
        String[] splittedBySlash = filePath.split("/");
        if (splittedByBackslash.length > 1) {
            fileName = splittedByBackslash[splittedByBackslash.length - 1];
        } else if (splittedBySlash.length > 1) {
            fileName = splittedBySlash[splittedBySlash.length - 1];
        }
        attachment.setDescription(fileName);
        attachment.setName(fileName);
        return attachment;
    }

    public static void main(String[] args) {
        try {
            String[] arr = { "/Users/dama/axis.log", "/Users/dama/axis.log", "/Users/dama/axis.log" };
            EmailSender.getInstance().sendMailWithMultipleAttachement("daniel.marthaler@id.unibe.ch", "daniel.marthaler@id.unibe.ch", "subjekt", "hallo", arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
