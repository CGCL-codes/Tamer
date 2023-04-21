package net.spatula.tally_ho.service.beans;

import java.io.Serializable;
import java.util.Date;

public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String email;

    private boolean verified;

    private String passwordQuestion;

    private String passwordAnswer;

    private String uid;

    private Date premiumUntil;

    private String credential;

    public static enum Authenticator {

        SERVICE, AUTOLOGIN, NONE
    }

    ;

    private Authenticator authenticator;

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordAnswer() {
        return passwordAnswer;
    }

    public void setPasswordAnswer(String passwordAnswer) {
        this.passwordAnswer = passwordAnswer;
    }

    public String getPasswordQuestion() {
        return passwordQuestion;
    }

    public void setPasswordQuestion(String passwordQuestion) {
        this.passwordQuestion = passwordQuestion;
    }

    public Date getPremiumUntil() {
        return premiumUntil;
    }

    public void setPremiumUntil(Date premiumUntil) {
        this.premiumUntil = premiumUntil;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public boolean isAuthenticated() {
        return credential != null;
    }
}
