package com.ma.j2mesync.google;

import java.util.Vector;

/**
 *
 * @author ma
 */
public class GoogleContact {

    String title;

    String id;

    String updated;

    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    Vector emailAddresses = new Vector();

    Vector addresses = new Vector();

    Vector phoneNumbers = new Vector();

    public Vector getAddresses() {
        return addresses;
    }

    public void setAddresses(Vector addresses) {
        this.addresses = addresses;
    }

    public Vector getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(Vector emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vector getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Vector phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public boolean equals(Object other) {
        if (other instanceof GoogleContact) {
            GoogleContact gOther = (GoogleContact) other;
            if (gOther != null && gOther.getTitle() != null && gOther.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }
}
