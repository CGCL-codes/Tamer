package ossobook.client.io.mail;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import ossobook.Messages;
import ossobook.client.util.Configuration;

/**
 * class to send conflict mails
 * 
 * @author j.lamprecht
 * 
 */
public class Mail {

    private final String[] recipients;

    private final String projectName;

    private final String server;

    private final String password;

    private final String username;

    private final String sender;

    /**
	 * initialize mail to send
	 * 
	 * @param recipients
	 *            - project owner of given project
	 * @param projectName
	 */
    public Mail(String[] recipients, String projectName) {
        server = Configuration.config.getProperty("mail.server");
        username = Configuration.config.getProperty("mail.username");
        password = Configuration.config.getProperty("mail.password");
        sender = Configuration.config.getProperty("mail.sender");
        this.recipients = recipients;
        this.projectName = projectName;
    }

    /**
	 * send conflict mail, append given conflict file
	 * 
	 * @param conflictFile
	 *            - file that contains occurred conflicts
	 * @throws MessagingException 
	 */
    public void sendConflictMail(File conflictFile) throws MessagingException {
        String warningMessage = Messages.getString("Mail.0", projectName);
        Properties props = new Properties();
        props.put("mail.smtp.host", server);
        if (!username.equals("") || !password.equals("")) {
            props.put("mail.smtp.auth", "true");
        }
        Session s = Session.getInstance(props, null);
        Message message = new MimeMessage(s);
        InternetAddress from = new InternetAddress(sender);
        message.setFrom(from);
        for (String recipient : recipients) {
            InternetAddress to = new InternetAddress(recipient);
            message.addRecipient(Message.RecipientType.TO, to);
        }
        message.setSubject(Messages.getString("Mail.1", projectName));
        MimeMultipart content = new MimeMultipart();
        MimeBodyPart messageText = new MimeBodyPart();
        messageText.setContent(warningMessage, "text/plain");
        content.addBodyPart(messageText);
        MimeBodyPart file = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(conflictFile);
        file.setDataHandler(new DataHandler(fds));
        file.setFileName(fds.getName());
        content.addBodyPart(file);
        message.setContent(content, "multipart/mixed");
        Transport transport = s.getTransport("smtp");
        transport.connect(server, username, password);
        message.saveChanges();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
