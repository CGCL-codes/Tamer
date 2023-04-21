package com.agileprojectassistant.client.permissions;

public interface PermissionsLevel {

    /**
	 * Get the permissions level for the currently logged in user.
	 * @return The permissions level for the current user as a string
	 */
    public String getLevel();
}
