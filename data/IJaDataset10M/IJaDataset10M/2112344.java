package jstress.run;

import java.util.Properties;
import jstress.task.Task;

/**
 * The InfiniteSerializedRun is a special case RunStrategy. It is intended to sequentially run the
 * same Task class indefinitely. Unlike other RunStrategies, only during a run() is an
 * Task instantiated, initialized, run, and destroyed, before the next Task is instantiated.
 * Note that the Task.sessionInit is called once. Task.sessionDestroy is never called.
 *
 * @version $Id: InfiniteSerializedRun.java,v 1.7 2004/04/19 19:25:15 cwensel Exp $
 */
public class InfiniteSerializedRun extends RunStrategy {

    public InfiniteSerializedRun() {
    }

    public InfiniteSerializedRun(Class taskClass) {
        super(taskClass);
    }

    public void initStrategy() {
    }

    public void runStrategy() {
        while (true) {
            initInstances(1);
            Task task = (Task) getTaskInstances().remove(0);
            try {
                task.run(this);
            } catch (Exception exception) {
                throw new RuntimeException("Error: could not run Task", exception);
            }
            destroyInstance(task);
        }
    }

    public void applyProperties(Properties properties) {
    }
}
