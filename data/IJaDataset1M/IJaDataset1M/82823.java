package com.finchsync.sync;

/**
 * 
 * $Author: $
 * <p>
 * $Revision: $
 */
public interface SyncMonitor {

    /**
	 * Displays an activity of the specified client.
	 * 
	 * @param sess
	 *            The syncsession.
	 * @param activity
	 *            the current action of the client. (Set to null, if the client
	 *            is closing).
	 */
    void displayActivity(SyncSession sess, String activity);
}
