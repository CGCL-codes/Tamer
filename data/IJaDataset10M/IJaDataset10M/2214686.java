package org.jsecurity.samples.spring.web;

/**
 * Command object that parameters are bound to when logging into the sample
 * application.
 *
 * @author Jeremy Haile
 * @since 0.1
 */
public class LoginCommand {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
