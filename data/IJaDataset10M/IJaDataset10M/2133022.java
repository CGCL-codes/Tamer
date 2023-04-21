package ru.scriptum.controller.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * GmapHandler for WMSCRGN.EVENT_REGISTRY
 */
public class EventClass {

    public static final String TABLE_NAME = "EVENT_REGISTRY";

    /** Property for WMSCRGN.EVENT_REGISTRY.DESCRIPTION */
    private String description = null;

    /** Property for WMSCRGN.EVENT_REGISTRY.EVENT_CLASS */
    private String eventClass = null;

    /** Property for WMSCRGN.EVENT_REGISTRY.EVENT_ID */
    private Long eventId = null;

    private List workflowListeners = new ArrayList();

    /**
	 * Reference for exported foreign key FK1_EVENT_DELEGATE to
	 * WMSCRGN.EVENT_DELEGATE
	 */
    private List workflowDelegates = new ArrayList();

    private java.lang.String operation = null;

    /**
	 * Constructor for WMSCRGN.EVENT_REGISTRY GmapHandler.
	 */
    public EventClass() {
    }

    /**
	 * Constructor for WMSCRGN.EVENT_REGISTRY GmapHandler.
	 * 
	 * @param eventId
	 *            Long
	 * @param description
	 *            String
	 * @param eventClass
	 *            String
	 */
    public EventClass(Long eventId, String description, String eventClass) {
        this.eventId = eventId;
        this.description = description;
        this.eventClass = eventClass;
    }

    /**
	 * Compare an object to this WMSCRGN.EVENT_REGISTRY GmapHandler.
	 * 
	 * @return boolean
	 */
    public boolean equals(Object _obj) {
        boolean _ret = false;
        if (_obj != null && _obj instanceof EventClass) {
            EventClass _test = (EventClass) _obj;
            _ret = ((this.description == null && _test.description == null) || (this.description != null && _test.description != null && this.description.equals(_test.description))) && ((this.eventClass == null && _test.eventClass == null) || (this.eventClass != null && _test.eventClass != null && this.eventClass.equals(_test.eventClass))) && ((this.eventId == null && _test.eventId == null) || (this.eventId != null && _test.eventId != null && this.eventId.equals(_test.eventId))) && ((this.workflowListeners == null && _test.workflowListeners == null) || (this.workflowListeners != null && _test.workflowListeners != null && this.workflowListeners.equals(_test.workflowListeners))) && ((this.workflowDelegates == null && _test.workflowDelegates == null) || (this.workflowDelegates != null && _test.workflowDelegates != null && this.workflowDelegates.equals(_test.workflowDelegates)));
        }
        return _ret;
    }

    /**
	 * Find an instance of WMSCRGN.EVENT_REGISTRY from the database.
	 * 
	 * @return EventRegistry
	 * @param session
	 *            Session
	 * @param eventId
	 *            Long
	 * @exception DatabaseException.
	 */
    public static EventClass[] findByDelegate(Long eventId) {
        Vector params = new Vector();
        params.add(eventId);
        Vector _ret = new Vector();
        EventClass[] ret = new EventClass[_ret.size()];
        for (int i = 0; i < _ret.size(); i++) {
            ret[i] = (EventClass) _ret.get(i);
        }
        return ret;
    }

    /**
	 * Find an instance of WMSCRGN.EVENT_REGISTRY from the database.
	 * 
	 * @return EventRegistry
	 * @param session
	 *            Session
	 * @param eventId
	 *            Long
	 * @exception DatabaseException.
	 */
    public static EventClass findByEventId(Long eventId) {
        EventClass _ret = null;
        Vector params = new Vector();
        params.add(eventId);
        return _ret;
    }

    /**
	 * Find an instance of WMSCRGN.EVENT_REGISTRY from the database.
	 * 
	 * @return EventRegistry
	 * @param session
	 *            Session
	 * @param eventId
	 *            Long
	 * @exception DatabaseException.
	 */
    public static EventClass findByOperation(String operation) {
        EventClass _ret = null;
        return _ret;
    }

    /**
	 * Find an instance of WMSCRGN.EVENT_REGISTRY from the database.
	 * 
	 * @return EventRegistry
	 * @param session
	 *            Session
	 * @param eventId
	 *            Long
	 * @exception DatabaseException.
	 */
    public static EventClass findByPrimaryKey(Long eventId) {
        EventClass _ret = null;
        return _ret;
    }

    /**
	 * Getter for WMSCRGN.EVENT_REGISTRY.DESCRIPTION
	 * 
	 * @return String
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * Getter for WMSCRGN.EVENT_REGISTRY.EVENT_CLASS
	 * 
	 * @return String
	 */
    public String getEventClass() {
        return eventClass;
    }

    /**
	 * Getter for WMSCRGN.EVENT_REGISTRY.EVENT_ID
	 * 
	 * @return Long
	 */
    public Long getEventId() {
        return eventId;
    }

    /**
	 * Insert the method's description here. Creation date: (6/23/2003 2:57:56
	 * PM)
	 * 
	 * @return java.lang.String
	 */
    public java.lang.String getOperation() {
        return operation;
    }

    /**
	 * Getter for exported foreign key FK1_EVENT_DELEGATE to
	 * WMSCRGN.EVENT_DELEGATE
	 * 
	 * @return Vector
	 */
    public List getWorkflowDelegates() {
        return workflowDelegates;
    }

    /**
	 * Getter for exported foreign key FK1_EVENT_LISTENER to
	 * WMSCRGN.EVENT_LISTENER
	 * 
	 * @return Vector
	 */
    public List getWorkflowListeners() {
        return workflowListeners;
    }

    /**
	 * Generate hash code of this WMSCRGN.EVENT_REGISTRY GmapHandler.
	 * 
	 * @return int
	 */
    public int hashCode() {
        return super.hashCode();
    }

    /**
	 * Setter for WMSCRGN.EVENT_REGISTRY.DESCRIPTION
	 * 
	 * @param description
	 *            String
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * Setter for WMSCRGN.EVENT_REGISTRY.EVENT_CLASS
	 * 
	 * @param eventClass
	 *            String
	 */
    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }

    /**
	 * Setter for WMSCRGN.EVENT_REGISTRY.EVENT_ID
	 * 
	 * @param eventId
	 *            Long
	 */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    /**
	 * Primitive setter for WMSCRGN.EVENT_REGISTRY.EVENT_ID
	 * 
	 * @param eventId
	 *            long
	 */
    public void setEventId_(long eventId) {
        setEventId(new Long(eventId));
    }

    /**
	 * Insert the method's description here. Creation date: (6/23/2003 2:57:56
	 * PM)
	 * 
	 * @param newOperation
	 *            java.lang.String
	 */
    public void setOperation(java.lang.String newOperation) {
        operation = newOperation;
    }

    /**
	 * Setter for exported foreign key FK1_EVENT_DELEGATE to
	 * WMSCRGN.EVENT_DELEGATE
	 * 
	 * @param workflowDelegates
	 *            Vector
	 */
    public void setWorkflowDelegates(List workflowDelegates) {
        this.workflowDelegates = workflowDelegates;
    }

    /**
	 * Setter for exported foreign key FK1_EVENT_LISTENER to
	 * WMSCRGN.EVENT_LISTENER
	 * 
	 * @param workflowListeners
	 *            Vector
	 */
    public void setWorkflowListeners(List workflowListeners) {
        this.workflowListeners = workflowListeners;
    }
}
