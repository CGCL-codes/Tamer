package org.vqwiki;

import org.vqwiki.persistency.db.DBDate;

/**
 * @author garethc
 * Date: 5/03/2003
 */
public class TopicLock {

    private String virtualWiki;

    private String topicName;

    private DBDate time;

    private String sessionKey;

    /**
     *
     */
    public TopicLock(String virtualWiki, String topicName, DBDate time, String sessionKey) {
        this.virtualWiki = virtualWiki;
        this.topicName = topicName;
        this.time = time;
        this.sessionKey = sessionKey;
    }

    /**
     *
     */
    public String getVirtualWiki() {
        return virtualWiki;
    }

    /**
     *
     */
    public void setVirtualWiki(String virtualWiki) {
        this.virtualWiki = virtualWiki;
    }

    /**
     *
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     *
     */
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    /**
     *
     */
    public DBDate getTime() {
        return time;
    }

    /**
     *
     */
    public void setTime(DBDate time) {
        this.time = time;
    }

    /**
     *
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     *
     */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
