package com.eptica.ias.models.requests.requestevent;

import java.io.*;
import java.lang.Comparable;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import com.eptica.ias.util.*;
import com.eptica.ias.models.requests.request.*;
import com.eptica.ias.models.requests.requesteventtype.*;
import com.eptica.ias.models.messages.message.*;

/**
 * Map the columns of the request_event table.
 * Supports many-to-one methods expected by Hibernate.
 *
 * The corresponding hibernate mapping file is located
 * under src/main/resources, under the same package hierarchy.
 */
public class RequestEventModel implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(RequestEventModel.class);

    private String requestEventId = null;

    private String esRequestEventId = null;

    private String requestId = null;

    private Integer requestEventTypeId = null;

    private java.util.Date creationDate = null;

    private String requestFrom = null;

    private String requestEsFromId = null;

    private String requestTo = null;

    private String requestEsToId = null;

    private String description = null;

    private String messageId = null;

    private Boolean hidden = null;

    private Integer version = null;

    private RequestModel request = null;

    private RequestEventTypeModel requestEventType = null;

    private MessageModel message = null;

    /**
     * Helper method to know whether the primary key is set or not,
     * that is if getRequestEventId() is not null and has a length greater than 0.
     * @return true if the primary key is set, false otherwise
     */
    public boolean hasPrimaryKey() {
        return getRequestEventId() != null && getRequestEventId().length() > 0;
    }

    /**
     * Setter for the field requestEventId that maps the column request_event_id.
     * Null value is not accepted by the underlying database.
     * Id
     * @param requestEventId the requestEventId
     */
    public void setRequestEventId(String requestEventId) {
        this.requestEventId = requestEventId;
    }

    /**
     * Getter for the field requestEventId that maps the column request_event_id.
     * @return the requestEventId.
     */
    public String getRequestEventId() {
        return requestEventId;
    }

    /**
     * Helper method to know if the requestEventId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasRequestEventId() {
        return getRequestEventId() != null && getRequestEventId().length() > 0;
    }

    /**
     * @see #hasRequestEventId()
     */
    public boolean getHasRequestEventId() {
        return hasRequestEventId();
    }

    /**
     * Setter for the field esRequestEventId that maps the column es_request_event_id.
     * Null value is accepted by the underlying database.
     * Eptica Server requestevent internal id
     * @param esRequestEventId the esRequestEventId
     */
    public void setEsRequestEventId(String esRequestEventId) {
        this.esRequestEventId = esRequestEventId;
    }

    /**
     * Getter for the field esRequestEventId that maps the column es_request_event_id.
     * @return the esRequestEventId.
     */
    public String getEsRequestEventId() {
        return esRequestEventId;
    }

    /**
     * Helper method to know if the esRequestEventId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasEsRequestEventId() {
        return getEsRequestEventId() != null && getEsRequestEventId().length() > 0;
    }

    /**
     * @see #hasEsRequestEventId()
     */
    public boolean getHasEsRequestEventId() {
        return hasEsRequestEventId();
    }

    /**
     * Setter for the field requestId that maps the column request_id.
     * Null value is not accepted by the underlying database.
     * This method will not persit the field, it is provided as a way to pass this value to a search facility (such as a DAO).
     * To persist this field you must use instead the setRequest setter.
     * Note that the setRequest setter set also this field.
     * Request
     * @param requestId the requestId
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Getter for the field requestId that maps the column request_id.
     * @return the requestId.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Helper method to know if the requestId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasRequestId() {
        return getRequestId() != null && getRequestId().length() > 0;
    }

    /**
     * @see #hasRequestId()
     */
    public boolean getHasRequestId() {
        return hasRequestId();
    }

    /**
     * Setter for the field requestEventTypeId that maps the column request_event_type_id.
     * Null value is not accepted by the underlying database.
     * This method will not persit the field, it is provided as a way to pass this value to a search facility (such as a DAO).
     * To persist this field you must use instead the setRequestEventType setter.
     * Note that the setRequestEventType setter set also this field.
     * Request event tye
     * @param requestEventTypeId the requestEventTypeId
     */
    public void setRequestEventTypeId(Integer requestEventTypeId) {
        this.requestEventTypeId = requestEventTypeId;
    }

    /**
     * Getter for the field requestEventTypeId that maps the column request_event_type_id.
     * @return the requestEventTypeId.
     */
    public Integer getRequestEventTypeId() {
        return requestEventTypeId;
    }

    /**
     * Helper method to know if the requestEventTypeId is not null .<br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasRequestEventTypeId() {
        return getRequestEventTypeId() != null;
    }

    /**
     * @see #hasRequestEventTypeId()
     */
    public boolean getHasRequestEventTypeId() {
        return hasRequestEventTypeId();
    }

    /**
     * Helper method to set the requestEventTypeId field via an int.
     * @see #setRequestEventTypeId(Integer)
     */
    public void setRequestEventTypeId(int requestEventTypeId) {
        setRequestEventTypeId(Integer.valueOf(requestEventTypeId));
    }

    /**
     * Helper method to set the requestEventTypeId field via a long.
     * @see #setRequestEventTypeId(Integer)
     */
    public void setRequestEventTypeId(long requestEventTypeId) {
        setRequestEventTypeId(new Integer("" + requestEventTypeId));
    }

    /**
     * Helper method to set the requestEventTypeId field via a boolean.
     * True is 1, false is 0.
     * @see #setRequestEventTypeId(Integer)
     */
    public void setRequestEventTypeId(boolean requestEventTypeId) {
        setRequestEventTypeId((requestEventTypeId == true) ? 1 : 0);
    }

    /**
     * Setter for the field creationDate that maps the column creation_date.
     * Null value is not accepted by the underlying database.
     * Indicates when this event occurs. Used to sort request events in a chronological order.
     * @param creationDate the creationDate
     */
    public void setCreationDate(java.util.Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Getter for the field creationDate that maps the column creation_date.
     * @return the creationDate.
     */
    public java.util.Date getCreationDate() {
        return creationDate;
    }

    /**
     * Helper method to know if the creationDate is not null .<br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    /**
     * @see #hasCreationDate()
     */
    public boolean getHasCreationDate() {
        return hasCreationDate();
    }

    /**
     * Returns this creationDate date as a localized string.<br>
     * @return the creationDate localized value
     */
    public String getLocalizedCreationDate() {
        if (hasCreationDate()) {
            return DateUtil.getLocalizedDate(getCreationDate());
        } else {
            return "";
        }
    }

    /**
     * Setter for the field requestFrom that maps the column request_from.
     * Null value is accepted by the underlying database.
     * From
     * @param requestFrom the requestFrom
     */
    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    /**
     * Getter for the field requestFrom that maps the column request_from.
     * @return the requestFrom.
     */
    public String getRequestFrom() {
        return requestFrom;
    }

    /**
     * Helper method to know if the requestFrom is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasRequestFrom() {
        return getRequestFrom() != null && getRequestFrom().length() > 0;
    }

    /**
     * @see #hasRequestFrom()
     */
    public boolean getHasRequestFrom() {
        return hasRequestFrom();
    }

    /**
     * Setter for the field requestEsFromId that maps the column request_es_from_id.
     * Null value is accepted by the underlying database.
     * TODO: set a description for request_event.request_es_from_id
     * @param requestEsFromId the requestEsFromId
     */
    public void setRequestEsFromId(String requestEsFromId) {
        this.requestEsFromId = requestEsFromId;
    }

    /**
     * Getter for the field requestEsFromId that maps the column request_es_from_id.
     * @return the requestEsFromId.
     */
    public String getRequestEsFromId() {
        return requestEsFromId;
    }

    /**
     * Helper method to know if the requestEsFromId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasRequestEsFromId() {
        return getRequestEsFromId() != null && getRequestEsFromId().length() > 0;
    }

    /**
     * @see #hasRequestEsFromId()
     */
    public boolean getHasRequestEsFromId() {
        return hasRequestEsFromId();
    }

    /**
     * Setter for the field requestTo that maps the column request_to.
     * Null value is accepted by the underlying database.
     * To
     * @param requestTo the requestTo
     */
    public void setRequestTo(String requestTo) {
        this.requestTo = requestTo;
    }

    /**
     * Getter for the field requestTo that maps the column request_to.
     * @return the requestTo.
     */
    public String getRequestTo() {
        return requestTo;
    }

    /**
     * Helper method to know if the requestTo is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasRequestTo() {
        return getRequestTo() != null && getRequestTo().length() > 0;
    }

    /**
     * @see #hasRequestTo()
     */
    public boolean getHasRequestTo() {
        return hasRequestTo();
    }

    /**
     * Setter for the field requestEsToId that maps the column request_es_to_id.
     * Null value is accepted by the underlying database.
     * TODO: set a description for request_event.request_es_to_id
     * @param requestEsToId the requestEsToId
     */
    public void setRequestEsToId(String requestEsToId) {
        this.requestEsToId = requestEsToId;
    }

    /**
     * Getter for the field requestEsToId that maps the column request_es_to_id.
     * @return the requestEsToId.
     */
    public String getRequestEsToId() {
        return requestEsToId;
    }

    /**
     * Helper method to know if the requestEsToId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasRequestEsToId() {
        return getRequestEsToId() != null && getRequestEsToId().length() > 0;
    }

    /**
     * @see #hasRequestEsToId()
     */
    public boolean getHasRequestEsToId() {
        return hasRequestEsToId();
    }

    /**
     * Setter for the field description that maps the column description.
     * Null value is accepted by the underlying database.
     * Description
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the field description that maps the column description.
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Helper method to know if the description is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasDescription() {
        return getDescription() != null && getDescription().length() > 0;
    }

    /**
     * @see #hasDescription()
     */
    public boolean getHasDescription() {
        return hasDescription();
    }

    /**
     * Setter for the field messageId that maps the column message_id.
     * Null value is accepted by the underlying database.
     * This method will not persit the field, it is provided as a way to pass this value to a search facility (such as a DAO).
     * To persist this field you must use instead the setMessage setter.
     * Note that the setMessage setter set also this field.
     * Message
     * @param messageId the messageId
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Getter for the field messageId that maps the column message_id.
     * @return the messageId.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Helper method to know if the messageId is not null  and has a length greater than 0. <br>
     * This method is mostly used to have a leaner and cleaner code in your view templates.
     */
    public boolean hasMessageId() {
        return getMessageId() != null && getMessageId().length() > 0;
    }

    /**
     * @see #hasMessageId()
     */
    public boolean getHasMessageId() {
        return hasMessageId();
    }

    /**
     * Setter for the field hidden that maps the column hidden.
     * Null value is accepted by the underlying database.
     * Hidden
     * @param hidden the hidden
     */
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Getter for the field hidden that maps the column hidden.
     * @return the hidden.
     */
    public Boolean getHidden() {
        return hidden;
    }

    /**
     * Setter for the field version that maps the column version.
     * It is used for optimistic locking by hibernate.
     * Null value is accepted by the underlying database.
     * Version
     * @param version the version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Getter for the field version that maps the column version.
     * It is used for optimistic locking by hibernate.
     * @return the version.
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Set the request without adding this requestEvent on the passed request.<br>
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by RequestModel
     */
    public void setRequest(RequestModel request) {
        this.request = request;
        if (request != null) {
            setRequestId(request.getRequestId());
        } else {
            setRequestId(null);
        }
    }

    /**
     * Return the request
     */
    public RequestModel getRequest() {
        return request;
    }

    /**
     * Helper method to know if the request has been set
     */
    public boolean hasRequest() {
        return getRequest() != null;
    }

    /**
     * @see #hasRequest()
     */
    public boolean getHasRequest() {
        return hasRequest();
    }

    /**
     * Set the requestEventType without adding this requestEvent on the passed requestEventType.<br>
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by RequestEventTypeModel
     */
    public void setRequestEventType(RequestEventTypeModel requestEventType) {
        this.requestEventType = requestEventType;
        if (requestEventType != null) {
            setRequestEventTypeId(requestEventType.getRequestEventTypeId());
        } else {
            setRequestEventTypeId(null);
        }
    }

    /**
     * Return the requestEventType
     */
    public RequestEventTypeModel getRequestEventType() {
        return requestEventType;
    }

    /**
     * Helper method to know if the requestEventType has been set
     */
    public boolean hasRequestEventType() {
        return getRequestEventType() != null;
    }

    /**
     * @see #hasRequestEventType()
     */
    public boolean getHasRequestEventType() {
        return hasRequestEventType();
    }

    /**
     * Set the message without adding this requestEvent on the passed message.<br>
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by MessageModel
     */
    public void setMessage(MessageModel message) {
        this.message = message;
        if (message != null) {
            setMessageId(message.getMessageId());
        } else {
            setMessageId(null);
        }
    }

    /**
     * Return the message
     */
    public MessageModel getMessage() {
        return message;
    }

    /**
     * Helper method to know if the message has been set
     */
    public boolean hasMessage() {
        return getMessage() != null;
    }

    /**
     * @see #hasMessage()
     */
    public boolean getHasMessage() {
        return hasMessage();
    }

    private boolean _freezeUseUidInEquals = false;

    private boolean _useUidInEquals = true;

    private java.rmi.dgc.VMID _uidInEquals = null;

    private void setEqualsAndHashcodeStrategy() {
        if (_freezeUseUidInEquals == false) {
            _freezeUseUidInEquals = true;
            _useUidInEquals = useUidInEquals();
            if (_useUidInEquals) {
                _uidInEquals = new java.rmi.dgc.VMID();
            }
        }
    }

    private boolean useUidInEquals() {
        return hasPrimaryKey();
    }

    /**
     * Please read discussion about object identity at <a href="http://www.hibernate.org/109.html">http://www.hibernate.org/109.html</a>
     * @see java.lang.Object#equals(Object)
     * @see #setEqualsAndHashcodeStrategy()
     * @return true if the equals, false otherwise
     */
    public boolean equals(Object requestEvent) {
        if (this == requestEvent) {
            return true;
        }
        if (requestEvent == null) {
            return false;
        }
        if (!(requestEvent instanceof RequestEventModel)) {
            return false;
        }
        RequestEventModel other = (RequestEventModel) requestEvent;
        setEqualsAndHashcodeStrategy();
        if (this._useUidInEquals != other._useUidInEquals && other._freezeUseUidInEquals == true) {
            if (logger.isErrorEnabled()) {
                logger.error("Limit case reached in equals strategy. Developper, fix me", new Exception("stack trace"));
            }
            throw new IllegalStateException("Limit case reached in equals strategy. Developper, fix me");
        }
        if (_useUidInEquals) {
            boolean eq = _uidInEquals.equals(other._uidInEquals);
            if (eq) {
                if (hashCode() != other.hashCode()) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Limit case reached in equals strategy. Developper, fix me", new Exception("stack trace"));
                    }
                }
            }
            return eq;
        } else {
            boolean eq = getRequestEventId().equals(other.getRequestEventId());
            if (eq) {
                if (hashCode() != other.hashCode()) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Limit case reached in equals strategy. Developper, fix me", new Exception("stack trace"));
                    }
                }
            }
            return eq;
        }
    }

    /**
     * When two objects are equals, their hashcode must be equal too
     * @see #equals(Object)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        setEqualsAndHashcodeStrategy();
        if (_useUidInEquals) {
            return _uidInEquals.hashCode();
        } else {
            if (hasPrimaryKey()) {
                return getRequestEventId().hashCode();
            } else {
                return super.hashCode();
            }
        }
    }

    /**
     * String representation of the current $modelClass
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (hasRequestEventId()) {
            ret.append("request_event.request_event_id=[").append(getRequestEventId()).append("]\n");
        }
        if (hasEsRequestEventId()) {
            ret.append("request_event.es_request_event_id=[").append(getEsRequestEventId()).append("]\n");
        }
        if (hasRequestId()) {
            ret.append("request_event.request_id=[").append(getRequestId()).append("]\n");
        }
        if (hasRequestEventTypeId()) {
            ret.append("request_event.request_event_type_id=[").append(getRequestEventTypeId()).append("]\n");
        }
        if (hasCreationDate()) {
            ret.append("request_event.creation_date=[").append(getLocalizedCreationDate()).append("]\n");
        }
        if (hasRequestFrom()) {
            ret.append("request_event.request_from=[").append(getRequestFrom()).append("]\n");
        }
        if (hasRequestEsFromId()) {
            ret.append("request_event.request_es_from_id=[").append(getRequestEsFromId()).append("]\n");
        }
        if (hasRequestTo()) {
            ret.append("request_event.request_to=[").append(getRequestTo()).append("]\n");
        }
        if (hasRequestEsToId()) {
            ret.append("request_event.request_es_to_id=[").append(getRequestEsToId()).append("]\n");
        }
        if (hasDescription()) {
            ret.append("request_event.description=[").append(getDescription()).append("]\n");
        }
        if (hasMessageId()) {
            ret.append("request_event.message_id=[").append(getMessageId()).append("]\n");
        }
        ret.append("request_event.hidden=[").append(getHidden()).append("]\n");
        ret.append("request_event.version=[").append(getVersion()).append("]\n");
        return ret.toString();
    }

    /**
     * A simple unique one-line String representation of this model.
     * Can be used in a select html form tag.
     */
    public String toDisplayString() {
        StringBuilder ret = new StringBuilder("");
        if (hasRequestEventId()) {
            ret.append(getRequestEventId()).append(" ");
        }
        if (hasRequestFrom()) {
            ret.append(getRequestFrom()).append(" ");
        }
        if (hasRequestTo()) {
            ret.append(getRequestTo()).append(" ");
        }
        if (hasDescription()) {
            ret.append(getDescription()).append(" ");
        }
        if (ret.toString().length() > 64) {
            return ret.toString().substring(0, 64) + "...";
        }
        if (ret.toString().trim().length() == 0) {
            return "hashcode=" + hashCode();
        }
        return ret.toString();
    }

    /**
     * compare this object to the passed instance
     *
     * @param object the object to compare with
     * @return a integer
     */
    public int compareTo(Object object) {
        RequestEventModel obj = (RequestEventModel) object;
        return new CompareToBuilder().append(getRequestEventId(), obj.getRequestEventId()).append(getEsRequestEventId(), obj.getEsRequestEventId()).append(getRequestId(), obj.getRequestId()).append(getRequestEventTypeId(), obj.getRequestEventTypeId()).append(getCreationDate(), obj.getCreationDate()).append(getRequestFrom(), obj.getRequestFrom()).append(getRequestEsFromId(), obj.getRequestEsFromId()).append(getRequestTo(), obj.getRequestTo()).append(getRequestEsToId(), obj.getRequestEsToId()).append(getDescription(), obj.getDescription()).append(getMessageId(), obj.getMessageId()).append(getHidden(), obj.getHidden()).append(getVersion(), obj.getVersion()).toComparison();
    }
}
