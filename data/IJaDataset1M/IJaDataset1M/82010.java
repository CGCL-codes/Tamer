package GUI;

import java.net.PasswordAuthentication;

public class SimpleProxyAuthenticator extends java.net.Authenticator {

    private String username;

    private String password;

    public SimpleProxyAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.username, this.password.toCharArray());
    }
}
