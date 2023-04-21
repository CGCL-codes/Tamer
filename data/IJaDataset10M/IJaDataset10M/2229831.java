package org.pojosoft.lms.content.model;

import java.io.Serializable;

/**
 * The activity composite key.
 * @author POJO Software
 * @version 1.0
 * @since 1.0
 */
public class ActivityPK implements Serializable {

    protected String contentOrgID;

    protected String activityID;

    public ActivityPK() {
    }

    public ActivityPK(String contentOrgID, String activityID) {
        this.contentOrgID = contentOrgID;
        this.activityID = activityID;
    }

    public String toString() {
        return contentOrgID + activityID;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ActivityPK that = (ActivityPK) o;
        if (!activityID.equals(that.activityID)) return false;
        if (!contentOrgID.equals(that.contentOrgID)) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = contentOrgID.hashCode();
        result = 29 * result + activityID.hashCode();
        return result;
    }

    public String getContentOrgID() {
        return contentOrgID;
    }

    public void setContentOrgID(String contentOrgID) {
        this.contentOrgID = contentOrgID;
    }

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }
}
