package com.google.code.task;

class TaskAndEvent {

    private Task task;

    private TaskEvent event;

    TaskAndEvent(Task task, TaskEvent event) {
        super();
        this.task = task;
        this.event = event;
    }

    Task getTask() {
        return task;
    }

    TaskEvent getEvent() {
        return event;
    }

    boolean isThereEvent() {
        return event == null ? false : true;
    }
}
