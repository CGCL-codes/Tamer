package lelouet.datacenter.simulation.eventhandlers;

import java.util.List;
import lelouet.datacenter.simulation.Event;
import org.slf4j.LoggerFactory;

/**
 * Logs the events on the slf4j logger
 * 
 * @author lelouet
 * 
 */
public class EventLogger extends DiscardEventHandler {

    private static org.slf4j.Logger mlogger = LoggerFactory.getLogger(EventLogger.class);

    @Override
    public List<Event> handleEvent(Event event) {
        mlogger.info("received {}", event);
        return super.handleEvent(event);
    }
}
