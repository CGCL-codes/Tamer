package net.sf.groofy.jobs;

import java.util.ArrayList;
import org.eclipse.swt.widgets.Composite;

public class JobManager {

    private static JobManager INSTANCE;

    public static JobManager getInstance() {
        if (INSTANCE == null) INSTANCE = new JobManager();
        return INSTANCE;
    }

    private ArrayList<Job> jobQueue = new ArrayList<Job>();

    private Composite parentComposite;

    private ArrayList<IRunningIndicator> runningIndicators = new ArrayList<IRunningIndicator>();

    public void addRunningIndicator(IRunningIndicator runningIndicator) {
        runningIndicators.add(runningIndicator);
    }

    public void removeRunningIndicator(IRunningIndicator runningIndicator) {
        runningIndicators.remove(runningIndicator);
    }

    public void setUiContainer(Composite parentComposite) {
        this.parentComposite = parentComposite;
    }

    public void terminateJobs() {
        for (Job job : jobQueue) {
            job.cancel();
        }
    }

    void addJob(Job job) {
        jobQueue.add(job);
        if (jobQueue.size() == 1) for (IRunningIndicator indicator : runningIndicators) indicator.startRunningIndicator();
    }

    Composite getUiContainer() {
        return parentComposite;
    }

    void removeJob(Job job) {
        jobQueue.remove(job);
        if (jobQueue.isEmpty()) for (IRunningIndicator indicator : runningIndicators) indicator.stopRunningIndicator();
    }
}
