package com.jxva.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2009-04-20 11:31:41 by Jxva
 */
public class SimpleAuthenticator extends Authenticator {

    private String username;

    private String password;

    public SimpleAuthenticator(String user, String password) {
        this.username = user;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
