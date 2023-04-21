package com.google.wireless.gdata.subscribedfeeds.data;

/**
 * The FeedUrl GData type.
 */
public class FeedUrl {

    private String feed;

    private String service;

    private String authToken;

    public FeedUrl() {
    }

    public FeedUrl(String feed, String service, String authToken) {
        setFeed(feed);
        setService(service);
        setAuthToken(authToken);
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void toString(StringBuffer sb) {
        sb.append("FeedUrl");
        sb.append(" url:").append(getFeed());
        sb.append(" service:").append(getService());
        sb.append(" authToken:").append(getAuthToken());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        toString(sb);
        return sb.toString();
    }
}
