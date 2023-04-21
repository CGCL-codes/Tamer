package com.opnworks.model;

import java.util.Vector;

/**
 * Class that plays the role of the domain model in the TableViewerExample In
 * real life, this class would access a persistent store of some kind.
 */
public class ExampleTaskList {

    private final int COUNT = 10;

    private Vector<ExampleTask> tasks = new Vector<ExampleTask>(this.COUNT);

    public ExampleTaskList() {
        super();
        this.initData();
    }

    private void initData() {
        ExampleTask task;
        for (int i = 0; i < this.COUNT; i++) {
            task = new ExampleTask("Task " + i);
            this.tasks.add(task);
        }
    }

    public Vector<ExampleTask> getTasks() {
        return this.tasks;
    }
}
