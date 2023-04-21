package org.vizzini.game.event;

import org.vizzini.game.action.IAction;
import org.vizzini.util.event.IEvent;

/**
 * Provides an event for action received changes. This class is immutable so
 * that listeners can't inadvertently alter instances.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class ActionReceivedEvent implements IEvent {

    /** Action which caused the action received change. */
    private IAction _action;

    /**
     * Construct this object with the given parameter.
     *
     * @param  action  Action which caused this action received change.
     *
     * @since  v0.1
     */
    public ActionReceivedEvent(IAction action) {
        _action = action;
    }

    /**
     * @return  the action.
     *
     * @since   v0.1
     */
    public IAction getAction() {
        return _action;
    }
}
