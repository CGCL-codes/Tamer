package org.systemsbiology.apps.gui.server.job;

import java.util.List;

/**
 * Class encapsulating all the information needed to submit a job
 * to a cluster or batch system.
 * 
 * @author vsharma
 *
 */
public class CommandJob implements IJob {

    protected IJobListener listener;

    private String jobName;

    private String workingDir;

    private String localWorkingDir;

    private String executable;

    private String outFile = null;

    private boolean appendOut = false;

    private JobResources resources = null;

    private List<String> arguments;

    private String schedulerId;

    public CommandJob(String workingDir, String executable, List<String> args, String jobName) {
        this.workingDir = workingDir;
        this.localWorkingDir = workingDir;
        this.executable = executable;
        this.jobName = jobName;
        this.arguments = args;
    }

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
    public void setLocalWorkingDir(String dir) {
        if (dir != null) localWorkingDir = dir;
    }

    public String getLocalWorkingDir() {
        return localWorkingDir;
    }

    public void setListener(IJobListener listener) {
        this.listener = listener;
    }

    public void setJobResources(JobResources resources) {
        this.resources = resources;
    }

    public String getJobName() {
        return jobName;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public String getExecutable() {
        return executable;
    }

    public JobResources getJobResources() {
        return resources;
    }

    /**
	 * Returns the id assigned to this job by the job scheduler.
	 * @return String
	 */
    public String getSchedulerJobId() {
        return schedulerId;
    }

    /**
	 * This is id assigned to this job by the job scheduler.
	 * @param id
	 */
    public void setSchedulerId(String id) {
        this.schedulerId = id;
    }

    public List<String> getArguments() {
        return this.arguments;
    }

    public boolean hasArgs() {
        return arguments.size() > 0;
    }

    public String getOutFile() {
        return this.outFile;
    }

    public void setOutFile(String outFile) {
        setOutFile(outFile, false);
    }

    /**
	 * @param outFile -- file where the output from this job should go
	 * @param appendOut -- true if output is to be appended
	 */
    public void setOutFile(String outFile, boolean appendOut) {
        this.outFile = outFile;
        this.appendOut = appendOut;
    }

    public boolean hasOutputRedirect() {
        return outFile != null;
    }

    public boolean hasJobResources() {
        return resources != null;
    }

    public boolean appendOut() {
        return appendOut;
    }

    /**
	 * Returns the command as it should be typed on the command-line for running this job
	 * @return String
	 */
    public String executableCommand() {
        StringBuilder buf = new StringBuilder();
        String sep = " ";
        buf.append(getExecutable());
        String args = argsString();
        if (args.length() > 0) {
            buf.append(sep);
            buf.append(args);
        }
        String outputRedir = outputRedirect();
        if (outputRedir.length() > 0) {
            buf.append(sep);
            buf.append(outputRedirect());
        }
        return buf.toString();
    }

    public String argsString() {
        List<String> args = getArguments();
        if (args == null || args.size() == 0) return "";
        StringBuilder buf = new StringBuilder();
        String sep = " ";
        for (String arg : args) {
            buf.append(sep);
            buf.append(arg);
        }
        buf.deleteCharAt(0);
        return buf.toString();
    }

    public String outputRedirect() {
        StringBuilder buf = new StringBuilder();
        String sep = " ";
        if (getOutFile() != null) {
            buf.append(">");
            if (appendOut()) buf.append(">");
            buf.append(sep);
            buf.append(getOutFile());
        }
        return buf.toString();
    }

    public void jobDone() {
        if (listener != null) listener.jobDone(this);
    }
}
