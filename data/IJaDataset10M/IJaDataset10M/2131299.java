package org.matsim.utils.logging;

import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Collects log4j LoggingEvent instances and stores them in a List.
 * @author dgrether
 *
 */
public class CollectLogMessagesAppender extends AppenderSkeleton {

    private List<LoggingEvent> logEvents = new LinkedList<LoggingEvent>();

    /**
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
    @Override
    protected void append(LoggingEvent e) {
        logEvents.add(e);
    }

    /**
	 * @see org.apache.log4j.Appender#close()
	 */
    public void close() {
        this.logEvents.clear();
    }

    /**
	 * @see org.apache.log4j.Appender#requiresLayout()
	 */
    public boolean requiresLayout() {
        return false;
    }

    public List<LoggingEvent> getLogEvents() {
        return this.logEvents;
    }
}
