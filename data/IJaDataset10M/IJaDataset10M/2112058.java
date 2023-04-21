package com.ewansilver.raindrop;

/**
 * A quick extension of ThroughputEvent to allow us to to generate a range of
 * ThroughPutEvents for different time intervals.
 * 
 * @author ewan.silver AT gmail.com
 */
public class ThroughputEventTestThing extends ThroughputEvent {

    private long timeTaken;

    /**
	 * @param aNumberOfEvents
	 */
    public ThroughputEventTestThing(long aNumberOfEvents, long aTimeTaken) {
        super(aNumberOfEvents);
        timeTaken = aTimeTaken;
    }

    /** Time, in ms, to process all these events. */
    public long getTimeTaken() {
        return timeTaken;
    }
}
