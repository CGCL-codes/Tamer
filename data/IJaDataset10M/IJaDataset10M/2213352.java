package perun.isle.scheduler;

import perun.common.log.Log;
import perun.common.Exit;

/**
 * Creates scheduler according to configuration.
 */
public class SchedulerFactory {

    public static final String SCHEDULER_CLASS_NAME = "isle.scheduling.class_name";

    public static Scheduler getScheduler() {
        String scheduler_class = perun.common.configuration.Configuration.getStringProperty(SCHEDULER_CLASS_NAME);
        Log.event(Log.INFO, "Creating scheduler <" + scheduler_class + ">");
        Scheduler sched = null;
        try {
            Class sched_class = Class.forName(scheduler_class);
            sched = (Scheduler) sched_class.newInstance();
        } catch (ClassNotFoundException cnfe) {
            Log.event(Log.ERROR, "Scheduler class not found: " + scheduler_class);
            Exit.text_fatal("Scheduler creation failed");
        } catch (IllegalAccessException iae) {
            Log.event(Log.ERROR, "Class or zero-argument constructor not available: " + scheduler_class);
            Exit.text_fatal("Scheduler creation failed");
        } catch (InstantiationException cnfe) {
            Log.event(Log.ERROR, "Cannot instantiate: " + scheduler_class);
            Exit.text_fatal("Scheduler creation failed");
        }
        Log.event(Log.INFO, "Scheduler created");
        return sched;
    }
}
