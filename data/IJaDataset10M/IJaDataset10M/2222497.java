package cowsultants.itracker.web.scheduler;

import java.util.*;

/**
  * This class contains utilities for dealing with the Scheduler and Scheduler tasks.
  */
public class SchedulerUtilities {

    public static final String TASK_CLASS_REMINDER = "cowsultants.itracker.web.scheduler.tasks.ReminderNotification";

    public static final String TASK_KEY_REMINDER = "itracker.web.admin.scheduler.task.reminder";

    private static HashMap definedTasks;

    static {
        definedTasks = new HashMap();
        definedTasks.put(TASK_CLASS_REMINDER, TASK_KEY_REMINDER);
    }

    public SchedulerUtilities() {
    }

    /**
      * This returns a HashMap of defined task classes and their associated resource bundle key.
      * @return a HashMap with the classes and keys of predefined tasks
      */
    public static HashMap getDefinedTasks() {
        return definedTasks;
    }

    /**
      * Returns the key associated with a class, or null if that class has not been defined.
      * @param className the name of the class including the package to look up
      * @return a sting containing the reosuce bundle key, or null if the class is not defined
      */
    public static String getClassKey(String className) {
        if (definedTasks.containsKey(className)) {
            return (String) definedTasks.get(className);
        }
        return null;
    }
}
