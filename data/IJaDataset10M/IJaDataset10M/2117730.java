package bzstats.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import bzstats.*;

/**
 * A sequential list of game events.
 * @author youth
 * @has 1 - * bzstats.event.GameEvent
 * @depend - "uses" - bzstats.LogReader
 *
 */
public class EventSequence extends ArrayList implements Period {

    /**
	 * Add a new event to the end.
	 * @param event The event to add.
	 */
    public void addEvent(final GameEvent event) {
        add(event);
    }

    /**
	 * Construct a gameperiod and fill it with data from a LogReader.
	 * @param reader
	 * @throws BZStatsException
	 */
    public EventSequence(final LogReader reader) throws BzStatsException {
        GameEvent event;
        try {
            event = reader.nextEvent();
            while (event != null) {
                addEvent(event);
                event = reader.nextEvent();
            }
        } catch (IOException e1) {
            throw new BzStatsException("Could not read logfile.", e1);
        }
    }

    /**
	 * Collect statistics from a GamePeriod.
	 * @param period The GamePeriod to analyze.
	 */
    public void collectStats(final PeriodStats stats) {
        final Iterator eventiter = iterator();
        GameEvent event;
        while (eventiter.hasNext()) {
            event = (GameEvent) eventiter.next();
            event.collectStats(stats);
        }
    }
}
