package org.ikasan.framework.initiator;

/**
 * Simple initiator state implementation.
 * 
 * @author Ikasan Development Team
 *
 */
public class InitiatorState {

    /** Stopped State - just stopped */
    public static final InitiatorState STOPPED = new InitiatorState("stopped", true, false, false, false);

    /** Running State - just running */
    public static final InitiatorState RUNNING = new InitiatorState("running", false, true, false, false);

    /** Recovering State = running, but in recovery */
    public static final InitiatorState RECOVERING = new InitiatorState("runningInRecovery", false, true, true, false);

    /** Error State - stopped, but in error */
    public static final InitiatorState ERROR = new InitiatorState("stoppedInError", true, false, false, true);

    /** state name */
    private final String name;

    /** stopped state */
    private final boolean stopped;

    /** running state */
    private final boolean running;

    /** recovering state */
    private final boolean recovering;

    /** error state */
    private final boolean error;

    /**
     * Constructor
     * @param name 
     * @param stopped 
     * @param running
     * @param recovering
     * @param error
     */
    protected InitiatorState(final String name, final boolean stopped, final boolean running, final boolean recovering, final boolean error) {
        this.name = name;
        this.stopped = stopped;
        this.running = running;
        this.recovering = recovering;
        this.error = error;
    }

    /**
     * Is this a stopped normally state
     * @return boolean
     */
    public boolean isStopped() {
        return this.stopped;
    }

    /**
     * Is this a running normally state
     * @return boolean
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Is this a running but in 'recovery' state
     * @return boolean
     */
    public boolean isRecovering() {
        return this.recovering;
    }

    /**
     * Is this a stopped in 'error' state
     * @return boolean
     */
    public boolean isError() {
        return this.error;
    }

    /**
     * Get the state name
     * @return name
     */
    public String getName() {
        return this.name;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getName() + " [");
        sb.append("name=");
        sb.append(name);
        sb.append("]");
        return sb.toString();
    }
}
