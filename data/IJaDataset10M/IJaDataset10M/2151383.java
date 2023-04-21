package org.javanuke.tests;

import java.net.UnknownHostException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import junit.framework.TestCase;
import org.javanuke.core.util.mail.EmailMessage;
import org.javanuke.core.util.mail.MailSender;

/**
 * 
 * @author Franklin S. Dattein ( <a href="mailto:franklin@portaljava.com">franklin@portaljava.com </a>)
 * @version $Id:
 * 
 */
public class MailSenderTest extends TestCase {

    public void testPostMail() throws Exception {
        EmailMessage msg = new EmailMessage();
        msg.setFrom("fsamir@gmail.com");
        msg.setSubject("JNuke mail tesst");
        msg.setText("test");
        msg.setTo(new String[] { "fsamir@gmail.com" });
        try {
            MailSender.postMail(msg);
        } catch (UnknownHostException e) {
        }
    }

    public void testGmailSend() throws MessagingException {
        Properties p = new Properties();
        p.put("mail.smtps.host", "smtp.gmail.com");
        p.put("mail.smtps.auth", "true");
        p.put("mail.smtps.port", "465");
        Session session = Session.getInstance(p, null);
        session.setDebug(true);
        Transport t = session.getTransport("smtps");
        t.connect("smtp.gmail.com", "jnuke@portaljava.com", "machado");
        Message message = new MimeMessage(session);
        t.sendMessage(message, new Address[] { new InternetAddress("fsamir@gmail.com") });
    }
}
