package com.jc.communication.email;

import javax.mail.PasswordAuthentication;

/**
 * @author Jeff Matthes
 *
 */
public class JcMailAuthenticator extends javax.mail.Authenticator {

    String username;

    String password;

    /**
	 * @param username
	 * @param password
	 */
    public JcMailAuthenticator(String usernameIn, String passwordIn) {
        username = usernameIn;
        password = passwordIn;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
