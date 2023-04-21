package com.j2biz.blogunity.services;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.j2biz.blogunity.exception.SystemException;
import com.j2biz.blogunity.pojo.SystemConfiguration;
import com.sun.mail.smtp.SMTPTransport;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class MailService extends Service implements MessageListener {

    private static final Log log = LogFactory.getLog(MailService.class);

    private static MailService SINGLETON = null;

    private Session session;

    private boolean useSmtpAuthorization = true;

    private SystemConfiguration config;

    public static MailService getInstance() {
        if (SINGLETON == null) SINGLETON = new MailService();
        return SINGLETON;
    }

    private MailService() {
    }

    public void init(SystemConfiguration config) throws SystemException {
        if (log.isInfoEnabled()) {
            log.info("Initializing service: " + getName());
        }
        this.config = config;
        try {
            Properties props = System.getProperties();
            if (useSmtpAuthorization) props.put("mail.smtp.auth", "true");
            session = Session.getDefaultInstance(props, null);
            Connection c = MQService.getInstance().getConnection();
            javax.jms.Session session = c.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            javax.jms.MessageConsumer consumer = session.createConsumer(MQService.getInstance().getMailQueue());
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            throw new SystemException(e);
        }
    }

    public void destroy(SystemConfiguration config) throws SystemException {
        if (log.isInfoEnabled()) {
            log.info("Destroying service: " + getName());
        }
    }

    /**
     * Send email message
     * 
     * @param message
     * @throws MessagingException
     * @throws SendFailedException
     */
    public void sendMessage(MimeMessage message) throws SendFailedException, MessagingException {
        SMTPTransport t = null;
        try {
            t = (SMTPTransport) session.getTransport("smtp");
            String host = config.getSmtpHost();
            String user = config.getSmtpUser();
            String pass = config.getSmtpPassword();
            t.connect(host, user, pass);
            t.sendMessage(message, message.getAllRecipients());
        } finally {
            try {
                if (t != null) t.close();
            } catch (MessagingException e) {
            }
        }
    }

    public Session getSession() {
        return session;
    }

    public void onMessage(Message message) {
        if (message instanceof MapMessage) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Sending email..");
                }
                MimeMessage m = new MimeMessage(session);
                m.setFrom(new InternetAddress(message.getStringProperty("from")));
                m.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(message.getStringProperty("to")));
                m.setSubject(message.getStringProperty("subject"));
                m.setText(message.getStringProperty("body"));
                sendMessage(m);
            } catch (Exception e) {
                log.error("Unable to send email-message!", e);
            }
        }
    }
}
