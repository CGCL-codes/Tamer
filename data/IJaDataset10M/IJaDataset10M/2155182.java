package org.esk.dablog.gwt.dablog.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 * This class 
 * User: esk
 * Date: 05.01.2007
 * Time: 21:57:33
 * $Id:$
 */
public class ClientForumPostEntry implements ForumEntry, IsSerializable {

    private long id;

    private String header;

    private String text;

    private String authorUsername;

    private long authorId;

    private Date date;

    public boolean hideHeaderOnExpand() {
        return false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
