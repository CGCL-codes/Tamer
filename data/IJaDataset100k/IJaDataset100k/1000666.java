package org.light.portlets.message.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Index;
import org.light.portal.core.model.Entity;
import org.light.portal.user.model.User;
import org.light.portal.util.DateUtil;
import org.light.portal.util.ImageUtil;

/**
 * 
 * @author Jianmin Liu
 **/
@javax.persistence.Entity
@Table(name = "light_message")
public class Message extends Entity {

    private static final long serialVersionUID = -5374386476439081454L;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @Index(name = "index_userId")
    private User user;

    @ManyToOne
    @Index(name = "index_postById")
    @JoinColumn(name = "postById", nullable = false)
    private User sender;

    @Column
    private String subject;

    @Column
    private String content;

    @Column
    private int format;

    @Column
    private int direction;

    @Column
    private int status;

    @Column
    private int type;

    @Column
    private int event;

    @Column
    private long eventId;

    public Message() {
        super();
    }

    public Message(String subject, String content, User user, User sender, long orgId) {
        this();
        this.subject = subject;
        this.content = content;
        this.user = user;
        this.sender = sender;
        this.setOrgId(orgId);
    }

    public Message(String subject, String content, User user, User sender, long orgId, int format) {
        this(subject, content, user, sender, orgId);
        this.format = format;
    }

    public Message(String subject, String content, User user, User sender, long orgId, int format, int direction) {
        this(subject, content, user, sender, orgId, format);
        this.direction = direction;
    }

    public Message(String subject, String content, User user, User sender, long orgId, int type, int event, long eventId) {
        this(subject, content, user, sender, orgId);
        this.type = type;
        this.event = event;
        this.eventId = eventId;
    }

    public Message(String subject, String content, User user, User sender, long orgId, int format, int type, int event, long eventId) {
        this(subject, content, user, sender, orgId, type, event, eventId);
        this.format = format;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getDate() {
        return DateUtil.format(this.getCreateDate(), "EEE, MMM dd, yyyy");
    }

    public String getFullDate() {
        return DateUtil.format(this.getCreateDate(), "EEEE, MMMM dd, yyyy HH:mm aaa");
    }

    public String getFromPhotoUrl() {
        return ImageUtil.getPhotoUrl(this.sender.getPhotoUrl());
    }

    public String getToPhotoUrl() {
        return ImageUtil.getPhotoUrl(this.user.getPhotoUrl());
    }

    public String getFromUri() {
        return this.sender.getUri();
    }

    public String getToUri() {
        return this.user.getUri();
    }

    public long getPostById() {
        return this.sender.getId();
    }

    public long getUserId() {
        return this.user.getId();
    }

    public String getFromDisplayName() {
        return this.sender.getDisplayName();
    }

    public String getToDisplayName() {
        return this.user.getDisplayName();
    }

    public int getFromCurrentStatusId() {
        return this.sender.getCurrentStatus();
    }

    public int getToCurrentStatusId() {
        return this.user.getCurrentStatus();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
