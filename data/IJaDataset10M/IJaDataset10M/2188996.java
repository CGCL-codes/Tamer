package com.icesoft.faces.util.event.servlet;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

/**
 * The <code>SessionDestroyedEvent</code> class represents an event that should
 * be fired when a session is destroyed in order to give registered listeners
 * time to do proper clean-up and/or other appropriate actions. </p>
 */
public class SessionDestroyedEvent extends AbstractSessionEvent implements ContextEvent {

    private HttpSessionEvent httpSessionEvent;

    /**
     * <p>
     *   Constructs a <code>SessionDestroyedEvent</code> with the specified
     *   <code>httpSessionEvent</code> as the nested event.
     * </p>
     *
     * @param httpSessionEvent the nested HTTP session event.
     */
    public SessionDestroyedEvent(final HttpSessionEvent httpSessionEvent) {
        super((HttpSession) httpSessionEvent.getSource());
        this.httpSessionEvent = httpSessionEvent;
    }

    /**
     * <p>
     *   Gets the nested HTTP Session event of this
     *   <code>SessionDestroyedEvent</code>.
     * </p>
     *
     * @return the nested HTTP Session event.
     */
    public HttpSessionEvent getNestedHttpSessionEvent() {
        return httpSessionEvent;
    }
}
