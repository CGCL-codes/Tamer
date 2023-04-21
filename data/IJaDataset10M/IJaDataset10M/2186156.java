package com.express.service.dto;

import java.io.Serializable;

public class AcceptanceCriteriaDto implements Serializable {

    private long id;

    private Long version;

    private String reference;

    private String title;

    private String description;

    private boolean verified;

    private BacklogItemDto backlogItem;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public BacklogItemDto getBacklogItem() {
        return backlogItem;
    }

    public void setBacklogItem(BacklogItemDto backlogItem) {
        this.backlogItem = backlogItem;
    }
}
