package org.josso.agent;

/**
 * Implementations of this interface may are notified of changes to the list of active local sessions
 * in a web application.
 *
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version CVS $Id: LocalSessionListener.java 543 2008-03-18 21:34:58Z sgonzalez $
 */
public interface LocalSessionListener {

    /**
     * Callback method for notifying Local Session Events.
     *
     * @param event session event data.
     */
    void localSessionEvent(LocalSessionEvent event);
}
