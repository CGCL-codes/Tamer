package common.notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotifier {

    public static String host = "smtp.gmail.com";

    public static String from = "NeuroLib <neurolib.email@gmail.com>";

    public static String to = "risto.laakso@gmail.com";

    public static DateFormat df = new SimpleDateFormat("dd MMM yyyy  HH:mm:ss.SSS");

    public static void notifyNoFail(String messageText) {
        try {
            notify(messageText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void notify(String messageText) throws AddressException, MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Authenticator auth = new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("neurolib.email@gmail.com", "w72pfK3X3Hvc5l");
            }
        };
        Session session = Session.getDefaultInstance(props, auth);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Notification from NeuroLib - " + df.format(new Date()));
        message.setText(messageText);
        Transport.send(message);
    }

    public static void main(String[] args) throws Exception {
        EmailNotifier.notify("Notification 123");
    }
}
