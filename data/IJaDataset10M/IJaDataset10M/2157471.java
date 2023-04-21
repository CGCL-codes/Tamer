package com.bluemarsh.jswat.core.context;

import com.bluemarsh.jswat.core.session.Session;
import com.bluemarsh.jswat.core.session.SessionProvider;
import java.util.EventObject;

/**
 * An event which indicates that the debugger context has changed. This
 * includes the current thread, current stack frame, and current location.
 *
 * @author  Nathan Fiedler
 */
public class ContextEvent extends EventObject {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /** The type of context change. */
    private ContextEventType type;

    /** The Session in which the change occurred. */
    private transient Session session;

    /** Indicates if Session was suspending when this event occurred. */
    private boolean suspending;

    /**
     * Constructs a ContextEvent.
     *
     * @param  source      source of this event.
     * @param  session     the associated Session.
     * @param  type        the type of the change.
     * @param  suspending  true if Session is suspending as a result of this.
     */
    public ContextEvent(Object source, Session session, ContextEventType type, boolean suspending) {
        super(source);
        this.session = session;
        this.type = type;
        this.suspending = suspending;
    }

    /**
     * Returns the Session relating to this event.
     *
     * @return  Session for this event.
     */
    public Session getSession() {
        return session;
    }

    /**
     * Returns the type of the context change.
     *
     * @return  an instance of the Type enum.
     */
    public ContextEventType getType() {
        return type;
    }

    /**
     * Indicates if the Session for this event is the current one.
     *
     * @return  true if event relates to current session, false otherwise.
     */
    public boolean isCurrentSession() {
        return SessionProvider.isCurrentSession(session);
    }

    /**
     * Indicates if Session was suspending when this event occurred.
     *
     * @return  true if Session suspended as a result, false otherwise.
     */
    public boolean isSuspending() {
        return suspending;
    }
}
