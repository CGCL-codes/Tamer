package org.jamwiki.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Provides an object representing a mapping of a user or group to a set of
 * roles.  This class exists primarily as a helper when adding or modifying
 * roles using a form interface.
 */
public class RoleMap implements Serializable {

    private Integer groupId;

    private String groupName;

    private List<String> roleNames;

    private Integer userId;

    private String userLogin;

    /**
	 *
	 */
    public RoleMap() {
    }

    /**
	 *
	 */
    public Integer getGroupId() {
        return this.groupId;
    }

    /**
	 *
	 */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
	 *
	 */
    public String getGroupName() {
        return this.groupName;
    }

    /**
	 *
	 */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
	 *
	 */
    public void addRole(String roleName) {
        if (this.roleNames == null) {
            this.roleNames = new ArrayList<String>();
        }
        this.roleNames.add(roleName);
    }

    /**
	 *
	 */
    public List<String> getRoleNames() {
        return this.roleNames;
    }

    /**
	 *
	 */
    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    /**
	 *
	 */
    public Integer getUserId() {
        return this.userId;
    }

    /**
	 *
	 */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
	 *
	 */
    public String getUserLogin() {
        return this.userLogin;
    }

    /**
	 *
	 */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
	 * This method is simply a utility method to be used with JSTL for
	 * determining if the current list of roles contains a specific role.
	 */
    public HashMap<String, String> getRoleNamesMap() {
        HashMap<String, String> results = new HashMap<String, String>();
        if (this.roleNames == null) {
            return results;
        }
        for (String key : this.roleNames) {
            String value = this.getUserGroup() + "|" + key;
            results.put(key, value);
        }
        return results;
    }

    /**
	 * This is a utility method for building a concatenated version of the
	 * user and group id values for use with JSTL.
	 */
    public String getUserGroup() {
        StringBuilder result = new StringBuilder();
        if (this.userId != null) {
            result.append(this.userId);
        } else {
            result.append(0);
        }
        result.append('|');
        if (this.groupId != null) {
            result.append(this.groupId);
        } else {
            result.append(0);
        }
        return result.toString();
    }
}
