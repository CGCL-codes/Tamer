package com.pcmsolutions.system.tasking;

/**
 * User: paulmeehan
 * Date: 07-Aug-2004
 * Time: 11:38:52
 */
abstract class AbstractTask implements Task {

    private final String name;

    private final TaskQ queue;

    protected AbstractTask(String name, TaskQ queue) {
        this.name = name;
        this.queue = queue;
    }

    public abstract void cbCancelled();

    public abstract void cbFinished(Exception e);

    public final String getName() {
        return name;
    }

    public final TaskQ getQueue() {
        return queue;
    }
}
