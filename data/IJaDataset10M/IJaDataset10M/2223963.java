package uk.ac.ed.rapid.jobsubmission.impl;

import java.util.HashMap;
import java.util.Map;
import uk.ac.ed.rapid.data.RapidData;
import uk.ac.ed.rapid.exception.RapidException;
import uk.ac.ed.rapid.jobdata.JobID;
import uk.ac.ed.rapid.jobdata.VariableResolver;
import uk.ac.ed.rapid.jobsubmission.JobQueue;
import uk.ac.ed.rapid.jobsubmission.JobQueueTable;
import uk.ac.ed.rapid.jobsubmission.jobmanager.JobManager;
import uk.ac.ed.rapid.jobsubmission.jobmanager.JobManagerFactory;

public class JobQueueTableImpl implements JobQueueTable {

    private Map<String, JobQueue> jobQueueTable = new HashMap<String, JobQueue>();

    public JobQueueTableImpl() {
    }

    public JobQueue getJobQueue(String name) throws RapidException {
        JobQueue queue = this.jobQueueTable.get(name);
        if (queue == null) throw new RapidException("No queue with name " + name); else return this.jobQueueTable.get(name);
    }

    public int size() {
        return this.jobQueueTable.size();
    }

    public void addJobQueue(String name, JobManagerFactory factory) throws RapidException {
        JobQueue queue = new JobQueueImpl(factory);
        this.jobQueueTable.put(name, queue);
    }

    private JobQueue getJobQueue(RapidData rapidData, JobID jobID) throws RapidException {
        String server = VariableResolver.resolve(rapidData.getJob().getSubmissionServer(), rapidData.getJobData(jobID));
        JobQueue queue = this.getJobQueue(server);
        if (queue == null) throw new RapidException("Could not find a server with name " + server); else return queue;
    }

    public JobManager addJobManager(RapidData rapidData, JobID jobID) throws RapidException {
        return this.getJobQueue(rapidData, jobID).addJob(rapidData, jobID);
    }

    public JobManager getJobManager(RapidData rapidData, JobID jobID) throws RapidException {
        return this.getJobQueue(rapidData, jobID).getJob(jobID);
    }
}
