package edu.ds.p2p.app;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import edu.ds.p2p.message.CommMessage;

/**
 * This class represents the job repository. 
 * @author Team Falcon
 *
 */
public class JobQueue {

    private Map<Integer, CommMessage> remoteJobs = new HashMap<Integer, CommMessage>();

    private Map<Integer, String> localJobs = new HashMap<Integer, String>();

    private Queue<Integer> localJobQueue = new LinkedList<Integer>();

    private Queue<Integer> remoteJobQueue = new LinkedList<Integer>();

    /**
	 * adds job to local queue
	 * @param description discriptor file name
	 * @param jobID job id
	 */
    public synchronized void addJobToLocalQueue(String description, int jobID) {
        this.localJobs.put(jobID, description);
        this.localJobQueue.add(jobID);
    }

    private static JobQueue queue;

    private JobQueue() {
    }

    public static JobQueue getJobQueue() {
        if (queue == null) {
            queue = new JobQueue();
        }
        return queue;
    }

    public synchronized boolean hasLocalJobs() {
        return !this.localJobQueue.isEmpty();
    }

    public synchronized int getJob() {
        int job = this.localJobQueue.remove();
        return job;
    }

    public synchronized String getJobDescription(int jobId) {
        String str = this.localJobs.get(jobId);
        this.localJobs.remove(jobId);
        return str;
    }

    /**
	 * This method adds remote job to the queue
	 * @param msg CommMessage containing the executable files and job properties
	 * @param jobid remote job id
	 */
    public synchronized void addRemoteJob(CommMessage msg, int jobid) {
        this.remoteJobs.put(jobid, msg);
        this.remoteJobQueue.add(jobid);
    }

    public synchronized int getRemoteJob() {
        return this.remoteJobQueue.remove();
    }

    public synchronized boolean hasRemoteJobs() {
        return !this.remoteJobQueue.isEmpty();
    }

    public synchronized CommMessage getRemoteJobDiscription(int jobid) {
        return this.remoteJobs.get(jobid);
    }

    public synchronized Set<Integer> getLocalJobs() {
        return this.localJobs.keySet();
    }

    public synchronized boolean hasLocalJob(Integer i) {
        return this.localJobQueue.contains(i);
    }
}
