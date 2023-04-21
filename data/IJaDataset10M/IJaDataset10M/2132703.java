package org.middleheaven.logging;

import org.middleheaven.core.wiring.service.Service;

/**
 * The Logging Service.
 * 
 * This is a environment dependant service and is initialized and controlled by the environment.
 */
@Service
public interface LoggingService {

    /**
	 * Publish a new log event to be broadcast among registered {@link LoggingEventListener}s.
	 * @param event
	 */
    public void log(LoggingEvent event);

    /**
	 * Add a {@link LoggingEventListener} that will receive future {@link LoggingEvent} events.
	 * @param listener the listener to register.
	 */
    public void addLogListener(LoggingEventListener listener);

    /**
	 * Remove a previously added {@link LoggingEventListener}. If the listeners was not previous register nothing the method does nothing.
	 * 
	 * @param listener the listener to remove from the register.
	 */
    public void removeLogListener(LoggingEventListener listener);
}
