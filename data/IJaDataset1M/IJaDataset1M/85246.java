package server.jobs;

import java.lang.reflect.Method;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import server.Logger;
import server.cluster.ClusterService;

/**
 * Title:        ClusterServer
 * Description:  A clustered server to replicate state across separate JVMs.
 * Copyright:    Copyright (c) 2001
 * Company:
 * <br><br>
 * A special job scheduler responsible for running master jobs when this
 * server accquires cluster master status. <br> The user can define any number
 * of master jobs in the 'job.properties' file but it is recommented to keep it
 * to a minimum. <br>
 * Master jobs should be provided with a properties file with the same name as
 * the class (MasterJob.class -> MasterJob.properties) to define configuration
 * for the Timer to run them.
 *
 * @author Miron Roth
 * @version 1.0
 */
public class MasterJobManager implements Runnable, JobConstants {

    /** Reference to the local cluster service */
    private ClusterService clusterServer;

    /** Reference to master jobs and schedulers */
    private Map masterJobs;

    /**
	 *  Constructor with intitialization
	 *
	 * @param cluster The local cluster service
	 */
    public MasterJobManager(String jobList, ClusterService cluster) {
        this.clusterServer = cluster;
        init(jobList);
    }

    /**
	 *  Initializes the list of master jobs by loading the appropriate classes
	 *  and property files
	 *
	 * @param jobJist A space separated list of class names to be loaded as
	 *                master jobs
	 */
    private void init(String jobList) {
        try {
            StringTokenizer jobs = new StringTokenizer(jobList);
            masterJobs = new HashMap();
            while (jobs.hasMoreTokens()) {
                Job j = JobClassLoader.getInstance().load(jobs.nextToken(), true);
                j.setStartDate(null);
                masterJobs.put(new JobScheduler(), j);
            }
        } catch (JobException e) {
            Logger.log(Logger.ERROR, "Jobs: Master job not loaded", this);
        }
    }

    /**
	 *  Method to be executed when thread starts
	 */
    public void run() {
        startMasterJobs();
    }

    /**
	 *  Starts the master job if the server is cluster master. Otherwise the
	 *  thread waits until it becomes cluster master.
	 */
    private void startMasterJobs() {
        while (!clusterServer.isMaster()) clusterServer.waitUntilMaster();
        Iterator i = masterJobs.keySet().iterator();
        while (i.hasNext()) {
            JobScheduler s = (JobScheduler) (i.next());
            Job job = (Job) (masterJobs.get(s));
            Logger.log(Logger.INFO, "Jobs: Starting master job [" + job.getClass().getName() + "]", this);
            s.schedule(job);
        }
    }
}
