package com.alesj.newsfeed.data;

import java.io.Serializable;
import com.generalynx.common.data.IEntity;

public class MessageLog implements IEntity, Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private Message message;

    private Contact contact;

    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
