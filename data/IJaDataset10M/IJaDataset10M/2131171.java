package net.sourceforge.jrcom.test.simple;

import net.sourceforge.jrcom.TaskResult;

public class IntegerTaskResult implements TaskResult<Integer> {

    private static final long serialVersionUID = 8295516926368068010L;

    private final int result;

    public IntegerTaskResult(int result) {
        this.result = result;
    }

    @Override
    public Integer getResult() {
        return result;
    }
}
