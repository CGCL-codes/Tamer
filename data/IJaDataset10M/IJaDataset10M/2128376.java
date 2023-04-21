package hu.sztaki.dsd.abilities.notification.broker;

import com.skype.Skype;
import com.skype.SkypeException;
import hu.sztaki.dsd.abilities.notification.util.PropertiesLoader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;
import javax.jbi.JBIException;
import javax.jbi.messaging.InOnly;
import javax.jbi.messaging.MessageExchange;
import javax.jbi.messaging.MessagingException;
import javax.jbi.messaging.NormalizedMessage;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.servicemix.MessageExchangeListener;
import org.apache.servicemix.components.util.ComponentSupport;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.SimpleMailMessage;

/**
 * Sends e-mail messages to given addresses.
 * @author Zoltan Toth, MTA SZTAKI
 */
public class MessageSender {

    private static final Log log = LogFactory.getLog(MessageSender.class);

    private String smtpServer;

    private String smtpProxy;

    private static Properties props;

    private static final String BROKER_PROPERTY_FILE = "/notificationbroker.properties";

    public static void main(String[] args) {
        System.out.println("************************* MESSAGESENDER RUNNING *************************");
        String toUri = null;
        String subject = null;
        String body = null;
        if (args.length == 3) {
            toUri = args[0];
            subject = args[1];
            body = args[2];
        } else {
            System.out.println("USAGE: java hu.sztaki.dsd.abilities.notification.broker.MessageSender " + "toURI subject body");
        }
        MessageSender ms = new MessageSender("smtp.sztaki.hu");
        try {
            ms.send(new URI(toUri), subject, body);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        System.out.println("*************************   MESSAGESENDER END   *************************");
    }

    /**
     * Creates a new instance of MessageSender and sets the smtpServer which is
     * used to send the e-mail messages.
     * @param smtpServer The address of the used smtp server (e.g.: "smtp.foo.org")
     */
    public MessageSender(String smtpServer) {
        this.smtpServer = smtpServer;
        try {
            props = PropertiesLoader.getInstance(BROKER_PROPERTY_FILE).getProperties();
        } catch (IOException ie) {
            log.error("Error loading property file: " + BROKER_PROPERTY_FILE);
        }
    }

    /**
     * Selects the appropriate message sending mechanism depending on the scheme
     * part of the uri parameter, and initiates its send method.
     * Currently sending only e-mail messages is supported.
     * @param uri The uri of the message (e.g.: "mailto:somebody@foo.org")
     * @param content The content of the message
     * @param topic The topic of the message
     * @param from The from-field of the message (e.g.: "somebody@foo.org")
     */
    public boolean send(URI uri, String topic, String content, String from) {
        String scheme = uri.getScheme();
        if (scheme.equals("mailto")) {
            sendEmail(smtpServer, uri.getSchemeSpecificPart(), from, topic, content);
        }
        return true;
    }

    /**
     * Selects the appropriate message sending mechanism depending on the scheme
     * part of the uri parameter, and initiates its send method.
     * Currently sending only e-mail messages is supported.
     * @param uri The uri of the message (e.g.: "mailto:somebody@foo.org")
     * @param content The content of the message
     * @param topic The topic of the message
     * @param from The from-field of the message (e.g.: "somebody@foo.org")
     */
    public boolean send(URI uri, String topic, String content) {
        String scheme = uri.getScheme();
        if (scheme.equals("mailto")) {
            sendEmail(smtpServer, uri.getSchemeSpecificPart(), props.getProperty("mail_from"), topic, content);
        } else if (scheme.equals("skype")) {
            sendSkype(uri.getSchemeSpecificPart(), content);
        } else if (scheme.equals("xmpp")) {
            sendXMPP(props.getProperty("gtalk_from"), props.getProperty("gtalk_pw"), uri.getSchemeSpecificPart(), content);
        } else if (scheme.equals("sms")) {
            sendSMS(smtpServer, uri.getSchemeSpecificPart(), props.getProperty("mail_from"), content);
        }
        return true;
    }

    public static void sendXMPP(String fromUserId, String fromUserPw, String toUserId, String text) {
        log.info("---------------------------------------");
        log.info("Try to send Google Talk chat message...");
        log.info("from:" + fromUserId);
        log.info("pass:" + fromUserPw);
        log.info("to  :" + toUserId);
        log.info("text:" + text);
        ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        XMPPConnection connection = new XMPPConnection(config);
        try {
            connection.connect();
            connection.login(fromUserId, fromUserPw);
        } catch (XMPPException ex) {
            ex.printStackTrace();
        }
        ChatManager chatmanager = connection.getChatManager();
        org.jivesoftware.smack.Chat newChat = chatmanager.createChat(toUserId, new MessageListener() {

            public void processMessage(org.jivesoftware.smack.Chat chat, org.jivesoftware.smack.packet.Message message) {
                System.out.println("Received message: " + message);
            }
        });
        try {
            newChat.sendMessage(text);
            log.info("XMPP message sent OK.");
        } catch (XMPPException e) {
            System.out.println("ERROR Delivering block");
        }
    }

    public static void sendSkype(String toName, String text) {
        log.info("---------------------------------------");
        log.info("Try to send Skype message using local Skype installation...");
        log.info("to  :" + toName);
        log.info("text:" + text);
        try {
            Skype.chat(toName).send(text);
            log.info("Skype message sent OK.");
        } catch (SkypeException ex) {
            ex.printStackTrace();
        }
    }

    public static void sendSMS(String smtpServer, String toNum, String from, String text) {
        log.info("---------------------------------------");
        log.info("Try to send SMS through EMAIL2SMS gateway...");
        log.info("smtp  :" + smtpServer);
        log.info("toNum :" + toNum);
        log.info("from  :" + from);
        log.info("text  :" + text);
        sendEmail(smtpServer, props.getProperty("email2sms"), from, toNum, text);
    }

    /**
     * "send" method to send e-mail messages.
     * @param smtpServer The smtp server for outgoing messages
     * @param to The address of the message (e.g.: "somebody@foo.org")
     * @param from the from-field of the message (e.g.: "somebody@foo.org")
     * @param subject The subject of the message
     * @param body The content of the message
     */
    public static void sendEmail(String smtpServer, String to, String from, String subject, String body) {
        try {
            log.info("smtpServer:" + "'" + smtpServer + "'");
            log.info("to:" + "'" + to + "'");
            log.info("from:" + "'" + from + "'");
            log.info("subject:" + "'" + subject + "'");
            log.info("body:" + body);
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(smtpServer);
            SimpleMailMessage m = new SimpleMailMessage();
            m.setSubject(subject);
            m.setFrom(from);
            m.setTo(to);
            m.setText(body);
            sender.send(m);
            log.info("Email sent OK.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
