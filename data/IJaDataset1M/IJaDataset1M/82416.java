package org.wsmoss.adapter.storage.jdbc.model;

import java.util.Date;
import java.util.List;

/**
 * The Class WorkSession.
 */
public class WorkSession {

    /** The attributes. */
    private List<WorkSessionAttribute> attributes;

    /** The last update. */
    private Date lastUpdate;

    /** The id. */
    private String id;

    /** The j session id. */
    private String jSessionId;

    /** The prev j session id. */
    private String prevJSessionId;

    /**
	 * Instantiates a new work session.
	 */
    public WorkSession() {
    }

    /**
	 * Sets the last update.
	 * 
	 * @param lastUpdate the lastUpdate to set
	 */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
	 * Gets the last update.
	 * 
	 * @return the lastUpdate
	 */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
	 * Sets the attributes.
	 * 
	 * @param attributes the attributes to set
	 */
    public void setAttributes(List<WorkSessionAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
	 * Gets the attributes.
	 * 
	 * @return the attributes
	 */
    public List<WorkSessionAttribute> getAttributes() {
        return attributes;
    }

    /**
	 * Sets the id.
	 * 
	 * @param id the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * Sets the http session id.
	 * 
	 * @param httpSessionId the httpSessionId to set
	 */
    public void setHttpSessionId(String httpSessionId) {
        this.jSessionId = httpSessionId;
    }

    /**
	 * Gets the http session id.
	 * 
	 * @return the httpSessionId
	 */
    public String getHttpSessionId() {
        return jSessionId;
    }

    /**
	 * Sets the prev j session id.
	 * 
	 * @param prevJSessionId the prevJSessionId to set
	 */
    public void setPrevJSessionId(String prevJSessionId) {
        this.prevJSessionId = prevJSessionId;
    }

    /**
	 * Gets the prev j session id.
	 * 
	 * @return the prevJSessionId
	 */
    public String getPrevJSessionId() {
        return prevJSessionId;
    }
}
