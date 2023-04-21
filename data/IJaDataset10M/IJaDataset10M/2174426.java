package ch.ethz.dcg.spamato.stats.server.mail;

import javax.mail.MessagingException;
import ch.ethz.dcg.spamato.stats.server.StatServerSettings;
import ch.ethz.dcg.thread.*;

/**
 * Provides the functionality to send EMail messages by calling a simple method.
 * The EarlgreyServerMailSender is threadsafe and sends one message after the
 * other (in the order of the arrival of the assignments).
 * 
 * @author simon schlachter
 * @author keno - ThreadPool
 */
public class MailSender {

    private static final MailSender theInstance = new MailSender();

    private final SendMail sendMail = new SendMail(StatServerSettings.getInstance().getSetting("mail.server.smtp"));

    private BufferedThreadPool threadPool = new BufferedThreadPool(1);

    public static MailSender getInstance() {
        return theInstance;
    }

    public void sendEmail(String receiver, String subject, String message) {
        threadPool.handleTask(new EmailTask(receiver, subject, message));
    }

    /**
	 * Send a message to <code>receiver</code>.
	 */
    void sendMessage(String receiver, String subject, String message) {
        try {
            sendMail.sendMail(StatServerSettings.getInstance().getSetting("mail.sender.name"), StatServerSettings.getInstance().getSetting("mail.sender.address"), null, receiver, subject, message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Class to hold all information needed to send a simple text to a user.
 * 
 * @author keno
 */
class EmailTask extends AbstractTask {

    private String receiver;

    private String subject;

    private String message;

    public EmailTask(String receiver, String subject, String message) {
        this.receiver = receiver;
        this.subject = subject;
        this.message = message;
    }

    public void doTask() {
        MailSender.getInstance().sendMessage(receiver, subject, message);
    }
}
