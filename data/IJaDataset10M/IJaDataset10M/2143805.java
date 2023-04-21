package com.cnc.mediaconnect1.request;

public class RecordRequest {

    private String id;

    private boolean isContact;

    private String token;

    public RecordRequest(String id, boolean isContact, String token) {
        super();
        this.id = id;
        this.isContact = isContact;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isContact() {
        return isContact;
    }

    public void setContact(boolean isContact) {
        this.isContact = isContact;
    }
}
