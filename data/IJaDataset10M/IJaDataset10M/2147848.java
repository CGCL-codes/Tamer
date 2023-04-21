package org.softnetwork.mail;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.softnetwork.log.Log4jConnector;
import org.softnetwork.mail.event.MailDeliveryEvent;
import org.softnetwork.mail.event.MailDeliveryEventListener;
import org.softnetwork.mail.event.MailErrorEvent;
import org.softnetwork.mail.event.MailEvent;
import org.softnetwork.mail.event.MailServerErrorEvent;
import org.softnetwork.mail.event.MailServerEventListener;
import org.softnetwork.mail.event.MailServerStartEvent;
import org.softnetwork.mail.event.MailServerStopEvent;
import org.softnetwork.threads.ObjectEvent;
import org.softnetwork.threads.ObjectListener;

/**
 * @author $Author$
 *
 * @version $Revision$
 */
class SMTPMailServer implements ObjectListener, MailServer {

    private String _smtpHost;

    private String login;

    private String pwd;

    private Properties smtpProps;

    private Transport smtp;

    private Session session;

    private boolean debug = false;

    private Vector mailListeners;

    private Vector serverListeners;

    private MailConsumer consumer;

    SMTPMailServer(String _smtpHost) throws NoSuchProviderException {
        this(_smtpHost, "", "", false);
    }

    SMTPMailServer(String _smtpHost, boolean debug) throws NoSuchProviderException {
        this(_smtpHost, "", "", debug);
    }

    SMTPMailServer(String _smtpHost, String login, String pwd) throws NoSuchProviderException {
        this(_smtpHost, login, pwd, false);
    }

    SMTPMailServer(String _smtpHost, String login, String pwd, boolean debug) throws NoSuchProviderException {
        Runtime.getRuntime().addShutdownHook(new MailServerShutdownHook(this));
        this._smtpHost = _smtpHost;
        this.login = login;
        this.pwd = pwd;
        this.debug = debug;
        startServer();
    }

    public void addMailServerEventListener(MailServerEventListener listener) {
        synchronized (serverListeners) {
            if (!serverListeners.contains(listener)) {
                serverListeners.add(listener);
            }
        }
    }

    public void removeMailServerEventListener(MailServerEventListener listener) {
        synchronized (serverListeners) {
            if (serverListeners.contains(listener)) {
                serverListeners.remove(listener);
            }
        }
    }

    public void addMailDeliveryEventListener(MailDeliveryEventListener listener) {
        synchronized (mailListeners) {
            if (!mailListeners.contains(listener)) {
                mailListeners.add(listener);
                addMailServerEventListener(listener);
            }
        }
    }

    public void removeMailDeliveryEventListener(MailDeliveryEventListener listener) {
        synchronized (mailListeners) {
            if (mailListeners.contains(listener)) {
                mailListeners.remove(listener);
                removeMailServerEventListener(listener);
            }
        }
    }

    void startServer() throws NoSuchProviderException {
        smtpProps = new Properties();
        smtpProps.put("mail.smtp.host", _smtpHost);
        mailListeners = new Vector();
        serverListeners = new Vector();
        session = Session.getDefaultInstance(smtpProps, null);
        session.setDebug(debug);
        smtp = session.getTransport(SMTP_MAIL);
        consumer = new MailConsumer(this, debug);
    }

    public void addMail(MailBean mail) {
        addMail(null, mail);
    }

    public void addMail(MailSender sender, MailBean mail) {
        consumer.addQueueElement(new MailInfos(sender, mail));
    }

    public void stopServer() {
        if (consumer != null) consumer.stopQueue();
        disconnect();
    }

    public void consume(ObjectEvent event) {
        if (event instanceof MailEvent) {
            new MailAgent(this, ((MailEvent) event).getMailSender(), ((MailEvent) event).getMail());
        }
    }

    void connect() {
        try {
            synchronized (smtp) {
                if (!smtp.isConnected()) {
                    smtp.connect(_smtpHost, login, pwd);
                    synchronized (serverListeners) {
                        Enumeration num = serverListeners.elements();
                        while (num.hasMoreElements()) {
                            MailServerEventListener listener = (MailServerEventListener) num.nextElement();
                            listener.mailServerStarted(new MailServerStartEvent(this));
                        }
                    }
                }
            }
        } catch (MessagingException ex) {
            synchronized (serverListeners) {
                Enumeration num = serverListeners.elements();
                while (num.hasMoreElements()) {
                    MailServerEventListener listener = (MailServerEventListener) num.nextElement();
                    listener.mailServerErrorOccured(new MailServerErrorEvent(this, ex));
                }
            }
        }
    }

    void disconnect() {
        try {
            synchronized (smtp) {
                if (smtp.isConnected()) {
                    smtp.close();
                    synchronized (serverListeners) {
                        Enumeration num = serverListeners.elements();
                        while (num.hasMoreElements()) {
                            MailServerEventListener listener = (MailServerEventListener) num.nextElement();
                            listener.mailServerStopped(new MailServerStopEvent(this));
                        }
                    }
                }
            }
        } catch (MessagingException ex) {
            synchronized (serverListeners) {
                Enumeration num = serverListeners.elements();
                while (num.hasMoreElements()) {
                    MailServerEventListener listener = (MailServerEventListener) num.nextElement();
                    listener.mailServerErrorOccured(new MailServerErrorEvent(this, ex));
                }
            }
        }
    }

    class MailAgent implements Runnable {

        Thread runner;

        MailBean mail;

        MailServer mailServer;

        MailSender mailSender;

        MailAgent(MailServer mailServer, MailSender mailSender, MailBean mail) {
            this.mailServer = mailServer;
            this.mailSender = mailSender;
            this.mail = mail;
            runner = new Thread(this);
            runner.start();
        }

        public void run() {
            try {
                Message msg = new MimeMessage(session);
                InternetAddress addressFrom = new InternetAddress(mail.getMailFrom());
                msg.setFrom(addressFrom);
                int size = 0;
                int i = 0;
                String[] mailTos = mail.getMailTos();
                size = mailTos.length;
                if (size > 0) {
                    InternetAddress[] addressTos = new InternetAddress[size];
                    for (i = 0; i < size; i++) {
                        addressTos[i] = new InternetAddress(mailTos[i]);
                    }
                    msg.setRecipients(Message.RecipientType.TO, addressTos);
                }
                String[] mailToCcs = mail.getMailToCcs();
                size = mailToCcs.length;
                if (size > 0) {
                    InternetAddress[] addressToCcs = new InternetAddress[size];
                    for (i = 0; i < size; i++) {
                        addressToCcs[i] = new InternetAddress(mailToCcs[i]);
                    }
                    msg.setRecipients(Message.RecipientType.CC, addressToCcs);
                }
                String[] mailToBccs = mail.getMailToBccs();
                size = mailToBccs.length;
                if (size > 0) {
                    InternetAddress[] addressToBccs = new InternetAddress[size];
                    for (i = 0; i < size; i++) {
                        addressToBccs[i] = new InternetAddress(mailToBccs[i]);
                    }
                    msg.setRecipients(Message.RecipientType.BCC, addressToBccs);
                }
                msg.setSubject(mail.getMailSubject());
                FileDataSource[] atts = mail.getMailAtts();
                size = atts.length;
                if (size > 0) {
                    Multipart _mp = new MimeMultipart();
                    MimeBodyPart _mbp = new MimeBodyPart();
                    _mbp.setContent(mail.getMailMessage(), mail.getMailContentsType());
                    Enumeration headers = _mbp.getAllHeaderLines();
                    while (headers.hasMoreElements()) {
                        Log4jConnector.getConsole().debug(headers.nextElement());
                    }
                    _mp.addBodyPart(_mbp);
                    for (i = 0; i < size; i++) {
                        FileDataSource fds = atts[i];
                        _mbp = new MimeBodyPart();
                        _mbp.setDataHandler(new DataHandler(fds));
                        _mbp.setFileName(fds.getName());
                        _mp.addBodyPart(_mbp);
                    }
                    msg.setContent(_mp);
                } else {
                    msg.setContent(mail.getMailMessage(), mail.getMailContentsType());
                }
                Enumeration headers = mail.getHeaderNames();
                while (headers.hasMoreElements()) {
                    String name = (String) headers.nextElement();
                    msg.addHeader(name, mail.getHeaderValue(name));
                }
                msg.saveChanges();
                synchronized (smtp) {
                    connect();
                    smtp.sendMessage(msg, msg.getAllRecipients());
                }
                Enumeration num = mailListeners.elements();
                while (num.hasMoreElements()) {
                    MailDeliveryEventListener listener = (MailDeliveryEventListener) num.nextElement();
                    listener.mailDelivered(new MailDeliveryEvent(mailServer, mailSender, mail));
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
                Enumeration num = mailListeners.elements();
                while (num.hasMoreElements()) {
                    MailDeliveryEventListener listener = (MailDeliveryEventListener) num.nextElement();
                    listener.mailErrorOccured(new MailErrorEvent(mailServer, mailSender, mail, ex));
                }
            }
        }
    }
}
