package dcf.tasks;

import java.io.*;
import dcf.server.*;

/**
 * This will solve a the simple task - It returns the square of the integer given<br>
 * @author Tal Salmona
 */
public class SimpleSolver implements Solver, Serializable {

    Task task;

    public SimpleSolver() {
    }

    public void setFor(Task t) {
        task = t;
    }

    public void solve(Task t) {
        while (t.hasMore()) {
            Object o = t.next();
            if (o instanceof Integer) {
                int i = ((Integer) o).intValue();
                setResult(new Integer(i * i));
            }
        }
    }

    public Task getTask() {
        return task;
    }

    public void setResult(Object obj) {
        task.addResult(obj);
    }
}
