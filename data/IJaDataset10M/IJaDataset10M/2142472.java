package org.jdiagnose.runtime;

/**
 * User: jamie
 * Date: May 30, 2004
 * Time: 7:15:16 PM
 */
public interface ResultListener {

    /**
     * Notification that a diagnostic has started
     * @param event an event that contains the result that started
     */
    void diagnosticStarted(ResultEvent event);

    /**
     * Notification that a diagnostic has finished
     * @param event an event that contains the result that finished
     * may contain an exception if the diagnostic failed 
     */
    void diagnosticFinished(ResultEvent event);
}
