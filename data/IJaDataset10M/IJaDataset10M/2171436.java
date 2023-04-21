package oneiro.server;

import java.util.*;
import java.lang.reflect.*;
import oneiro.server.ai.*;
import oneiro.server.entity.*;

/** 
 * A job describes a method call. Jobs are placed in priority queues
 * of type <code>JobQueue</code> to allow task scheduling.
 *
 * @author Markus Mårtensson
 * @see JobQueue
 */
public abstract class Job implements Runnable, Comparable {

    public final int LW_PRIORITY = 50;

    public final int MD_PRIORITY = 100;

    public final int HI_PRIORITY = 500;

    /** Priority of the message. */
    protected int prio = MD_PRIORITY;

    /** 
     * Time of execution. If set to 0 then the Job is executed as soon as
     * it reaches the top of the queue, otherwise the specified time must
     * be exceeded by <code>System.currentTimeMillis()</code>.
     */
    protected long time = 0;

    /** 
     * Constructs a <code>Job</code>, without delay.
     */
    public Job() {
    }

    /** 
     * Return the time of execution. 
     *
     * @return          the time of execution.
     */
    public long getTime() {
        return time;
    }

    /**
     * A high priority (greater than or equal to HI_PRIORITY) means
     * the job is guaranteed a thread. 
     *
     * @return         true if the job has a high enough priority.
     */
    public boolean hasHighPriority() {
        return prio >= HI_PRIORITY;
    }

    /** 
     * Invokes the Jobs method and parameters on its object. 
     */
    public void begin() {
        Thread t = new Thread();
        try {
            t.setPriority(Thread.NORM_PRIORITY);
        } catch (IllegalArgumentException e) {
            Log.debug("Job priority out of bounds.");
        } catch (SecurityException e) {
            Log.debug("Not allowed to change job priority.");
        }
        t.start();
    }

    /** 
     * Returns a negative integer, zero, or a positive integer as this
     * job should be executed earlier, at the same time or later than
     * the specified Job.
     *
     * @param other     another Job to compare with.
     * @return          a negative integer, zero, or a positive 
     *                  integer as this Job is to be executed before,
     *                  at the same time or after the other Job.
     */
    public int compareTo(Object other) throws ClassCastException {
        return (int) (time - ((Job) other).time);
    }

    /** 
     * Delays the Job until the specified time has come.
     *
     * @param time      a moment in time given exceeding that of 
     *                  System.currentTimeMillis().
     * @return          this Job.
     */
    public Job delay(long time) {
        this.time = time;
        return this;
    }

    /**
     * Adds the Job to the JobQueue.
     *
     * @return          this Job.
     */
    public Job queue() {
        Server.addJob(this);
        return this;
    }
}
