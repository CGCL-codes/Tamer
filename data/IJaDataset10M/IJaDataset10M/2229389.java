package com.ohioedge.j2ee.api.org.proc.ejb;

import java.io.Serializable;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)ActivityScheduleStatusTypePrivilegePK.java 1.350 01/12/03
 * @version 1.3.1
 * @since OEC1.2
 */
public class ActivityScheduleStatusTypePrivilegePK implements java.io.Serializable {

    public ActivityScheduleStatusTypePrivilegePK(Integer activityScheduleStatusTypeID, Integer privilegeID) {
        this.activityScheduleStatusTypeID = activityScheduleStatusTypeID;
        this.privilegeID = privilegeID;
        ;
    }

    public ActivityScheduleStatusTypePrivilegePK() {
    }

    public String toString() {
        return this.activityScheduleStatusTypeID + "-" + this.privilegeID;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object pk) {
        if (pk instanceof ActivityScheduleStatusTypePrivilegePK) return this.hashCode() == ((ActivityScheduleStatusTypePrivilegePK) pk).hashCode();
        return false;
    }

    public Integer activityScheduleStatusTypeID;

    public Integer privilegeID;

    public Integer getActivityScheduleStatusTypeID() {
        return activityScheduleStatusTypeID;
    }

    public void setActivityScheduleStatusTypeID(Integer activityScheduleStatusTypeID) {
        this.activityScheduleStatusTypeID = activityScheduleStatusTypeID;
    }

    public Integer getPrivilegeID() {
        return privilegeID;
    }

    public void setPrivilegeID(Integer privilegeID) {
        this.privilegeID = privilegeID;
    }
}
