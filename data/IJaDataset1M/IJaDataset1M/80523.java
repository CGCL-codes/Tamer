package com.ubx1.pdpscanner.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The user service caller
 * 
 * @author wbraik
 * 
 */
public interface UserServiceAsync {

    /**
	 * Log a user in
	 * 
	 * @param username
	 *            the user's username
	 * @param password
	 *            the user's password
	 * @param callback
	 *            the callback object
	 */
    void logIn(String username, String password, AsyncCallback<String> callback);

    /**
	 * Register a new user in the database
	 * 
	 * @param name
	 *            the new user's name
	 * @param email
	 *            the new user's email
	 * @param username
	 *            the new user's username
	 * @param password
	 *            the new user's password
	 * @param cell
	 *            the new user's cell phone number
	 * @param creationDate
	 *            the new user's creation date
	 * @param callback
	 *            the callback object
	 */
    void signUp(String name, String email, String username, String password, String cell, String creationDate, AsyncCallback<String> callback);

    /**
	 * Check wheter the user's session is still valid
	 * 
	 * @param callback
	 *            the callback object
	 */
    void checkSessionValidity(AsyncCallback<String> callback);

    /**
	 * Check whether the given password corresponds to the current user
	 * 
	 * @param password
	 *            the password
	 * @param callback
	 *            the callback object
	 */
    void checkPasswordValidity(String password, AsyncCallback<String> callback);

    /**
	 * Log a user out
	 * 
	 * @param callback
	 *            the callback object
	 */
    void logOut(AsyncCallback<String> callback);

    /**
	 * Update the user's password
	 * 
	 * @param newPassword
	 *            the new password
	 * @param callback
	 *            the callback object
	 */
    void updatePassword(String newPassword, AsyncCallback<String> callback);
}
