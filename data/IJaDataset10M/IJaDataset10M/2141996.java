package net.codesmarts.log4j;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * Base class for appenders that file bug reports as emails to specified addresses
 * @author Fred McCann
 */
public abstract class AbstractEmailBugReportAppender extends AbstractBugReportAppender {

    private String to;

    private String from;

    private String smtpServer;

    private String subjectPrepend = "";

    private boolean html = false;

    /**
     * Implementing class decides how to create email message body
     * @param report
     * @return
     */
    public abstract String getMessage(BugReport report);

    /**
     * Implementing class decides how to create email message body
     * @param report
     * @return
     */
    public String getHTMLMessage(BugReport report) {
        return null;
    }

    /**
     * Implementing class decides how to create email subject
     * @param report
     * @return
     */
    public abstract String getSubject(BugReport report);

    /**
     * @see net.codesmarts.log4j.BugReportAppender#append(net.codesmarts.log4j.BugReport)
     */
    public synchronized void append(BugReport report) {
        String[] addresses = to.split(",");
        String subject = subjectPrepend + getSubject(report);
        for (int x = 0; x < addresses.length; x++) {
            if (html == true) {
                try {
                    HtmlEmail email = new HtmlEmail();
                    email.setHostName(smtpServer);
                    email.setFrom(from);
                    email.addTo(addresses[x]);
                    email.setSubject(subject);
                    String htmlMessage = getHTMLMessage(report);
                    if (htmlMessage != null) email.setHtmlMsg(htmlMessage);
                    email.setMsg(getMessage(report));
                    email.send();
                } catch (EmailException e) {
                    getErrorHandler().error("Could not send message: " + subject + " to: " + addresses[x] + " from: " + from + ". " + e.getMessage());
                }
            } else {
                try {
                    SimpleEmail email = new SimpleEmail();
                    email.setHostName(smtpServer);
                    email.setFrom(from);
                    email.addTo(addresses[x]);
                    email.setSubject(subject);
                    email.setMsg(getMessage(report));
                    email.send();
                } catch (EmailException e) {
                    getErrorHandler().error("Could not send message: " + subject + " to: " + addresses[x] + " from: " + from + ". " + e.getMessage());
                }
            }
        }
    }

    /**
     * @see net.codesmarts.log4j.AbstractBugReportAppender#init()
     */
    public void init() {
        boolean good = true;
        if (smtpServer == null) {
            getErrorHandler().error("Must set SMTP server for " + this.getClass().getName());
            good = false;
        }
        if (from == null) {
            getErrorHandler().error("Must set \"from\" email address for " + this.getClass().getName());
            good = false;
        }
        if (to == null) {
            getErrorHandler().error("Must set \"to\" email address for " + this.getClass().getName());
            good = false;
        }
        if (good = false) close();
    }

    /**
     * Set the email address from which to send messages
     * @param from The from to set.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Set the SMTP server to use for message delivery
     * @param smtpServer The smtpServer to set.
     */
    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    /**
     * Set comma delimited list of message recipients
     * @param to The to to set.
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Set this to true to send messages in HTML format; defaults to false
     * @param html The html to set.
     */
    public void setHtml(boolean html) {
        this.html = html;
    }

    /**
     * Set optional string to prepend to all messages
     * @param subjectPrepend The subjectPrepend to set.
     */
    public void setSubjectPrepend(String subjectPrepend) {
        this.subjectPrepend = subjectPrepend;
    }
}
