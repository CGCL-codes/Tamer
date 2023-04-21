package org.garret.ptl.core;

import java.util.Date;
import java.io.Serializable;

class AnonymousPerson implements IPerson, Serializable {

    private final String login;

    private final String locale;

    AnonymousPerson(String login, String locale) {
        this.login = login;
        this.locale = locale;
    }

    public Date getBirthDate() {
        return new Date(0L);
    }

    public String getAvatar() {
        return null;
    }

    public String getCheckQuestion() {
        return null;
    }

    public String getCheckAnswer() {
        return null;
    }

    public boolean isGuest() {
        return true;
    }

    public boolean isDisabled() {
        return false;
    }

    public Date getRegistrationDate() {
        return new Date(0L);
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public long getId() {
        return 0;
    }
}
