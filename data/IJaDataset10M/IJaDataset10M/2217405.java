package com.bbn.vessel.core.runtime.event;

import com.bbn.vessel.core.runtime.message.Message;

/**
 * An event is a message sent from the game to the runtime engine.
 * <p>
 * 
 * <pre>
 * By convention, all event {@link #getType} names are either "On"+VERB
 * for trigger events, e.g.:
 *   OnEnter(nodeName=roomX)
 * or "Is"+VERB for condition events:
 *   IsInside(nodeName=roomX, is_true=true)
 * </pre>
 */
public interface Event extends Message {

    /** our {@link #getKind()} value */
    String EVENT_KIND = "event";
}
