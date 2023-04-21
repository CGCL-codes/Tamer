package org.jefb.sec.entity;

import java.security.PublicKey;
import java.util.Date;
import javax.crypto.SecretKey;

public class ReceiverSession {

    private String senderAgentId;

    private String sessionId;

    private String sessionOwner;

    private PublicKey senderPublicKey;

    private SecretKey senderAESKey;

    private Date creationTime;

    private Date lastAccessTime;

    private Integer accessCounter;

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionOwner() {
        return sessionOwner;
    }

    public void setSessionOwner(String sessionOwner) {
        this.sessionOwner = sessionOwner;
    }

    public void setSenderPublicKey(PublicKey senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public PublicKey getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderAESKey(SecretKey senderAESKey) {
        this.senderAESKey = senderAESKey;
    }

    public SecretKey getSenderAESKey() {
        return senderAESKey;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccessCounter(Integer accessCounter) {
        this.accessCounter = accessCounter;
    }

    public Integer getAccessCounter() {
        return accessCounter;
    }

    public void setSenderAgentId(String senderAgentId) {
        this.senderAgentId = senderAgentId;
    }

    public String getSenderAgentId() {
        return senderAgentId;
    }
}
