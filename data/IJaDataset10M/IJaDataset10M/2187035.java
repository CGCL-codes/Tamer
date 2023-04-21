package it.infodea.tapestrydea.services.mail.impl;

import it.infodea.tapestrydea.MailObject;
import it.infodea.tapestrydea.services.mail.MailSender;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.tapestry5.ioc.internal.util.TapestryException;

public class MailSenderImpl implements MailSender {

    private final Properties props;

    private final Session session;

    public MailSenderImpl(Collection<Properties> propsColl) {
        Object[] array = propsColl.toArray();
        this.props = (Properties) array[array.length - 1];
        this.session = init();
    }

    private Session init() {
        String auth = props.getProperty("mail.smtp.auth");
        if ("true".equals(auth)) {
            Authenticator authenticator = new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    String user = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    PasswordAuthentication passwordAuthentication = new PasswordAuthentication(user, password);
                    return passwordAuthentication;
                }
            };
            return Session.getDefaultInstance(props, authenticator);
        } else {
            return Session.getDefaultInstance(props);
        }
    }

    public void sendMail(String sender, String to, String cc, String ccn, String subject, String text, List<String> attachments) throws AddressException, MessagingException {
        MailObject mailObject = new MailObject();
        mailObject.setSender(sender);
        mailObject.setTo(to);
        mailObject.setCc(cc);
        mailObject.setCcn(ccn);
        mailObject.setSubject(subject);
        mailObject.setText(text);
        mailObject.setAttachments(attachments);
        sendMail(mailObject);
    }

    public void sendMail(MailObject mailObject) throws AddressException, MessagingException {
        MimeMessage msg = new MimeMessage(session);
        initSender(mailObject, msg);
        initTo(mailObject, msg);
        initCC(mailObject, msg);
        initCCN(mailObject, msg);
        initSubject(mailObject, msg);
        MimeBodyPart messageBodyPart = initAttachments(mailObject, msg);
        initBody(mailObject, messageBodyPart);
        Transport.send(msg);
    }

    private void initBody(MailObject mailObject, MimeBodyPart messageBodyPart) throws MessagingException {
        if (mailObject.getText() != null) {
            messageBodyPart.setText(mailObject.getText());
        } else {
            messageBodyPart.setText("");
        }
    }

    private MimeBodyPart initAttachments(MailObject mailObject, MimeMessage msg) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        addAttachments(multipart, mailObject.getAttachments());
        msg.setContent(multipart);
        return messageBodyPart;
    }

    private void initSubject(MailObject mailObject, MimeMessage msg) throws MessagingException {
        if (mailObject.getSubject() != null) {
            msg.setSubject(mailObject.getSubject());
        }
    }

    private void initCCN(MailObject mailObject, MimeMessage msg) throws AddressException, MessagingException {
        if (mailObject.getCcn() != null) {
            InternetAddress[] ccnAddrs = InternetAddress.parse(mailObject.getCcn(), false);
            msg.setRecipients(Message.RecipientType.BCC, ccnAddrs);
        }
    }

    private void initCC(MailObject mailObject, MimeMessage msg) throws AddressException, MessagingException {
        if (mailObject.getCc() != null) {
            InternetAddress[] ccAddrs = InternetAddress.parse(mailObject.getCc(), false);
            msg.setRecipients(Message.RecipientType.CC, ccAddrs);
        }
    }

    private void initTo(MailObject mailObject, MimeMessage msg) throws AddressException, MessagingException {
        if (mailObject.getTo() != null) {
            InternetAddress[] toAddrs = InternetAddress.parse(mailObject.getTo());
            msg.setRecipients(Message.RecipientType.TO, toAddrs);
        } else {
            throw new TapestryException("No recipient address specified", null);
        }
    }

    private void initSender(MailObject mailObject, MimeMessage msg) throws MessagingException, AddressException {
        if (mailObject.getSender() != null) {
            msg.setFrom(new InternetAddress(mailObject.getSender()));
        } else {
            throw new TapestryException("No sender address specified", null);
        }
    }

    private void addAttachments(Multipart multipart, List<String> paths) throws MessagingException {
        MimeBodyPart messageBodyPart = null;
        DataSource source = null;
        if (paths != null) {
            for (String path : paths) {
                messageBodyPart = new MimeBodyPart();
                source = new FileDataSource(path);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(path.substring(path.lastIndexOf("\\")));
                multipart.addBodyPart(messageBodyPart);
            }
        }
    }
}
