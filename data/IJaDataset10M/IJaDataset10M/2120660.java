package com.buenocode.androidxmpp.contacts;

import java.io.Serializable;

public class JidContact implements Serializable {

    private static final long serialVersionUID = -5369218714405257765L;

    private String id;

    private String displayName;

    private String number;

    private JidStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public JidStatus getStatus() {
        return status;
    }

    public void setStatus(JidStatus status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : Integer.MIN_VALUE;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
