package jstress.load;

import jstress.task.TaskCase;
import EDU.oswego.cs.dl.util.concurrent.Semaphore;
import EDU.oswego.cs.dl.util.concurrent.FIFOSemaphore;

/**
 *
 * @version $Id: LoadDependentTask.java,v 1.1 2004/04/19 16:13:35 cwensel Exp $
 */
public class LoadDependentTask extends TaskCase {

    float loadFactor = 2;

    float loadDelay = 400;

    float precision = 100;

    public LoadDependentTask() {
    }

    public void initTask() throws Exception {
    }

    protected void runTask() throws Exception {
        startTimer();
        for (int i = 0; i < loadDelay / precision; i++) {
            double concurrentTasks = getParent().currentRunningTasks();
            long delay = Math.round(precision + Math.pow(concurrentTasks * loadFactor, 2));
            Thread.sleep(delay);
        }
        stopTimer();
    }

    public void destroyTask() throws Exception {
    }
}
