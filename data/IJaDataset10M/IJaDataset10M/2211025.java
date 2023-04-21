package com.lms.admin.orm;

import java.io.Serializable;

public class UserGroupOrm implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String userGroupId = null;

    private String userGroupName = null;

    private String userGroupShortName = null;

    private int aproveLevel;

    public UserGroupOrm() {
    }

    public UserGroupOrm(final String userGroupId, final String userGroupName, final String userGroupDesc, final Integer aproveLevel) {
        this.userGroupId = userGroupId;
        this.userGroupName = userGroupName;
        this.userGroupShortName = userGroupDesc;
        this.aproveLevel = aproveLevel;
    }

    public String getUserGroupShortName() {
        return this.userGroupShortName;
    }

    public void setUserGroupShortName(final String userGroupShortName) {
        this.userGroupShortName = userGroupShortName;
    }

    public int getAproveLevel() {
        return this.aproveLevel;
    }

    public void setAproveLevel(final int aproveLevel) {
        this.aproveLevel = aproveLevel;
    }

    public String getUserGroupId() {
        return this.userGroupId;
    }

    public void setUserGroupId(final String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return this.userGroupName;
    }

    public void setUserGroupName(final String userGroupName) {
        this.userGroupName = userGroupName;
    }
}
