package perun.isle.task.advanced;

import java.util.List;
import perun.isle.task.Task;

/**
 * Reverse <code>Task</code>. Checks if organism is able to read <b>n1</b> followed by <b>n2</b>
 * and write <b>n2</b> followed by <b>n1</b> (perform reverse)
 */
public class Reverse extends Task {

    /** 
	 * Checks if <code>Task</code> was performed
	 */
    public boolean checkActivity(List<Integer> in, List<Integer> out, boolean performedOutput) {
        return performedOutput && (in.get(0) == out.get(1)) && (in.get(1) == out.get(0));
    }

    public int getInputSize() {
        return 2;
    }

    public int getOutputSize() {
        return 2;
    }
}
