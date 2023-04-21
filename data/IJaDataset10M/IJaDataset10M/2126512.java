package com.entelience.objects.risk;

import java.util.Date;
import java.io.Serializable;

/**
 * BEAN - Event
 * 
 */
public class Event implements Serializable {

    public Event() {
    }

    private RiskEventId eventId;

    private String reference;

    private String title;

    private String description;

    private Integer defaultLikelihoodId;

    private String defaultLikelihood;

    private Double defaultProbability;

    private int defaultPeriod;

    private Date creationDate;

    private Date lastModificationDate;

    private int eventCategoryId;

    private String category;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDefaultLikelihood() {
        return defaultLikelihood;
    }

    public void setDefaultLikelihood(String defaultLikelihood) {
        this.defaultLikelihood = defaultLikelihood;
    }

    public Integer getDefaultLikelihoodId() {
        return defaultLikelihoodId;
    }

    public void setDefaultLikelihoodId(Integer defaultLikelihoodId) {
        this.defaultLikelihoodId = defaultLikelihoodId;
    }

    public int getDefaultPeriod() {
        return defaultPeriod;
    }

    public void setDefaultPeriod(int defaultPeriod) {
        this.defaultPeriod = defaultPeriod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(int eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    public RiskEventId getEventId() {
        return eventId;
    }

    public void setEventId(RiskEventId eventId) {
        this.eventId = eventId;
    }

    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
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

    public Double getDefaultProbability() {
        return defaultProbability;
    }

    public void setDefaultProbability(Double defaultProbability) {
        this.defaultProbability = defaultProbability;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        StringBuffer s = new StringBuffer(super.toString());
        s.append(" [eventId=").append(eventId).append("],");
        s.append(" [reference=").append(reference).append("],");
        s.append(" [title=").append(title).append("],");
        s.append(" [eventCategoryId=").append(eventCategoryId).append(']');
        return s.toString();
    }
}
