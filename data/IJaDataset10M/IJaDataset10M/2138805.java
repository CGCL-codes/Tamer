package org.systemsbiology.apps.gui.server.job;

/**
 * Class encapsulating all the information needed to submit a job
 * to a cluster or batch system.
 * 
 * @author vsharma
 * @author Mark Christiansen
 *
 */
public interface IJob {

    /**
	 * Use this method if the path to which the job script is to be written
	 * out is different from the path used when executing the script.  
	 * For example, if the data directory is mounted both on the webserver, 
	 * and the server that will run the jobs, and the mount point is different
	 * on the two machines. 
	 * So, the webserver may write scripts to /Path1/project/myscript.sh
	 * and the script is visible to the executing server as 
	 * /Path2/project/myscript.sh
	 * For the example above, setLocalWorkingDir to /Path1/project.
	 * 
	 * @param dir
	 */
    public abstract void setLocalWorkingDir(String dir);

    public abstract String getLocalWorkingDir();

    public abstract void setListener(IJobListener listener);

    public abstract void setJobResources(JobResources resources);

    public abstract String getJobName();

    public abstract String getWorkingDir();

    public JobResources getJobResources();

    /**
	 * Returns the id assigned to this job by the job scheduler.
	 * @return String
	 */
    public abstract String getSchedulerJobId();

    /**
	 * This is id assigned to this job by the job scheduler.
	 * @param id
	 */
    public abstract void setSchedulerId(String id);

    public abstract String getOutFile();

    public abstract void setOutFile(String outFile);

    /**
	 * @param outFile -- file where the output from this job should go
	 * @param appendOut -- true if output is to be appended
	 */
    public abstract void setOutFile(String outFile, boolean appendOut);

    public abstract boolean hasOutputRedirect();

    public abstract boolean hasJobResources();

    public abstract boolean appendOut();

    /**
	 * Redirect output to the given file (if one is given)
	 */
    public abstract String outputRedirect();

    public abstract void jobDone();
}
