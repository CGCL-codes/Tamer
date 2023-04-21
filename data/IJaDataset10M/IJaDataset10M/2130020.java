package mimosa.scheduler;

import java.util.Vector;

/**
 * A scheduler support provides the functionalities for managing listeners and firing the scheduler events.
 *  
 * @author Jean-Pierre Muller
 */
public class SchedulerSupport {

    private Vector<SchedulerListener> listeners = new Vector<SchedulerListener>();

    /**
     * The empty constructor.
     */
    public SchedulerSupport() {
        super();
    }

    /**
     * Adds a SchedulerListener to the list.
     * @param listener The SchedulerListener to add.
     */
    public void addSchedulerListener(SchedulerListener listener) {
        listeners.add(listener);
    }

    /**
     * Fires a change of the current date.
     * @param sched
     */
    public void fireCurrentDateChange(Scheduler sched) {
        SchedulerEvent evt = new SchedulerEvent(sched);
        for (SchedulerListener e : listeners) e.currentDateChanged(evt);
    }

    /**
     * Fires a change of the terminating date.
     * @param sched
     */
    public void fireEndDateChange(Scheduler sched) {
        SchedulerEvent evt = new SchedulerEvent(sched);
        for (SchedulerListener e : listeners) e.endDateChanged(evt);
    }

    /**
     * Fires a change in the scheduler state.
     * @param sched
     */
    public void fireStateChange(Scheduler sched) {
        SchedulerEvent evt = new SchedulerEvent(sched);
        for (SchedulerListener e : listeners) e.stateChanged(evt);
    }

    /**
     * Removes a SchedulerListener from the list.
     * @param listener The SchedulerListener to remove.
     */
    public void removeSchedulerListener(SchedulerListener listener) {
        listeners.remove(listener);
    }
}
