package com.lightattachment.stats;

import java.io.IOException;
import java.io.Writer;
import java.net.SocketException;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.log4j.Logger;
import com.lightattachment.mails.LightAttachment;
import com.lightattachment.mails.StringOutputStream;

/** Send an activity report. */
public class SendReportThread extends StoppableThread {

    /** From who the report must be sent. */
    private String from;

    /** To who the report must be sent. */
    private String to;

    /** The SMTP server to use. */
    private String smtp;

    /** Message body. */
    private String body;

    /** Message subject. */
    private String subject;

    /** Report file name. */
    private String filename;

    /** Report ID. */
    private int code;

    /** Logger used to trace activity. */
    static Logger log = Logger.getLogger(SendReportThread.class);

    /** Build a <code>SendReportThread</code>.
	 * @param from From who the report must be sent
	 * @param to To who the report must be sent
	 * @param body Message body
	 * @param subject Message subject
	 * @param smtp The SMTP server to use
	 * @param filenames Report file name
	 * @param code Report ID */
    public SendReportThread(String from, String to, String body, String subject, String smtp, String filenames, int code) {
        super();
        this.from = from;
        this.to = to;
        this.filename = filenames;
        this.smtp = smtp;
        this.body = body;
        this.subject = subject;
        this.code = code;
    }

    public void run() {
        super.run();
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.localhost", "vmsource.e-logiq.net");
        Session session = Session.getDefaultInstance(props, null);
        try {
            send(session);
            setDone(true);
        } catch (AddressException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (SocketException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }

    /** Sends the report. */
    public void send(Session session) throws AddressException, MessagingException, SocketException, IOException {
        Message message = new MimeMessage(session);
        log.info("Building report message " + code);
        System.out.println("Building report message " + code);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        if (filename != null) {
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDisposition("attachment");
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            if (filename.indexOf("\\") > -1) {
                filename = filename.substring(filename.lastIndexOf("\\") + 1);
            } else {
                filename = filename.substring(filename.lastIndexOf("/") + 1);
            }
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
        }
        message.setContent(multipart);
        int reply;
        SMTPClient client = new SMTPClient();
        client.connect(LightAttachment.config.getString("report.smtp"), 25);
        reply = client.getReplyCode();
        if (!SMTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            log.warn("Fail to send activity report");
        } else {
            if (client.login(LightAttachment.config.getString("hostname"))) {
                boolean tok = client.setSender(LightAttachment.config.getString("report.mailfrom"));
                StringTokenizer st = new StringTokenizer(LightAttachment.config.getString("report.mailto"), " ");
                while (st.hasMoreTokens()) {
                    String rcpt = st.nextToken();
                    if (!client.addRecipient(rcpt)) tok = false;
                }
                if (tok) {
                    Writer w = client.sendMessageData();
                    if (w != null) {
                        StringOutputStream sos = new StringOutputStream(new StringBuffer());
                        message.writeTo(sos);
                        String m = sos.getString();
                        w.write(m);
                        w.close();
                        log.info("Activity report delivered to " + LightAttachment.config.getString("report.smtp"));
                        System.out.println("Activity report  delivered to " + LightAttachment.config.getString("report.smtp"));
                        if (!client.completePendingCommand()) {
                            client.disconnect();
                        }
                        client.disconnect();
                    }
                }
            } else {
                client.disconnect();
                log.warn("Fail to send activity report");
            }
        }
    }
}
