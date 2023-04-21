package org.dvb.application;

/**
 * Abstract class for the filters. Instances of concrete classes that extend
 * <code>AppsDatabaseFilter</code> are passed to the
 * AppsDatabase.getAppAttributes and AppsDatabase.getAppIDs methods to allow an
 * applications to set a filter on the list of applications (respectively
 * AppAttributes and AppIDs) that it wants to retrieve from the AppDatabase.
 * @since MHP 1.0
 */
public abstract class AppsDatabaseFilter {

    /**
	 * Construct an AppsDatabaseFilter object.
	 */
    public AppsDatabaseFilter() {
        super();
    }

    /**
	 * Test if a specified appid should be included in the Enumeration.
	 * @param appid the specified appid to test.
	 * @return true if the application with identifier appid should be listed,
	 *         false otherwise.
	 * @since MHP1.0
	 */
    public abstract boolean accept(AppID appid);
}
