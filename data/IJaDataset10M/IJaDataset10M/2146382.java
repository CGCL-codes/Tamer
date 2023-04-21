package ch.qos.logback.eclipse.model;

import java.util.EventObject;
import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * @author S&eacute;bastien Pennec
 */
public class LoggingEventManagerEvent extends EventObject {

    private static final long serialVersionUID = 1218534891181341451L;

    private final LoggingEvent[] added;

    private final LoggingEvent[] removed;

    public LoggingEventManagerEvent(final LoggingEventManager source, final LoggingEvent[] itemsAdded, final LoggingEvent[] itemsRemoved) {
        super(source);
        added = itemsAdded;
        removed = itemsRemoved;
    }

    public LoggingEvent[] getItemsAdded() {
        return added;
    }

    public LoggingEvent[] getItemsRemoved() {
        return removed;
    }
}
