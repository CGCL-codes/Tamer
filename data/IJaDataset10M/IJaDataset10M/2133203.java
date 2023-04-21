package com.peterhi.server.mail;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtilities extends Authenticator {

    private static MailUtilities instance;

    public static MailUtilities getInstance() {
        if (instance == null) instance = new MailUtilities();
        return instance;
    }

    private ExecutorService pool = Executors.newCachedThreadPool();

    private Session session;

    private MailUtilities() {
        Properties props = new Properties();
        props.put("mail.user", "agent");
        props.put("mail.host", "sz2.peterhi.com");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        session = Session.getInstance(props, this);
        session.setDebug(true);
    }

    public void sendSimpleMessage(final String subject, final String content, final String contentType, final String receiver) {
        pool.execute(new Runnable() {

            public void run() {
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setSubject(subject);
                    message.setContent(content, contentType);
                    message.setFrom(new InternetAddress("agent@sz2.peterhi.com"));
                    message.addRecipient(RecipientType.TO, new InternetAddress(receiver));
                    Transport.send(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void sendSimpleMessage(final String email, final String message) {
        pool.execute(new Runnable() {

            public void run() {
                try {
                    MimeMessage msg = new MimeMessage(session);
                    msg.setText(message);
                    msg.setSubject("Message");
                    msg.setFrom(InternetAddress.getLocalAddress(session));
                    msg.addRecipient(RecipientType.TO, new InternetAddress(email));
                    Transport.send(msg);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("agent", "agent");
    }
}
