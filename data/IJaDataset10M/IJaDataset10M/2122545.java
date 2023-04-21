package taskblocks.graph;

import java.util.Comparator;

/**
 * Compares tasks according to their starting time
 * 
 * @author jakub
 *
 */
public class TaskStartTimeComarator implements Comparator<Task> {

    public int compare(Task t1, Task t2) {
        return (int) (t1.getStartTime() - t2.getStartTime());
    }
}
