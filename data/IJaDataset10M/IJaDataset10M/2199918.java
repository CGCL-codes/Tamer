package net.wgen.op.event;

import org.apache.log4j.Level;
import java.util.*;

/**
 * Configures an event type with thresholds to dynamically asses logger levels according to the
 * incoming timings.
 *
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: EventLoggingConfig.java 22 2007-06-26 01:11:24Z paulfeuer $
 */
public class EventLoggingConfig {

    protected final SortedMap<Long, Level> _timingToLevel = new TreeMap<Long, Level>(Collections.reverseOrder());

    protected List<Level> _availableLevels = Arrays.asList(Level.toLevel("TRACE"), Level.DEBUG, Level.WARN);

    public EventLoggingConfig() {
    }

    /**
     * Sets the sequence of levels to use for this type. These available levels handle timings that
     * don't get hit by the thresholds. If you set thresholds of 500ms/WARN, 200ms/DEBUG, what
     * happens to events at 100ms? These levels aid in that, so you could say log anything below at
     * TRACE. By default, the configured levels are TRACE, DEBUG, WARN.
     *
     * @param levels the levels to use as fallbacks when none of the thresholds are hit
     */
    public void setLevelOrder(String[] levels) {
        List<Level> list = new ArrayList<Level>(levels.length);
        for (String levelStr : levels) list.add(Level.toLevel(levelStr));
        synchronized (_availableLevels) {
            _availableLevels.clear();
            _availableLevels.addAll(list);
        }
    }

    /**
     * Sets a timing threshold for logger level, such that one can say at events taking longer than
     * 500 ms should be logged at the WARN level. 
     *
     * @param triggerThreshold a timing threshold above which the given level will be used
     * @param level the level associated with the threshold
     */
    public void registerThresholdForLevel(long triggerThreshold, Level level) {
        synchronized (_timingToLevel) {
            _timingToLevel.put(triggerThreshold, level);
        }
    }

    public void registerPropertyForLevel(String property, String value, Level level) {
        registerThresholdForLevel(Long.valueOf(value), level);
    }

    /**
     * Get the log level appropriate for this event type when processing a timing event of some
     * length. In other words, if an event took 400 ms, should it be logged at DEBUG or WARN?
     *
     * @param event the event to evaluate for a level
     *
     * @return the logger level configured for this timing
     */
    public Level getLevelForValue(Event event) {
        return getThresholdTriggeredLevel(event.getTiming(), _timingToLevel);
    }

    protected Level getThresholdTriggeredLevel(Number value, SortedMap<Long, Level> thresholds) {
        Iterator<Map.Entry<Long, Level>> it = thresholds.entrySet().iterator();
        Level lowestLevelConfigured = null;
        while (it.hasNext()) {
            Map.Entry<Long, Level> entry = it.next();
            Long triggerThreshold = entry.getKey();
            if (lowestLevelConfigured == null || lowestLevelConfigured.isGreaterOrEqual(entry.getValue())) {
                lowestLevelConfigured = entry.getValue();
            }
            if (value.longValue() >= triggerThreshold.longValue()) {
                return entry.getValue();
            }
        }
        if (lowestLevelConfigured != null) {
            int idx = _availableLevels.indexOf(lowestLevelConfigured) - 1;
            if (idx < 0) idx = 0;
            return _availableLevels.get(idx);
        }
        return _availableLevels.get(0);
    }
}
