package com.xavax.jsf.util;

/**
 * BasicEvent serves as a base class for events used by broadcasters
 * and observers and can also be used as a concrete class for simple
 * events that require no additional information other than the type
 * and source of the event.
 */
public class BasicEvent implements Event {

    /**
   * Construct a BasicEvent with the specified type.
   *
   * @param type  the type of this event.
   */
    public BasicEvent(int type) {
        this.type = type;
        this.source = null;
    }

    /**
   * Construct a BasicEvent with the specified type and source.
   *
   * @param source  the source of this event. 
   * @param type  the type of this event.
   */
    public BasicEvent(Broadcaster source, int type) {
        this.type = type;
        this.source = source;
    }

    /**
   * Returns the type of this event.
   *
   * @return the type of this event.
   */
    public int type() {
        return this.type;
    }

    /**
   * Returns the source of this event.
   *
   * @return the source of this event.
   */
    public Broadcaster source() {
        return this.source;
    }

    /**
   * Sets the source of this event.
   *
   * @param source  the source of this event.
   */
    public void source(Broadcaster source) {
        this.source = source;
    }

    /**
   * Returns a string representation of this event.
   *
   * @return a string representation of this event.
   */
    public String toString() {
        String result = "E(" + type + ")";
        return result;
    }

    protected int type;

    protected Broadcaster source;
}
