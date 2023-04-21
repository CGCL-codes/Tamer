package org.jactr.core.queue.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.event.AbstractACTREvent;
import org.jactr.core.queue.ITimedEvent;
import org.jactr.core.queue.TimedEventQueue;
import org.jactr.core.runtime.ACTRRuntime;

/**
 * TimedEvent events are sent whenever a TimedEvent's state has changed.
 * 
 * @author harrison
 * @created April 18, 2003
 */
public class TimedEventEvent extends AbstractACTREvent<TimedEventQueue, ITimedEventListener> {

    /**
   * Logger definition
   */
    private static final transient Log LOGGER = LogFactory.getLog(TimedEventEvent.class);

    public static enum Type {

        ABORTED, FIRED, QUEUED, UPDATED
    }

    private Type _type;

    private ITimedEvent _timedEvent;

    public TimedEventEvent(TimedEventQueue queue, ITimedEvent te, Type type) {
        super(queue, ACTRRuntime.getRuntime().getClock(queue.getModel()).getTime());
        _type = type;
        _timedEvent = te;
    }

    /**
   * 
   */
    public ITimedEvent getTimedEvent() {
        return _timedEvent;
    }

    public Type getType() {
        return _type;
    }

    @Override
    public void fire(final ITimedEventListener listener) {
        switch(this.getType()) {
            case QUEUED:
                listener.eventQueued(this);
                break;
            case ABORTED:
                listener.eventAborted(this);
                break;
            case FIRED:
                listener.eventFired(this);
                break;
            case UPDATED:
                listener.eventUpdated(this);
                break;
            default:
                if (LOGGER.isWarnEnabled()) LOGGER.warn("No clue what to do with type " + this.getType());
        }
    }
}
