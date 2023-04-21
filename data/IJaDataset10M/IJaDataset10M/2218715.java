package org.wsmoss.core.monitor;

/**
 * The Interface StorageStatusMonitor.
 */
public interface StorageStatusMonitor {

    /**
	 * Gets the work sessions count.
	 * 
	 * @return the work sessions count
	 */
    int getWorkSessionsCount();

    /**
	 * Gets the work sessions info.
	 * 
	 * @return the work sessions info
	 */
    String[] getWorkSessionsInfo();

    /**
	 * Gets the work session info.
	 * 
	 * @param httpSessionId the http session id
	 * 
	 * @return the work session info
	 */
    String getWorkSessionInfo(String httpSessionId);

    /**
	 * Exists work session.
	 * 
	 * @param httpSessionId the http session id
	 * 
	 * @return true, if successful
	 */
    boolean existsWorkSession(String httpSessionId);

    /**
	 * Exists unloaded work session.
	 * 
	 * @param httpSessionId the http session id
	 * 
	 * @return true, if successful
	 */
    boolean existsUnloadedWorkSession(String httpSessionId);

    /**
	 * Gets the timeout info.
	 * 
	 * @param httpSessionId the http session id
	 * 
	 * @return the timeout info
	 */
    String getTimeoutInfo(String httpSessionId);
}
