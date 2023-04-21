package protopeer.measurement;

import java.util.*;
import org.apache.log4j.*;
import protopeer.*;
import protopeer.time.*;
import protopeer.time.Timer;
import protopeer.util.quantities.*;

/**
 * 
 * The main class used for logging the measurements. Handles the transitions
 * between the measurement epochs. <code>MeasurementLoggerListener</code> can be
 * registered to get notified when the measurement epochs end.
 * 
 * The duration of the measurement epoch is set via
 * <code>MainConfiguration.measurementEpochDuration</code>.
 */
public class MeasurementLogger {

    private static final Logger logger = Logger.getLogger(MeasurementLogger.class);

    private final LinkedList<MeasurementLoggerListener> listeners = new LinkedList<MeasurementLoggerListener>();

    private final Clock clock;

    private final Timer epochEndTimer;

    private MeasurementLog measurementLog;

    /**
	 * Creates a measurement logger that will use the supplied clock for
	 * dividing the time into the measurement epochs.
	 * 	 * 
	 * @param clock
	 */
    public MeasurementLogger(Clock clock) {
        this(clock, new MeasurementLog());
    }

    public MeasurementLogger(Clock clock, MeasurementLog measurementLog) {
        Clock.measurementLogger = this;
        double currentTime = clock.getCurrentTime();
        this.clock = clock;
        this.measurementLog = measurementLog;
        this.epochEndTimer = clock.createNewTimer();
        epochEndTimer.addTimerListener(new TimerListener() {

            public void timerExpired(Timer timer) {
                if (timer == epochEndTimer) {
                    fireMeasurmentEpochEnded(getCurrentLog(), (int) (Time.inMeasurementEpochs(Time.inMilliseconds(getClock().getCurrentTime()))) - 1);
                    logger.debug("measurement logging epochEndTimer fired: " + getClock().getCurrentTime());
                    epochEndTimer.schedule(getTimeUntilNextEpoch(getClock().getCurrentTime()));
                }
            }
        });
        epochEndTimer.schedule(getTimeUntilNextEpoch(currentTime));
    }

    private Clock getClock() {
        return clock;
    }

    private double getTimeUntilNextEpoch(double currentTime) {
        return (Math.floor(currentTime / MainConfiguration.getSingleton().measurementEpochDuration) + 1) * MainConfiguration.getSingleton().measurementEpochDuration - currentTime + 20.0;
    }

    private MeasurementLog getCurrentLog() {
        return measurementLog;
    }

    public void addMeasurementLoggerListener(MeasurementLoggerListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeMeasurmentLoggerListener(MeasurementLoggerListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    private void fireMeasurmentEpochEnded(MeasurementLog log, int epochNumber) {
        synchronized (listeners) {
            for (MeasurementLoggerListener listener : listeners) {
                listener.measurementEpochEnded(log, epochNumber);
            }
        }
    }

    private int getCurrentEpochNumber() {
        return (int) (Time.inMeasurementEpochs(Time.inMilliseconds(getClock().getCurrentTime())));
    }

    /**
	 * Log some measured <code>value</code> tagged with three tags.
	 * 
	 * @param tag1
	 * @param tag2
	 * @param tag3
	 * @param value
	 */
    public void log(Object tag1, Object tag2, Object tag3, double value) {
        getCurrentLog().log(getCurrentEpochNumber(), tag1, tag2, tag3, value);
    }

    /**
	 * Log some measured <code>value</code> tagged with two tags.
	 * 
	 * @param tag1
	 * @param tag2
	 * @param value
	 */
    public void log(Object tag1, Object tag2, double value) {
        getCurrentLog().log(getCurrentEpochNumber(), tag1, tag2, value);
    }

    /**
	 * Log some measured <code>value</code> tagged with one tag.
	 * 
	 * @param tag1
	 * @param value
	 */
    public void log(Object tag, double value) {
        getCurrentLog().log(getCurrentEpochNumber(), tag, value);
    }

    /**
	 * Log some measured <code>value</code> tagged with a set of tags.
	 */
    public void logTagSet(Set<Object> tags, double value) {
        getCurrentLog().logTagSet(getCurrentEpochNumber(), tags, value);
    }

    /**
	 * Returns the associated measurement log to which this logger logs all the
	 * values.
	 * 
	 * @return
	 */
    public MeasurementLog getMeasurementLog() {
        return measurementLog;
    }
}
