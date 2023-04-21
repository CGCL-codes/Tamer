package org.eiichiro.monophony;

public class Email {

    private final String email;

    public Email(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return email;
    }
}
