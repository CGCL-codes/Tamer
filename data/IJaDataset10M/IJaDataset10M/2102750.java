package com.jetigy.magicbus.event.bus.queue;

public class SynchronizedEventTask extends EventTask {

    /**
   * 
   * @param task
   * @param notify
   */
    public SynchronizedEventTask(Task task, boolean notify) {
        super(task, notify);
    }

    /**
   * 
   * @param task
   */
    public SynchronizedEventTask(Task task) {
        this(task, false);
    }

    protected void notifyTarget(Task task) {
    }
}
