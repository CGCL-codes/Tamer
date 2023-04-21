package org.magicbox.sso;

import org.magicbox.sso.exception.InvalidUserException;

public interface Profile {

    /**
	 * 
	 * Adds an attribute to the user.
	 * 
	 * 
	 * 
	 * @param username
	 *            The username.
	 * 
	 * @param key
	 *            The key.
	 * 
	 * @param value
	 *            The value.
	 * 
	 * @throws InvalidUserException
	 *             Invalid username.
	 * 
	 */
    public void addAttribute(String username, String key, String value) throws InvalidUserException;

    /**
	 * 
	 * Removes an attribute from the user.
	 * 
	 * 
	 * 
	 * @param username
	 *            The username.
	 * 
	 * @param key
	 *            The attribute key.
	 * 
	 * @param value
	 *            The attribute value.
	 * 
	 * @throws InvalidUserException
	 *             Invalid username.
	 * 
	 */
    public void removeAttribute(String username, String key, String value) throws InvalidUserException;

    /**
	 * 
	 * Obtains an attribute for a user.
	 * 
	 * 
	 * 
	 * @param username
	 *            The username.
	 * 
	 * @param key
	 *            The key.
	 * 
	 * @return The value.
	 * 
	 * @throws InvalidUserException
	 *             Invalid username.
	 * 
	 */
    public String getAttribute(String username, String key) throws InvalidUserException;
}
