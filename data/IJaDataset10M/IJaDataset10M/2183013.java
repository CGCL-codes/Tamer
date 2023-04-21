package uchicago.src.sim.engine;

/**
 * Executes within a parent <code>Schedule</code> and manages the
 * execution of <code>BasicActions</code> according to an internal clock.
 * A SubSchedule allows a user to further divide the ticks of a parent
 * Schedule by some specified number. Certain aspects of the model can
 * then occur "faster" than other elements.<p>
 *
 * A SubSchedule is typically scheduled against a Schedule to execute at some
 * tick t. When the SubSchedule exectutes at t, it then iterates over its
 * own scheduled actions a specified number of times. So, for example,
 * a SubSchedule may execute all its actions three times for every tick
 * of its parent Schedule.<p>
 *
 * The actions scheduled on a SubSchedule will iterate with a simulated
 * concurrency (i.e. in random order). If the actions should be executed
 * in some specified order, the actions should be added to an ActionGroup
 * set for sequential execution. This ActionGroup can then be added to
 * the SubSchedule for execution. Specifying the order in the
 * scheduleActionAt and scheduleActionAtInterval methods can be used to
 * insure that certain actions occur after other actions.
 *
 * @author Nick Collier (Modified by Michael J. North)
 * @version $Revision: 1.7 $ $Date: 2004/11/03 19:50:57 $
 * @deprecated
 *
 * @see Schedule
 * @see BasicAction
 * @see ActionGroup
 */
public class SubSchedule extends ScheduleBase {

    private long iterations = 1;

    /**
   * Constructs a SubSchedule with a default interval of 1, and with the
   * specified number of times to execute its scheduled actions per tick
   * of the parent Schedule
   *
   * @param iterations the number of times to execute scheduled actions per
   * tick of the parent schedule
   */
    public SubSchedule(long iterations) {
        this(1, iterations);
    }

    /**
   * Constructs a SubSchedule with the specifed interval, and with the
   * specified number of times to execute its scheduled actions per tick
   * of the parent Schedule
   *
   * @param interval the execution interval
   * @param iterations the number of times to execute scheduled actions per
   * tick of the parent schedule
   */
    public SubSchedule(double interval, long iterations) {
        super(interval);
        this.iterations = iterations;
    }

    /**
   * Sets the number of iterations of this SubSchedule's action per
   * tick of the parent Schedule.
   *
   * @param iterations the number of times to execute my scheduled actions per
   * tick of the parent schedule
   */
    public void setIterations(long iterations) {
        this.iterations = iterations;
    }

    /**
   * Gets the number of times this SubSchedule's actions will execute per
   * parent Schedule tick.
   */
    public long getIterations() {
        return iterations;
    }

    /**
   * Executes this SubSchedules actions the specified number of times.
   */
    public void execute() {
    }
}
