package fhire.utils;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import fhire.language.Language;

/**
 * Class which handles the mail sendings.
 * 
 * @author Joerg Doppelreiter, Johann Zagler, Robert Maierhofer
 *
 */
public class MailSender {

    /**
	 * Mail: SMTP - Server
	 */
    public static final String SMTP_SERVER = "smtp.fh-joanneum.at";

    /**
     * Mail: Mail From
     */
    public static final String MAIL_FROM = "fhire@fh-joanneum.at";

    /**
     * Sends an Email.
     * 
     * @param mailTo
     * @param mailText
     */
    public static void sendMail(String mailTo, String mailText) {
        try {
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", SMTP_SERVER);
            Session session = Session.getDefaultInstance(properties, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo, false));
            message.setSubject(Language.getText("email-subject-register", Language.GERMAN));
            message.setText(mailText);
            message.setHeader("X-Mailer", "FHire");
            message.setSentDate(new Date());
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
