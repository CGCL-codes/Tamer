package com.ohioedge.j2ee.api.org.proc.ejb;

import java.io.Serializable;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)AssignmentPK.java 1.350 01/12/03 Primary Key
 * @version 1.3.1
 * @since OEC1.2
 */
public class AssignmentPK implements java.io.Serializable {

    public AssignmentPK(Integer activityID, Integer assigneeID) {
        this.activityID = activityID;
        this.assigneeID = assigneeID;
    }

    public AssignmentPK() {
    }

    public String toString() {
        return this.activityID + "." + this.assigneeID;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object pk) {
        if (pk instanceof AssignmentPK) return this.hashCode() == ((AssignmentPK) pk).hashCode();
        return false;
    }

    public Integer activityID;

    public Integer assigneeID;

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }

    public Integer getAssigneeID() {
        return assigneeID;
    }

    public void setAssigneeID(Integer assigneeID) {
        this.assigneeID = assigneeID;
    }
}
