package com.googlecode.jazure.sdk.task.tracker;

import com.googlecode.jazure.sdk.task.TaskInvocation;

public class TaskNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 5637043244007234019L;

    private final TaskInvocation invocation;

    public TaskNotFoundException(TaskInvocation invocation) {
        this.invocation = invocation;
    }

    public TaskInvocation getInvocation() {
        return invocation;
    }
}
