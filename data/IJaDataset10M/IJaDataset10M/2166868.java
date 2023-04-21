package com.simconomy.twitter.script.commons.client.model;

import java.io.Serializable;
import java.util.Date;

public class Tweet implements Serializable {

    private Date createdAt;

    private long id;

    private String text;

    private String source;

    private boolean isTruncated;

    private long inReplyToStatusId;

    private int inReplyToUserId;

    private boolean isFavorited;

    private boolean isRetweet = false;

    private String inReplyToScreenName;

    private double latitude = -1;

    private double longitude = -1;

    private TwitterUser user = null;

    private Tweet retweetDetails = null;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(boolean isTruncated) {
        this.isTruncated = isTruncated;
    }

    public long getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    public void setInReplyToStatusId(long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    public int getInReplyToUserId() {
        return inReplyToUserId;
    }

    public void setInReplyToUserId(int inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public String getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    public void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public TwitterUser getUser() {
        return user;
    }

    public void setUser(TwitterUser user) {
        this.user = user;
    }

    public void setRetweet(Tweet retweetDetails) {
        this.retweetDetails = retweetDetails;
        this.isRetweet = retweetDetails != null;
    }

    public void setRetweet(boolean isRetweet) {
        this.isRetweet = isRetweet;
    }

    public boolean isRetweet() {
        return isRetweet;
    }
}
