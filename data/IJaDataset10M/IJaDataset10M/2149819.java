package org.apache.batik.anim.timing;

import org.w3c.dom.events.Event;

/**
 * Abstract class from which all event-like timing specifier classes derive.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id: EventLikeTimingSpecifier.java 475477 2006-11-15 22:44:28Z cam $
 */
public abstract class EventLikeTimingSpecifier extends OffsetTimingSpecifier {

    /**
     * Creates a new EventLikeTimingSpecifier object.
     */
    public EventLikeTimingSpecifier(TimedElement owner, boolean isBegin, float offset) {
        super(owner, isBegin, offset);
    }

    /**
     * Returns whether this timing specifier is event-like (i.e., if it is
     * an eventbase, accesskey or a repeat timing specifier).
     */
    public boolean isEventCondition() {
        return true;
    }

    /**
     * Invoked to resolve an event-like timing specifier into an instance time.
     */
    public abstract void resolve(Event e);
}
