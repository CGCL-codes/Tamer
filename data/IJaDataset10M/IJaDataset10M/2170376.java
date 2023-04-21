package com.lms.admin.orm;

import java.io.Serializable;

/**
 * Created by G.Vijayaraja
 * 
 * lms
 */
public class UserAccessRightsOrm implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int userAcessId;

    private String groupId = null;

    private String userId = null;

    private String securityStr = null;

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }

    public String getSecurityStr() {
        return this.securityStr;
    }

    public void setSecurityStr(final String securityStr) {
        this.securityStr = securityStr;
    }

    public int getUserAcessId() {
        return this.userAcessId;
    }

    public void setUserAcessId(final int userAcessId) {
        this.userAcessId = userAcessId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }
}
