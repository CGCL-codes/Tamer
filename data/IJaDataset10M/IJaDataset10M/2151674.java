package com.ohioedge.j2ee.api.org.proc.ejb;

/**
 * @(#)ActivityStatusEJB.java 1.350 01/12/03
 * @version 1.3.1
 * @see org.j2eebuilder.view.ManagedComponentObjectFactory#getDataVO(java.lang.Object,
 *      java.lang.Class)
 * @since OEC1.2
 */
public class ActivityStatusBean extends org.j2eebuilder.model.ejb.SignatureImpl {

    public Integer activityID;

    public Integer activityStatusTypeID;

    public String name;

    public String description;

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }

    public Integer getActivityStatusTypeID() {
        return activityStatusTypeID;
    }

    public void setActivityStatusTypeID(Integer activityStatusTypeID) {
        this.activityStatusTypeID = activityStatusTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
