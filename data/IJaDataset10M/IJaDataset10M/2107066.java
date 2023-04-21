package com.tysanclan.site.projectewok.beans;

/**
 * @author Jeroen Steenbeeke
 */
public interface AuthenticationService {

    /**
	 * Determines whether or not the given login credentials represent a valid
	 * member
	 * 
	 * @param username
	 *            The username presented
	 * @param password
	 *            The password provided
	 * @return <code>true</code> if this is a valid member, <code>false</code>
	 *         otherwise
	 */
    public abstract boolean isValidMember(String username, String password);

    /**
	 * Determines whether or not the given login credentials represent a valid
	 * user
	 * 
	 * @param username
	 *            The username presented
	 * @param password
	 *            The password provided
	 * @return <code>true</code> if this is a valid user, <code>false</code>
	 *         otherwise
	 */
    public abstract boolean isValidUser(String username, String password);
}
