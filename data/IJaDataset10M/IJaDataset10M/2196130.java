package net.sf.jabs.notification;

import org.apache.commons.mail.EmailException;

public interface NotificationEmail {

    public static final String DEFAULT_MAIL_FROM = "noreply";

    public static final String DEFAULT_SMTP_HOST = "mail";

    public void prepare() throws Exception;

    public void send() throws EmailException;

    public StringBuilder getBody();
}
