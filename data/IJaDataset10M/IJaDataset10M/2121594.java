package fr.gedeon.telnetservice;

import java.util.List;

/**
 * This is the internal action event dispatcher, it implements {@link ActionListener} which is the
 * interface that is provided to the {@link Session} instance.<br />
 * Registered <code>ActionListener</code>s are managed by this class:
 * <ul>
 * <li>given list is ordered by priority, high to low</li>
 * <li>at each event, the listeners are traversed by order of priority</li>
 * <li>each listener's response is used to decide whether to carry on with the notification of the
 * remainder of the list</li>
 * </ul>
 * 
 * @author <a href="mailto:wgedeon@gmail.com">wgedeon@gmail.com</a>
 */
class ActionListenerMainHandlerImpl implements ActionListener {

    /**
     * The registered listeners, ordered by priority
     */
    List<ActionListener> listeners;

    /**
     * toString cache, it is invalidated by a change in the stored listeners, and reconstructed at
     * the next call to {@link #toString()}.
     */
    transient String toString = null;

    /**
     * toString cache dirty flag.
     */
    transient boolean toStringHasChanged;

    /**
     * @param listeners the listener list. This list is already ordered by priority of listeners
     */
    ActionListenerMainHandlerImpl(List<ActionListener> listeners) {
        this.listeners = listeners;
        this.toStringHasChanged = true;
    }

    public void initialize(SessionState sessionState) throws ActionListenerInitializationException {
    }

    public void initializeSessionState(SessionState sessionState) {
    }

    public boolean notifyControlChar(SessionState sessionState, String currentBuffer, int relOffset, char c) {
        boolean cont = true;
        for (ActionListener listener : this.listeners) {
            cont = listener.notifyControlChar(sessionState, currentBuffer, relOffset, c);
            if (!cont) break;
        }
        return cont;
    }

    public boolean notifyDown(SessionState sessionState, String currentBuffer, int relOffset) {
        boolean cont = true;
        for (ActionListener listener : this.listeners) {
            cont = listener.notifyDown(sessionState, currentBuffer, relOffset);
            if (!cont) break;
        }
        return cont;
    }

    public boolean notifyLeft(SessionState sessionState, String currentBuffer, int relOffset) {
        boolean cont = true;
        for (ActionListener listener : this.listeners) {
            cont = listener.notifyLeft(sessionState, currentBuffer, relOffset);
            if (!cont) break;
        }
        return cont;
    }

    public boolean notifyNewLine(SessionState sessionState, String currentBuffer, int relOffset) {
        boolean cont = true;
        for (ActionListener listener : this.listeners) {
            cont = listener.notifyNewLine(sessionState, currentBuffer, relOffset);
            if (!cont) break;
        }
        return cont;
    }

    public boolean notifyRight(SessionState sessionState, String currentBuffer, int relOffset) {
        boolean cont = true;
        for (ActionListener listener : this.listeners) {
            cont = listener.notifyRight(sessionState, currentBuffer, relOffset);
            if (!cont) break;
        }
        return cont;
    }

    public boolean notifyTab(SessionState sessionState, String currentBuffer, int relOffset) {
        boolean cont = true;
        for (ActionListener listener : this.listeners) {
            cont = listener.notifyTab(sessionState, currentBuffer, relOffset);
            if (!cont) break;
        }
        return cont;
    }

    public boolean notifyUp(SessionState sessionState, String currentBuffer, int relOffset) {
        boolean cont = true;
        for (ActionListener listener : this.listeners) {
            cont = listener.notifyUp(sessionState, currentBuffer, relOffset);
            if (!cont) break;
        }
        return cont;
    }

    public String toString() {
        if (this.toStringHasChanged || this.toString == null) {
            StringBuffer buf = new StringBuffer();
            buf.append("Action Listeners:\n");
            for (ActionListener al : this.listeners) {
                buf.append("  - ").append(al.getClass().getName()).append('\n');
            }
            this.toString = buf.toString();
        }
        return this.toString;
    }
}
