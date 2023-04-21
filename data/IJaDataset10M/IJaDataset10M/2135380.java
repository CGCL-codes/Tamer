package com.softserveinc.edu.training.webserver.taskbuilder;

import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue extends LinkedBlockingQueue<Runnable> {

    private static final long serialVersionUID = 1L;

    private int capacity;

    public TaskQueue(int capacity) {
        super(capacity);
        this.capacity = capacity;
    }

    public boolean offer(Runnable r) {
        if (size() <= capacity) {
            System.out.println("The task was added to the queue");
            return super.offer(r);
        }
        return false;
    }

    public Runnable take() throws InterruptedException {
        return super.take();
    }

    public int remainingCapacity() {
        return super.remainingCapacity();
    }
}
