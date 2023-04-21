package src.multiplayer.email;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import src.eleconics.Utilities;
import src.eleconics.timing.ProgressReporter;

public class EmailMime implements ProgressReporter {

    private boolean debug = false;

    private Properties props;

    private Session session;

    private int progress = 0;

    private String message = "";

    private boolean done = false;

    private boolean failed = false;

    private boolean stop = false;

    private String sender;

    private String recipient;

    private String subject;

    private String messageBody;

    private String attachmentFilename;

    private String mailServer;

    private int serverPort;

    private boolean ssl;

    private String username;

    private String password;

    private boolean deleteFileOnCompletion;

    public EmailMime(String sender, String recipient, String subject, String messageBody, String attachmentFilename, String mailServer, int serverPort, boolean ssl, String username, String password, boolean deleteFileOnCompletion, boolean debug) {
        props = System.getProperties();
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.messageBody = messageBody;
        this.attachmentFilename = attachmentFilename;
        this.mailServer = mailServer;
        this.serverPort = serverPort;
        this.ssl = ssl;
        this.username = username;
        this.password = password;
        this.deleteFileOnCompletion = deleteFileOnCompletion;
        this.debug = debug;
    }

    private void sendMail() {
        progress++;
        message = "Setting properties";
        String protocol;
        if (ssl) {
            protocol = "smtps";
        } else {
            protocol = "smtp";
        }
        props.put("mail." + protocol + ".host", mailServer);
        if (serverPort != 0) {
            props.put("mail." + protocol + ".port", Integer.toString(serverPort));
        }
        if (username != null) {
            props.put("mail." + protocol + ".user", username);
            props.put("mail." + protocol + ".auth", "true");
        } else {
            props.put("mail." + protocol + ".user", sender);
        }
        if (stop) return;
        progress++;
        message = "Creating session";
        if (username != null) {
            session = Session.getInstance(props, new EmailAuthenticator(username, password));
        } else {
            session = Session.getInstance(props);
        }
        session.setDebug(debug);
        if (stop) return;
        progress++;
        try {
            message = "Creating message";
            MimeMessage msg = createMessage();
            if (stop) return;
            progress++;
            message = "Getting transport";
            Transport transport = session.getTransport(protocol);
            if (stop) return;
            progress++;
            message = "Connecting to SMTP server";
            if (username != null) {
                transport.connect(mailServer, username, password);
            } else {
                transport.connect();
            }
            if (stop) return;
            progress++;
            message = "Sending message";
            transport.sendMessage(msg, msg.getAllRecipients());
            progress++;
            message = "Closing connection";
            transport.close();
            progress++;
            if (deleteFileOnCompletion) {
                message = "Cleaning up";
                new File(attachmentFilename).delete();
            }
            done = true;
        } catch (SendFailedException e) {
            done = true;
            failed = true;
            Utilities.popUp("Failed to send message: " + e.getMessage());
        } catch (MessagingException e) {
            done = true;
            failed = true;
            Utilities.popUp("Failed to send message.");
        }
    }

    private MimeMessage createMessage() throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sender));
        InternetAddress[] address = { new InternetAddress(recipient) };
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText(messageBody);
        MimeBodyPart mbp2 = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(attachmentFilename);
        mbp2.setDataHandler(new DataHandler(fds));
        mbp2.setFileName(fds.getName());
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        mp.addBodyPart(mbp2);
        msg.setContent(mp);
        msg.setSentDate(new Date());
        return msg;
    }

    public int getTaskLength() {
        return 8;
    }

    public int getProgress() {
        return progress;
    }

    public String getMessage() {
        return message;
    }

    public boolean isDone() {
        return done;
    }

    public boolean failed() {
        return failed;
    }

    public void stop() {
        stop = true;
    }

    public void run() {
        sendMail();
    }
}
