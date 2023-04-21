package com.beanstalktech.common.event;

/**
 * Interface providing descriptive information about a source
 * of an event.
 *
 */
public interface EventSource {

    /**
     * Sets the name of the event source.
     *
     * @param String representing the source name.
     */
    public void setEventSourceName(String eventSourceName);

    /**
     * Provides the name of the event source.
     *
     * @return String representing the source name.
     */
    public String getEventSourceName();
}
