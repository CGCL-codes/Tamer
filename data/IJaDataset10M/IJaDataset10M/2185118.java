package org.globus.gatekeeper.jobmanager;

public abstract class JobDoneListener implements JobStatusListener {

    public int getStatusMask() {
        return 0;
    }

    public String getID() {
        return "not important";
    }

    public abstract void dispose();

    public void statusChanged(JobManager jobManager) {
    }

    public String toString() {
        return getClass().getName();
    }
}
