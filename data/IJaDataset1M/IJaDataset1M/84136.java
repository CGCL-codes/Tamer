package de.die_crowd.model;

import java.net.InetAddress;
import java.util.Date;

public class AbstractContent {

    long contentId;

    long userId;

    Date creation;

    InetAddress ip;

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }
}
