package de.ibis.permoto.solver.sim.tech.simEngine1.queueNet;

/**
 * This class declares all the constants related to message event types.
 * @author Francesco Radaelli
 */
public class NetEvent {

    static final int EVENT_MASK = 0x0000FFFF;

    /** Event ID: Aborts the simulation.*/
    public static final int EVENT_ABORT = 0x0000;

    /** Event ID: Stops the simulation.*/
    public static final int EVENT_STOP = 0x0001;

    /** Event ID: Starts the simulation.*/
    public static final int EVENT_START = 0x0002;

    /** Event ID: The event contains a job.*/
    public static final int EVENT_JOB = 0x0004;

    /** Event ID: Job ack event.*/
    public static final int EVENT_ACK = 0x0008;

    /** Event ID: Keeps node awake.*/
    public static final int EVENT_KEEP_AWAKE = 0x0010;

    /** Event ID: Job sub service ack event */
    public static final int EVENT_SUB_ACK = 0x0020;

    /** Event ID: Job sub service rollback event */
    public static final int EVENT_SUB_ROL = 0x0040;

    /** The last log replayer job was created. Used for scheduler to mark the last event. */
    public static final double NO_MORE_REPLAY_JOB_DELAY = -1.0;
}
