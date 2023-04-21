package org.ezfusion.tools;

import javax.mail.PasswordAuthentication;

/**
 * SimpleAuthenticator is used to do simple authentication
 * when the SMTP server requires it.
 */
public class SMTPAuthenticator extends javax.mail.Authenticator {

    private final String SMTP_AUTH_USER = "ezfusion.bug.tracker@gmail.com";

    private final String SMTP_AUTH_PWD = "sourceforge";

    public PasswordAuthentication getPasswordAuthentication() {
        String username = SMTP_AUTH_USER;
        String password = SMTP_AUTH_PWD;
        return new PasswordAuthentication(username, password);
    }
}
