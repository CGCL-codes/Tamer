package com.scholardesk.email;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.smtp.SMTPSSLTransport;

/**
 * Handles all the functionality for sending an email message via
 * the SMTP protocol.  It delegates most of the work to the 
 * javax.mail.* classes.
 * 
 * @author Christopher M. Dunavant
 *
 */
public class Smtp {

    private Session m_session;

    private String m_username;

    private String m_password;

    private boolean m_isSSL;

    private URLName m_urln;

    /**
	 * Sets login credentials for SMTP authentication. (optional)
	 * 
	 * @param _username smtp server username.
	 * @param _password smtp server password.
	 */
    public void login(String _username, String _password) {
        this.m_username = _username;
        this.m_password = _password;
    }

    /**
	 * Establishes a connection with an SMTP server. It can handle
	 * SSL and authenticated connections.
	 * 
	 * @param _host host of smtp server.
	 * @param _port port of stmp server.
	 * @param _isSSL boolean set for SSL connection required.
	 * @param _requiresAuth boolean set for Authentication connection required.
	 */
    public void connect(String _host, int _port, boolean _isSSL, boolean _requiresAuth) {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", _host);
        properties.put("mail.smtp.port", _port);
        properties.put("mail.smtp.starttls.enable", String.valueOf(_isSSL));
        properties.put("mail.smtp.auth", String.valueOf(_requiresAuth));
        this.m_isSSL = _isSSL;
        m_urln = new URLName("smtp", _host, _port, "", m_username, m_password);
        if (_requiresAuth) {
            Authenticator auth = new SMTPAuthenticator();
            m_session = Session.getDefaultInstance(properties, auth);
        } else {
            m_session = Session.getDefaultInstance(properties);
        }
    }

    /**
	 * Sends an email message to the SMTP server.
	 * 
	 * @see EmailMessage
	 * 
	 * @param _message object containing email message attributes.
	 * 
	 * @exception MessagingException if email addresses are not valid.
	 * 
	 * @throws Exception
	 */
    public void send(EmailMessage _message) throws Exception {
        Message _mailMessage = new MimeMessage(m_session);
        InternetAddress[] addresses = null;
        try {
            if (_message.getTo() != null) {
                String _to_address = _message.getTo();
                addresses = InternetAddress.parse(_to_address);
                _mailMessage.setRecipients(Message.RecipientType.TO, addresses);
            } else {
                throw new MessagingException("The mail message requires" + " valid 'To' address(es).");
            }
            if (_message.getFrom() != null) {
                if (_message.getFromName() != null) _mailMessage.setFrom(new InternetAddress(_message.getFrom(), _message.getFromName())); else _mailMessage.setFrom(new InternetAddress(_message.getFrom()));
            } else {
                throw new MessagingException("The mail message requires a" + " valid 'From' address.");
            }
            if (_message.getReplyTo() != null) {
                addresses = InternetAddress.parse(_message.getReplyTo());
                _mailMessage.setReplyTo(addresses);
            }
            if (_message.getBcc() != null) {
                addresses = InternetAddress.parse(_message.getBcc());
                _mailMessage.setRecipients(Message.RecipientType.BCC, addresses);
            }
            if (_message.getSubject() != null) _mailMessage.setSubject(_message.getSubject());
            if (_message.getContent() != null) _mailMessage.setContent(_message.getContent(), _message.getContentType());
            SMTPTransport transport;
            if (m_isSSL) transport = new SMTPSSLTransport(m_session, m_urln); else transport = new SMTPTransport(m_session, m_urln);
            transport.setStartTLS(m_isSSL);
            transport.send(_mailMessage);
            transport.close();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
	 * SimpleAuthenticator is used to do simple authentication when the SMTP
	 * server requires it.
	 */
    private class SMTPAuthenticator extends Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(m_username, m_password);
        }
    }
}
