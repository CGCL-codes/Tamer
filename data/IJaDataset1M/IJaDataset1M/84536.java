package org.knopflerfish.service.console;

/**
 * Session event listener interface.
 * 
 * @author Gatespace AB
 * @see Session
 */
public interface SessionListener {

    /**
     * Session end event. Called when the close method of session is executed.
     * 
     * @param session
     *            Current session
     */
    public void sessionEnd(Session session);
}
