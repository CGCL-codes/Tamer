package com.google.code.task;

import static com.google.code.task.TaskResultType.*;

public class AspectDecorator extends AbstractFluentTask {

    private Task before;

    private Task task;

    private Task after;

    public AspectDecorator(Task task) {
        this.task = task;
    }

    public TaskResultType execute() {
        if (before != null) {
            validateTaskResult(before.execute());
        }
        validateTaskResult(task.execute());
        if (after != null) {
            validateTaskResult(after.execute());
        }
        return TaskResultType.SUCCESS;
    }

    public AspectDecorator before(Task before) {
        this.before = before;
        return this;
    }

    public AspectDecorator after(Task after) {
        this.after = after;
        return this;
    }

    Task getBefore() {
        return before;
    }

    Task getTask() {
        return task;
    }

    Task getAfter() {
        return after;
    }
}
