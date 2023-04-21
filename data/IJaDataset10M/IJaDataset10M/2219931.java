package com.n4.threading.recurring;

import java.util.Iterator;
import com.n4.threading.Scheduler;

/**
 * Implementation of Scheduler which panders to Recurring Jobs.
 */
public class RecurringScheduler extends Scheduler {

    /**
     * If ID is already in the scheduler, then return true.
     * @param jobID
     * @return
     */
    public boolean IDExists(String jobID) {
        return jobMap.containsKey(jobID);
    }

    /**
	 * TODO: Rather than being a LinkedList, make this a PriorityQueue.
	 * @return the number of milliseconds until the next Job is to be run.
	 */
    protected synchronized long runJobs() {
        long minDiff = Long.MAX_VALUE;
        long now = System.currentTimeMillis();
        Iterator jobNodes = jobList.iterator();
        if (jobNodes.hasNext()) {
            RecurringJobNode jobNode = (RecurringJobNode) jobNodes.next();
            do {
                if (jobNode.getExecutionTime().getTimeInMillis() <= now) {
                    Thread thread = new Thread(jobNode.getJob());
                    thread.start();
                    jobNode.primeNextExecutionTime();
                } else {
                    minDiff = Math.min(jobNode.getExecutionTime().getTimeInMillis() - now, minDiff);
                    if (!jobNodes.hasNext()) {
                        break;
                    }
                    jobNode = (RecurringJobNode) jobNodes.next();
                }
            } while (true);
        }
        return minDiff;
    }
}
