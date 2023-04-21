package com.vb.project.model;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

    private static final long serialVersionUID = 91236982730L;

    private int id;

    private String title;

    private String description;

    private String content;

    private int toUserId;

    private TaskStatus status;

    private Date creationDate;

    private Date lastUpdateDate;

    private float completedPercent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public float getCompletedPercent() {
        return completedPercent;
    }

    public void setCompletedPercent(float completedPercent) {
        this.completedPercent = completedPercent;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
