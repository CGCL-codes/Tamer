package com.calefay.utils;

/** GameEvent is used both to queue events for the event handler and (usually by the event handler) to send actions to gameEntities.
 * For an action, 'target' is the action data - eg. damage amount. Actions are applied directly to entities, so they do not need a target.
 * @author James Waddington
 *
 */
public class GameEvent {

    private Object eventInitiator = null;

    private Object eventTarget = null;

    private String eventType = null;

    public GameEvent(String type, Object initiator, Object target) {
        eventInitiator = initiator;
        eventTarget = target;
        eventType = type;
    }

    public String getEventType() {
        return eventType;
    }

    public Object getEventInitiator() {
        return eventInitiator;
    }

    public Object getEventTarget() {
        return eventTarget;
    }
}
