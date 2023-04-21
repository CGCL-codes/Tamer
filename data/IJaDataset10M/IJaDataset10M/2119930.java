package org.kunlong.netix.processor.standardprocessor;

import java.util.LinkedList;
import java.util.Queue;

public class JobQueue {

    private Queue<Job> jobQueue = new LinkedList<Job>();

    public void addJob(Job j) {
        jobQueue.add(j);
    }

    public Job getJob() {
        return jobQueue.remove();
    }

    public int size() {
        return jobQueue.size();
    }
}
