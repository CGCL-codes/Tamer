package com.gr.staffpm.datatypes;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Proxy;

/**
 * @author Graham Rhodes 21 Feb 2011 00:13:45
 */
@Entity
@Proxy(lazy = false)
@Table(name = "activity")
public class Activity implements Comparable<Activity>, Serializable {

    public static final String NOTIFIED = "notified";

    public static final String CREATED = "created";

    public static final String NEW_NOTE = "newNote";

    private static final long serialVersionUID = 1L;

    private int activityId = 0;

    private Activities activity = null;

    private User notified = null;

    private Task task = null;

    private boolean newNote = false;

    private User createdBy = null;

    private Date created = new Date(Calendar.getInstance().getTimeInMillis());

    private User updatedBy = null;

    private Date lastUpdated = new Date(Calendar.getInstance().getTimeInMillis());

    @Id
    @GeneratedValue
    @Column(name = "activity_id")
    public int getActivityId() {
        return activityId;
    }

    @ManyToOne
    @JoinColumn(name = "act_id")
    public Activities getActivity() {
        return activity;
    }

    @ManyToOne
    @JoinColumn(name = "notified_id")
    public User getNotified() {
        return notified;
    }

    @ManyToOne
    @JoinTable(name = "activity_tasks", joinColumns = @JoinColumn(name = "activity_id"), inverseJoinColumns = @JoinColumn(name = "task_id"))
    public Task getTask() {
        return task;
    }

    @Basic
    @Column(name = "newNote")
    public boolean isNewNote() {
        return newNote;
    }

    @ManyToOne
    @JoinColumn(name = "createdBy")
    public User getCreatedBy() {
        return createdBy;
    }

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    @ManyToOne
    @JoinColumn(name = "updatedBy")
    public User getUpdatedBy() {
        return updatedBy;
    }

    @Column(name = "lastUpdated")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setActivity(Activities activity) {
        this.activity = activity;
    }

    public void setNotified(User notified) {
        this.notified = notified;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setNewNote(boolean newNote) {
        this.newNote = newNote;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int compareTo(Activity other) {
        return this.created.compareTo(other.getCreated());
    }
}
