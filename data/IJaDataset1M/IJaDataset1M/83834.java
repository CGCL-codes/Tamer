package com.core.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.security.Security;
import java.util.*;

public class SimpleSMTP {

    private String smtpUser = "express3.com@gmail.com";

    private String smtpPassword = "xxxxxxx";

    private String smtpEmailFrom = "express3.com@gmail.com";

    private String smtpEmailFromName = "express3.com@gmail.com";

    private String smtpHost = "smtp.gmail.com";

    private String smtpPort = "465";

    private boolean debug = true;

    public static void main(String[] args) {
        System.out.println("Start sending email.");
        Properties props = new Properties();
        props.put("mail.to", "colombie@gmail.com");
        props.put("mail.subject", "Test Email");
        props.put("mail.text", "Dieses Email testet die SMTP Einstellungen.");
        SimpleSMTP smtp = new SimpleSMTP();
        try {
            smtp.sendEmail(props, true);
        } catch (Exception e) {
            System.out.println("Could not send email: " + e);
        }
        System.out.println("Email sent.");
    }

    public void sendEmail(String user, String password, String emailFrom, String emailFromName, String host, String port, Properties props, boolean ssl) throws Exception {
        smtpUser = user;
        smtpPassword = password;
        smtpEmailFrom = emailFrom;
        smtpEmailFromName = emailFromName;
        smtpHost = host;
        smtpPort = port;
        sendEmail(props, ssl);
    }

    public void sendEmail(Properties props, boolean ssl) throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        props.put("mail.smtp.user", smtpUser);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.debug", debug + "");
        props.put("mail.smtp.socketFactory.port", smtpPort);
        if (ssl) {
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        props.put("mail.smtp.socketFactory.fallback", "false");
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
        session.setDebug(debug);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(smtpEmailFrom, smtpEmailFromName));
        msg.setSubject(props.getProperty("mail.subject"));
        msg.setText(props.getProperty("mail.text"));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(props.getProperty("mail.to")));
        if (props.getProperty("mail.to.bcc") != null) {
            msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(props.getProperty("mail.to.bcc")));
        }
        Transport.send(msg);
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpUser, smtpPassword);
        }
    }
}
