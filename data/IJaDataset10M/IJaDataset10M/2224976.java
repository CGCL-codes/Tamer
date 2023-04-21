package org.apache.tomcat.util.buf;

import java.io.Serializable;

/**
 * Main tool for object expiry. 
 * Marks creation and access time of an "expirable" object,
 * and extra properties like "id", "valid", etc.
 *
 * Used for objects that expire - originally Sessions, but 
 * also Contexts, Servlets, cache - or any other object that
 * expires.
 * 
 * @author Costin Manolache
 */
public final class TimeStamp implements Serializable {

    private long creationTime = 0L;

    private long lastAccessedTime = creationTime;

    private long thisAccessedTime = creationTime;

    private boolean isNew = true;

    private long maxInactiveInterval = -1;

    private boolean isValid = false;

    MessageBytes name;

    int id = -1;

    Object parent;

    public TimeStamp() {
    }

    /**
     *  Access notification. This method takes a time parameter in order
     *  to allow callers to efficiently manage expensive calls to
     *  System.currentTimeMillis() 
     */
    public void touch(long time) {
        this.lastAccessedTime = this.thisAccessedTime;
        this.thisAccessedTime = time;
        this.isNew = false;
    }

    /** Return the "name" of the timestamp. This can be used
     *  to associate unique identifier with each timestamped object.
     *  The name is a MessageBytes - i.e. a modifiable byte[] or char[]. 
     */
    public MessageBytes getName() {
        if (name == null) name = MessageBytes.newInstance();
        return name;
    }

    /** Each object can have an unique id, similar with name but
     *  providing faster access ( array vs. hashtable lookup )
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** Returns the owner of this stamp ( the object that is
     *  time-stamped ).
     *  For a 
     */
    public void setParent(Object o) {
        parent = o;
    }

    public Object getParent() {
        return parent;
    }

    public void setCreationTime(long time) {
        this.creationTime = time;
        this.lastAccessedTime = time;
        this.thisAccessedTime = time;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public long getThisAccessedTime() {
        return thisAccessedTime;
    }

    /** Inactive interval in millis - the time is computed
     *  in millis, convert to secs in the upper layer
     */
    public long getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(long interval) {
        maxInactiveInterval = interval;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void recycle() {
        creationTime = 0L;
        lastAccessedTime = 0L;
        maxInactiveInterval = -1;
        isNew = true;
        isValid = false;
        id = -1;
        if (name != null) name.recycle();
    }
}
