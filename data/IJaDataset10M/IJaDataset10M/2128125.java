package org.tracide.core.model;

import java.util.Date;
import java.util.Map;

/**
 * Ticket Model class.
 * 
 * @author Hiroyuki Wada
 * 
 */
public class Ticket {

    private int id;

    private Date createdDate;

    private Date lastChangedDate;

    private Map attribute;

    public String getAttribute(String key) {
        if (attribute.containsKey(key)) {
            return attribute.get(key).toString();
        }
        return null;
    }

    public Map getAttribute() {
        return attribute;
    }

    public void setAttribute(String key, String value) {
        attribute.put(key, value);
    }

    public void setAttribute(Map attribute) {
        this.attribute = attribute;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createDate) {
        this.createdDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLastChangedDate() {
        return lastChangedDate;
    }

    public void setLastChangedDate(Date lastChangeDate) {
        this.lastChangedDate = lastChangeDate;
    }
}
