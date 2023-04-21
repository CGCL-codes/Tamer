package distrit.server;

import distrit.*;
import java.util.Vector;

/** This allows many InteractiveTasks to be bundled togather as one InteractiveTask. <p>
 * All tasks will be started simultaneously and the task as a whole will only
 * finish when all tasks have completed. <p>
 * For more flexibility in managing tasks dynamically use FineGrainedInteractiveTasks.
 *
 * @author Michael Garvie
 */
public class MultiInteractiveTask implements InteractiveTask {

    private Vector tasks;

    private Vector results = new Vector();

    private Vector threads = new Vector();

    /** Creates new MultiInteractiveTask
     * @param tasks Vector containing list of tasks to run simultaneously.
     */
    public MultiInteractiveTask(Vector tasks) {
        this.tasks = tasks;
    }

    /** Used to get output from the task
     * @param params If not null, then must be a Vector with what must be sent to each task.
     * @return a Vector holding what each task's get method returned.
     */
    public Object get(Object params) {
        Vector rv = new Vector();
        for (int tl = 0; tl < tasks.size(); tl++) {
            Object thisTaskParams = null;
            if (params != null) {
                thisTaskParams = ((Vector) params).elementAt(tl);
            }
            rv.add(((InteractiveTask) tasks.elementAt(tl)).get(thisTaskParams));
        }
        return rv;
    }

    /** Used to send input to the task
     * @param paramsAndWhat if not null, must be a Vector with what must be sent to each task
     */
    public void set(Object paramsAndWhat) {
        for (int tl = 0; tl < tasks.size(); tl++) {
            Object thisTaskParams = null;
            if (paramsAndWhat != null) {
                thisTaskParams = ((Vector) paramsAndWhat).elementAt(tl);
            }
            ((InteractiveTask) tasks.elementAt(tl)).set(thisTaskParams);
        }
    }

    /** Starts all tasks in their own thread
     */
    public Object run(Object params) throws InterruptedException {
        for (int tl = 0; tl < tasks.size(); tl++) {
            results.add(null);
            final int ti = tl;
            Thread t = new Thread() {

                public void run() {
                    try {
                        results.add(ti, ((InteractiveTask) tasks.elementAt(ti)).run(null));
                    } catch (InterruptedException e) {
                    }
                }
            };
            t.start();
            threads.add(t);
        }
        for (int tl = 0; tl < tasks.size(); tl++) {
            if (Thread.currentThread().isInterrupted()) {
                getThread(tl).interrupt();
            } else {
                try {
                    System.out.println("Waiting for task " + tl);
                    ((Thread) threads.elementAt(tl)).join();
                } catch (InterruptedException e) {
                    getThread(tl).interrupt();
                }
            }
        }
        return results;
    }

    private InteractiveTask getTask(int pos) {
        if (tasks.size() <= pos) {
            tasks.setSize(pos + 1);
            threads.setSize(pos + 1);
            results.setSize(pos + 1);
        }
        return ((InteractiveTask) tasks.elementAt(pos));
    }

    private Thread getThread(int pos) {
        return ((Thread) threads.elementAt(pos));
    }

    public String toString() {
        String rv = "";
        for (int i = 0; i < tasks.size(); i++) {
            rv += "\nTask " + i + ":\n" + tasks.elementAt(i) + "\n";
        }
        return rv;
    }
}
