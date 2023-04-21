package org.tolven.queue.entity;

import java.io.Serializable;
import java.util.List;
import org.tolven.doc.bean.TolvenMessage;

/**
 * DTO for the Queue oject .
 * 
 * @author Sabu Antony
 * 
 */
public class QueueInfo implements Serializable {

    private String name;

    private String jndiName;

    private int messageCount;

    private int fullSize;

    private int pageSize;

    private int downCacheSize;

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDownCacheSize() {
        return downCacheSize;
    }

    public void setDownCacheSize(int downCacheSize) {
        this.downCacheSize = downCacheSize;
    }

    public int getFullSize() {
        return fullSize;
    }

    public void setFullSize(int fullSize) {
        this.fullSize = fullSize;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
