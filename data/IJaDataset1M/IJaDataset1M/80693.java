package org.jlf.log;

import java.util.*;
import org.jlf.config.*;

/**
 * LogManager is a concrete class which manages all of the logs in the
 * local system.  Anything that a particular log cannot do of itself,
 * LogManager is responsible for.
 *
 * @author Todd Lauinger
 * @version $Revision: 1.2 $
 *
 * @see RemoteLogManager
 */
public class LogManager extends Vector {

    /**
	 * Holds the application name the logs are generated for.
	 * The default comes from AppInfo but can be overriden.
	 */
    protected String applicationName = AppInfo.getInstance().getApplicationName();

    protected static LogManager singleton = new LogManager();

    protected LogManager() {
    }

    public static LogManager getInstance() {
        return singleton;
    }

    /**
	 * Return the application name string that the logs pertain to.
	 * With the advent of AppInfo, this simply becomes a dispatch
	 * back to that class.
	 */
    public String getApplicationName() {
        return applicationName;
    }

    /**
	 * Set the application name string that the logs pertain to
	 */
    public void setApplicationName(String appName) {
        applicationName = appName;
    }
}
