package org.jmonit.events;

/**
 * Monitoring event for data exposed by the application to the monitor.
 * 
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public class DataMonitoredEvent extends MonitoringEvent {

    private long data;

    /**
     * Constructor
     * 
     * @param data the data exposed by the application
     */
    public DataMonitoredEvent(long data) {
        super();
        this.data = data;
    }

    /**
     * @return the data exposed by the application
     */
    public long getData() {
        return data;
    }
}
