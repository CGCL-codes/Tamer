package com.ohioedge.j2ee.api.org.proc.ejb;

import java.io.Serializable;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)ActivityPK.java 1.350 01/12/03
 * @version 1.3.1
 * @since OEC1.2
 */
public class ActivityPK implements java.io.Serializable {

    public ActivityPK(Integer activityID) {
        this.activityID = activityID;
    }

    public ActivityPK() {
    }

    public String toString() {
        return String.valueOf(this.activityID);
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object pk) {
        if (pk instanceof ActivityPK) return this.hashCode() == ((ActivityPK) pk).hashCode();
        return false;
    }

    public Integer activityID;

    public Integer getActivityID() {
        return activityID;
    }

    public void setActivityID(Integer activityID) {
        this.activityID = activityID;
    }
}
