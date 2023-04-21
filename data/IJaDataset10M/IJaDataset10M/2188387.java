package org.ikasan.framework.monitor;

/**
 * Ikasan Abstract monitor listener.
 * 
 * @author Ikasan Development Team
 */
public abstract class AbstractMonitorListener implements MonitorListener {

    /** Monitor listener name */
    private String name;

    /**
     * Constructor
     * 
     * @param name The name of the monitor
     */
    public AbstractMonitorListener(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract void notify(String state);
}
