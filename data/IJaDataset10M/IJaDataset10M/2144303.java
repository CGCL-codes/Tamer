package org.middleheaven.logging;

import org.middleheaven.events.EventListenersSet;

/**
 * 
 */
public class BroadcastLoggingService implements LoggingService {

    private EventListenersSet<LoggingEventListener> listeners = EventListenersSet.newSet(LoggingEventListener.class);

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void log(LoggingEvent event) {
        listeners.broadcastEvent().onLoggingEvent(event);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void addLogListener(LoggingEventListener listener) {
        listeners.addListener(listener);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void removeLogListener(LoggingEventListener listener) {
        listeners.removeListener(listener);
    }
}
