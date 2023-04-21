package com.vb.project.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the notifications database table.
 * 
 */
@Entity
@Table(name = "notifications")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    private int forUserId;

    private byte isNotificated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date notificatedDate;

    private String title;

    public Notification() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getForUserId() {
        return this.forUserId;
    }

    public void setForUserId(int forUserId) {
        this.forUserId = forUserId;
    }

    public byte getIsNotificated() {
        return this.isNotificated;
    }

    public void setIsNotificated(byte isNotificated) {
        this.isNotificated = isNotificated;
    }

    public Date getNotificatedDate() {
        return this.notificatedDate;
    }

    public void setNotificatedDate(Date notificatedDate) {
        this.notificatedDate = notificatedDate;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
