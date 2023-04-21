package com.bonkey.util;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import nu.xom.Attribute;
import nu.xom.Element;
import org.eclipse.ui.IPropertyListener;
import com.bonkey.config.BonkeyConstants;
import com.bonkey.config.ConfigManager;

/**
 * Class which listens to the ConfigManager for log events
 * and logs to email
 * 
 * @author marcel
 *
 */
public class EmailLogger implements IPropertyListener {

    /**
	 * Buffer used to store contents of email log before sending
	 */
    private StringBuffer emailBuffer;

    /**
	 * Whether to log messages as well as errors
	 */
    private boolean messages;

    private String toAddress;

    private String fromAddress;

    private String subject;

    private String smtpAddress;

    private String authType;

    private String smtpUsername;

    private String smtpPassword;

    private int smtpPort;

    public static final String AUTH_NONE = Messages.getString("EmailLogger.0");

    public static final String AUTH_TLS = Messages.getString("EmailLogger.1");

    public EmailLogger(Element emailLog) {
        toAddress = emailLog.getAttributeValue(Messages.getString("EmailLogger.2"));
        fromAddress = emailLog.getAttributeValue(Messages.getString("EmailLogger.3"));
        subject = emailLog.getAttributeValue(Messages.getString("EmailLogger.4"));
        smtpAddress = emailLog.getAttributeValue(Messages.getString("EmailLogger.5"));
        smtpPort = Integer.valueOf(emailLog.getAttributeValue(Messages.getString("EmailLogger.6")));
        authType = emailLog.getAttributeValue(Messages.getString("EmailLogger.7"));
        smtpUsername = emailLog.getAttributeValue(Messages.getString("EmailLogger.8"));
        smtpPassword = emailLog.getAttributeValue(Messages.getString("EmailLogger.9"));
        messages = emailLog.getAttributeValue(Messages.getString("EmailLogger.10")).equals(Messages.getString("EmailLogger.11"));
        emailBuffer = new StringBuffer();
    }

    public EmailLogger(String toAddress, String fromAddress, String subject, String smtpAddress, int smtpPort, String authType, String smtpUsername, String smtpPassword, boolean messages) {
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.subject = subject;
        this.smtpAddress = smtpAddress;
        this.smtpPort = smtpPort;
        this.authType = authType;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.messages = messages;
        emailBuffer = new StringBuffer();
        ConfigManager.getConfigManager().addPropertyListener(this);
    }

    public void propertyChanged(Object source, int propId) {
        switch(propId) {
            case BonkeyConstants.PROP_LOG_ERROR:
                emailBuffer.append(((String) source) + "\n");
                break;
            case BonkeyConstants.PROP_LOG_MESSAGE:
                if (messages) {
                    emailBuffer.append(((String) source) + "\n");
                }
                break;
        }
    }

    /**
	 * Send the log email
	 *
	 */
    public void sendEmail() {
        if (emailBuffer.length() > 0) {
            try {
                Properties props = new Properties();
                props.put(Messages.getString("EmailLogger.14"), smtpAddress);
                props.put(Messages.getString("EmailLogger.15"), Integer.toString(smtpPort));
                if (isAuthenticated()) {
                    props.put(Messages.getString("EmailLogger.16"), Messages.getString("EmailLogger.17"));
                    props.put(Messages.getString("EmailLogger.18"), Messages.getString("EmailLogger.19"));
                    props.put(Messages.getString("EmailLogger.20"), smtpUsername);
                    props.put(Messages.getString("EmailLogger.21"), Integer.toString(smtpPort));
                    props.put(Messages.getString("EmailLogger.22"), Messages.getString("EmailLogger.23"));
                    props.put(Messages.getString("EmailLogger.24"), Messages.getString("EmailLogger.25"));
                }
                Authenticator auth = new SMTPAuthenticator();
                Session session = Session.getInstance(props, auth);
                MimeMessage msg = new MimeMessage(session);
                msg.setText(emailBuffer.toString());
                msg.setSubject(subject);
                msg.setFrom(new InternetAddress(fromAddress));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
                Transport.send(msg);
                emailBuffer = new StringBuffer();
            } catch (Exception e) {
                ConfigManager.getConfigManager().logError(Messages.getString("EmailLogger.ErrorSendingEmailLog") + e.getMessage());
            }
        }
    }

    private boolean isAuthenticated() {
        return !authType.equals(AUTH_NONE);
    }

    public Element toXML() {
        Element e = new Element(Messages.getString("EmailLogger.27"));
        e.addAttribute(new Attribute(Messages.getString("EmailLogger.28"), toAddress));
        e.addAttribute(new Attribute(Messages.getString("EmailLogger.29"), fromAddress));
        e.addAttribute(new Attribute(Messages.getString("EmailLogger.30"), subject));
        e.addAttribute(new Attribute(Messages.getString("EmailLogger.31"), smtpAddress));
        e.addAttribute(new Attribute(Messages.getString("EmailLogger.32"), Integer.toString(smtpPort)));
        e.addAttribute(new Attribute(Messages.getString("EmailLogger.33"), authType));
        e.addAttribute(new Attribute(Messages.getString("EmailLogger.34"), smtpUsername));
        e.addAttribute(new Attribute(Messages.getString("EmailLogger.35"), smtpPassword));
        e.addAttribute(new Attribute(Messages.getString("EmailLogger.36"), (messages ? Messages.getString("EmailLogger.37") : Messages.getString("EmailLogger.38"))));
        return e;
    }

    /**
	 * Set whether to log messages
	 * @param messages true to log all messages; false for errors only
	 */
    public void setMessages(boolean messages) {
        this.messages = messages;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSmtpAddress(String smtpAddress) {
        this.smtpAddress = smtpAddress;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    private class SMTPAuthenticator extends Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpUsername, smtpPassword);
        }
    }

    public void setSmtpAuthType(String newValue) {
        this.authType = newValue;
    }

    public void setSmtpPort(int newValue) {
        this.smtpPort = newValue;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public boolean isMessages() {
        return messages;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getSmtpAddress() {
        return smtpAddress;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public int getSmtpPort() {
        return smtpPort;
    }
}
