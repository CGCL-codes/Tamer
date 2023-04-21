package com.antmanager.jobs.test;

import org.apache.tools.ant.Project;
import com.antmanager.jobs.Job;

public class FakeJob extends Job {

    private boolean isDone = false;

    public FakeJob(String jobId, Project project) {
        super(jobId, project);
    }

    public synchronized void execute() {
        for (int i = 0; i < 100; i++) {
            System.err.println(i);
            Thread.yield();
        }
        System.err.println("+FINISH+");
        this.isDone = true;
    }

    public boolean isDone() {
        return this.isDone;
    }
}
