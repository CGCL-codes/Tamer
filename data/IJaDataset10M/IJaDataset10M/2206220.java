package com.markatta.hund.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents the status of a host at a given time
 *
 * @author johan
 */
@Entity
@Table(name = "STATUS_EVENTS")
public class Status implements Serializable, Identifiable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    /**
     * Time that the status changed into something else. <code>null</code> if
     * this status is currently active.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIMESTAMP")
    private Date endTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private StatusType type;

    @Column(name = "DETAILS")
    private String details;

    /**
     * Time the check took
     */
    @Column(name = "TIMING")
    private long timing;

    /**
     * Required for JPA. Use the other constructor when creating instances of the class
     */
    public Status() {
    }

    public Status(StatusType type, String details) {
        timestamp = new Date();
        this.type = type;
        this.details = details;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Date getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Date endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public StatusType getType() {
        return type;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return A description/message from the plugin execution
     */
    public String getDetails() {
        return details;
    }

    /**
     * @return The time the plugin took to check the service
     */
    public long getTiming() {
        return timing;
    }

    public void setTiming(long timing) {
        this.timing = timing;
    }

    /**
     * The time this status has stayed the same. If no time is set with <code>setDuration</code>
     * the duration will be calculated with the status timestamp and current time.
     */
    public long getDuration() {
        if (endTimestamp == null) {
            return new Date().getTime() - timestamp.getTime();
        } else {
            return endTimestamp.getTime() - timestamp.getTime();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Status other = (Status) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.id == 0 && (this != other)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return type.toString() + "(" + timestamp.getTime() + ")";
    }
}
