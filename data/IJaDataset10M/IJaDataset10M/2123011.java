package com.sun.midp.events;

import java.util.Vector;

/**
 * An implementation of an EventListener that records the events passed to its 
 * process() and preprocess() methods.
 */
class InstrumentedEventListener implements EventListener {

    /** The value preprocess() should return. */
    boolean preprocess;

    /** Accumulates events passed to process(). */
    Vector processedEvents;

    /** Accumulates events passed to preprocess(). */
    Vector preprocessedEvents;

    /** Accumulates the waitingEvents passed to preprocess(). */
    Vector preprocessedWaitingEvents;

    /**
     * Creates a new InstrumentedEventListener, setting
     * the return value of its preprocess() method to be
     * newPreprocess.
     */
    public InstrumentedEventListener(boolean newPreprocess) {
        processedEvents = new Vector();
        preprocessedEvents = new Vector();
        preprocessedWaitingEvents = new Vector();
        preprocess = newPreprocess;
    }

    /**
     * Creates a new InstrumentedEventListener whose preprocess()
     * method returns true.
     */
    public InstrumentedEventListener() {
        this(true);
    }

    /**
     * The preprocess() method of EventListener.  Records the event and 
     * the waitingEvent in the corresponding vectors.  Returns the current 
     * value of preprocess.
     */
    public boolean preprocess(Event event, Event waitingEvent) {
        preprocessedEvents.addElement(event);
        preprocessedWaitingEvents.addElement(waitingEvent);
        return preprocess;
    }

    /**
     * Sets the return value of the preprocess() method.
     */
    public void setPreprocess(boolean p) {
        preprocess = p;
    }

    /**
     * The process() method of EventListener.  Simply records the given event 
     * in the processedEvents vector.
     */
    public void process(Event event) {
        processedEvents.addElement(event);
    }

    /**
     * Returns an array of events recorded by the process() method.
     */
    public Event[] getProcessedEvents() {
        return getArray(processedEvents);
    }

    /**
     * Returns an array of events recorded by the preprocess() method.
     */
    public Event[] getPreprocessedEvents() {
        return getArray(preprocessedEvents);
    }

    /**
     * Returns an array of waiting events recorded by the preprocess() method.
     * Note that there will likely be null elements within this array.
     */
    public Event[] getWaitingEvents() {
        return getArray(preprocessedWaitingEvents);
    }

    /**
     * Returns an array of events, given a vector of events.
     */
    Event[] getArray(Vector v) {
        Event eva[] = new Event[v.size()];
        v.copyInto(eva);
        return eva;
    }
}
