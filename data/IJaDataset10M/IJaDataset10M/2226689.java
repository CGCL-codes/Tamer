package physis.core.task.math;

import physis.core.task.TaskAdapter;

public class Sub extends TaskAdapter {

    public boolean checkActivity(int[] in, int[] out) {
        return in[1] - in[2] == out[1];
    }

    public int getInputSize() {
        return 2;
    }

    public int getOutputSize() {
        return 1;
    }
}
