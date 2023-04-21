package org.sourceseed.tools.backup.backThatThingUp;

import java.util.ArrayList;
import java.util.List;
import org.sourceseed.tools.backup.tasks.EmailTrigger;

/**
 * Contains list of who should receive email notifications, what will trigger these <br\>
 * notifications, and some basic information as to what should be included in the email
 * message.
 *
 * @author Dean Del Ponte
 */
public class EmailNotifications {

    private List<String> emailAddresses;

    private EmailTrigger emailTrigger;

    private String from;

    private String subject;

    private String message;

    public EmailNotifications() {
        this(new ArrayList<String>());
    }

    /**
     * 
     * @param emailAddresses List of recipient's email addresses
     */
    public EmailNotifications(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
        setEmailTrigger(EmailTrigger.ALL);
    }

    /**
     * 
     * @return EmailTrigger - contains information as to when emails should<br \>
     * be sent (i.e. on successful backup, on failure, both, etc.)
     */
    public EmailTrigger getEmailTrigger() {
        return emailTrigger;
    }

    /**
     * 
     * @param trigger
     */
    public void setEmailTrigger(EmailTrigger trigger) {
        this.emailTrigger = trigger;
    }

    /**
     * 
     * @return Email address which should be sent notifications for a particular emailTrigger
     */
    public List<String> getEmailAddresses() {
        return emailAddresses;
    }

    /**
     * 
     * @param emailAddresses List of recipient email addresses
     */
    public void setEmailAddresses(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    /**
     * 
     * @return String which should appear in the "From" field of sent email
     */
    public String getFrom() {
        return from;
    }

    /**
     * 
     * @param from String which should appear in the "From" field of sent email
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 
     * @return Subject of email
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 
     * @param subject Text to appear in subject line of sent email
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 
     * @return Content included in body of sent email message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message Content included in body of sent email message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return Returns true attributes of this class are not null, otherwise<br\>
     * returns false
     */
    public boolean isValid() {
        boolean valid = false;
        if (emailAddresses.size() > 0 && emailTrigger != null && from != null && message != null && subject != null) {
            valid = true;
        }
        return valid;
    }
}
