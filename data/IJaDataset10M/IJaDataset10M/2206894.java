package com.excilys.sugadroid.activities.interfaces;

/**
 * An activity that provides callbacks to show informations when authenticated
 * tasks perform
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public interface IAuthenticatedActivity extends ICallingLoadingTasksActivity {

    /**
	 * Callback when the action could not be performed because the user is not
	 * logged in
	 */
    public abstract void onNotLoggedIn();

    /**
	 * Callback when the action could not be performed because the user has an
	 * invalid session
	 */
    public abstract void onSessionInvalid();

    /**
	 * Callback when the action could not be performed because of a specific
	 * error
	 * 
	 * @param message
	 */
    public abstract void onServiceCallFailed(String message);

    /**
	 * Callback when the action could not be performed because of a network
	 * error
	 */
    public abstract void onServiceCallFailedNoNetwork();
}
