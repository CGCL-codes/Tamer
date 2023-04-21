package it.eng.bxmodeller.webApp;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;

public class SendMailUsingAuthentication {

    private static final String SMTP_HOST_NAME = "";

    private static final String SMTP_AUTH_USER = "";

    private static final String SMTP_AUTH_PWD = "";

    public void postMail(String recipients[], String subject, String message, String from) throws MessagingException {
        boolean debug = false;
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(debug);
        Message msg = new MimeMessage(session);
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
    }

    /**
* SimpleAuthenticator is used to do simple authentication
* when the SMTP server requires it.
*/
    private class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }
}
