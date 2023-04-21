package org.tigr.htc.request.sge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.ggf.drmaa.DrmaaException;
import org.ggf.drmaa.JobTemplate;
import org.ggf.drmaa.Session;
import org.tigr.antware.shared.exceptions.MissingArgumentException;
import org.tigr.antware.shared.util.StringUtils;
import org.tigr.htc.common.HTCConfig;

/**
 * <b>SgeJob</b> models a Sun Grid Engine job and provides the ability to submit
 * that job to the grid engine using DRMAA (an API provided by many grid schedulers
 * including Condor, Torque, etc...).
 * 
 * @see "http://gridengine.sunsource.net"
 * @see "http://www.drmaa.org"
 *
 */
public class SgeJob {

    static Logger logger = Logger.getLogger(SgeJob.class);

    private String name = null;

    private String remoteCommand = null;

    private String project;

    private Map<String, String> environment;

    private String workingDir;

    private String input, output, error;

    private String queue;

    private int priority;

    private int times = 1;

    private long runningTimeMinutes = 0;

    private int memory = 0;

    private boolean isArrayJob = false;

    private boolean notify;

    private boolean rerunnable = true;

    private String additionalNativeSpec = null;

    private List<String> arguments = null;

    private List<String> hosts = null;

    private List<String> operatingSystems = null;

    private List<String> emails = null;

    /**
	 * Configure the job to be notified of events from the grid engine.
	 * Jobs are notified with the use of signals USR1 and USR2.
	 * 
	 * @param notify a <code>boolean</code>
	 */
    public void setNotifying(boolean notify) {
        this.notify = notify;
    }

    /**
	 * Returns whether the job will be notified of events from the grid engine or not.
	 * Jobs are notified with the use of signals USR1 and USR2.
	 * 
	 * @return a <code>boolean</code>
	 */
    public boolean isNotifying() {
        return notify;
    }

    /**
	 * Set the job's priority level.
	 * 
	 * @param priority an <code>int</code> with the priority level
	 */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
	 * Return the job's configured priority level.
	 * 
	 * @return an <code>int</code> an int with the priority level
	 */
    public int getPriority() {
        return priority;
    }

    /**
	 * Return whether the job should be re-runnable on the grid or not.
	 * 
	 * @return a <code>boolean</code>
	 * @see "SGE 'qsub' documentation for the -r option"
	 */
    public boolean isRerunnable() {
        return rerunnable;
    }

    /**
	 * Configure whether the job is to be re-runnable or not. That is,
	 * whether the job should restart if it was aborted without leaving a consistent state.
	 * 
	 * @param rerunnable a <code>boolean</code>
	 * @see "SGE 'qsub' documentation for the -r option"
	 */
    public void setRerunnable(boolean rerunnable) {
        this.rerunnable = rerunnable;
    }

    /**
	 * Return's the job's configured name. This name is what appears when grid activity is
	 * listed with the 'qstat' command.
	 * 
	 * @return a <code>String</code>
	 * @see "Sun Grid Engine 'qstat' documentation/man page"
	 */
    public String getName() {
        return name;
    }

    /**
	 * Set the job's name. This name is what will appear for the job when grid activity is
	 * listed with the 'qstat' command.
	 * 
	 * @param name a <code>String</code>
	 * @see "Sun Grid Engine 'qstat' documentation/man page"
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Retrieve the command to execute on the grid.
	 * 
	 * @return a <code>String</code> with the executable configured to run.
	 */
    public String getRemoteCommand() {
        return remoteCommand;
    }

    /**
	 * Set the command to execute on the grid.
	 * 
	 * @param remoteCommand
	 */
    public void setRemoteCommand(String remoteCommand) {
        this.remoteCommand = remoteCommand;
    }

    /**
	 * Retrieve the configured list of command line arguments that will be passed to the
	 * executable to run on the grid.
	 * 
	 * @return a {@link List} of <code>String</code> arguments for the remote executable to run.
	 */
    public List<String> getArguments() {
        return arguments;
    }

    /**
	 * Set the command line arguments that will be passed to the executable to run on the grid.
	 * 
	 * @param arguments a {@link List} of <code>String</code> arguments
	 */
    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
	 * Set the list of command line arguments that will be passed to the
	 * executable to run on the grid.
	 * 
	 * @param arguments an array of <code>String</code> arguments
	 */
    public void setArguments(String[] arguments) {
        this.arguments = Arrays.asList(arguments);
    }

    /**
	 * Retrieve the job's configured project.
	 * 
	 * @return a <code>String</code> containing the job's configured project
	 */
    public String getProject() {
        return project;
    }

    /**
	 * Set the job's project. SGE can be configured to require a project when job's are submitted.
	 * This is useful for tracking and accounting purposes.
	 * 
	 * @param project
	 * @see HTCConfig#ENFORCE_PROJECTS_CONFIG
	 */
    public void setProject(String project) {
        this.project = project;
    }

    /**
	 * Return the job's configured environment with the variable names
	 * as keys and the values, as, values.
	 *  
	 * @return a {@link Map}
	 */
    public Map<String, String> getEnvironment() {
        return environment;
    }

    /**
	 * Set the job's environment with a {@link Map} of environment variables. The keys
	 * are to be variable names and the values, their respective values.
	 * 
	 * @param environment
	 *            a {@link Map}
	 */
    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    /**
	 * An accessor for the grid job's working directory
	 * 
	 * @return a <code>String</code> containing the configured working directory
	 */
    public String getWorkingDir() {
        return workingDir;
    }

    /**
	 * A mutator for the grid job's working directory
	 * 
	 * @param workingDir a <code>String</code> containing the configured working directory
	 */
    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    /**
	 * An accessor for the grid job's output file (the grid job's STDOUT)
	 * 
	 * @return a <code>String</code> containing the configured output file
	 */
    public String getOutput() {
        return output;
    }

    /**
	 * A mutator for the output file (the grid job's STDOUT)
	 * 
	 * @param output a <code>String</code>
	 */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
	 * An accessor for the grid job's input file (STDIN)
	 * 
	 * @return a <code>String</code> containing the configured input file
	 */
    public String getInput() {
        return input;
    }

    /**
	 * Sets the input path for the job.
	 * 
	 * @param input a <code>String</code> containing the configured input file
	 * @see JobTemplate#setInputPath(String)
	 */
    public void setInput(String input) {
        this.input = input;
    }

    /**
	 * An accessor for the grid job's error file (STDERR)
	 * 
	 * @return a <code>String</code> containing the configured error file
	 */
    public String getError() {
        return error;
    }

    /**
	 * Sets the error path for the job.
	 * 
	 * @param error a <code>String</code> containing the configured error file
	 * @see JobTemplate#setErrorPath(String)
	 */
    public void setError(String error) {
        this.error = error;
    }

    /**
	 * Returns the queue that the job must run on.
	 * 
	 * @return a <code>String</code> with the configured queue name
	 */
    public String getQueue() {
        return queue;
    }

    /**
	 * Set the queue on which the job(s) should run.
	 * 
	 * @param queue a <code>String</code> with the queue name required
	 * @see "Sun Grid Engine qconf documentation/man page"
	 */
    public void setQueue(String queue) {
        this.queue = queue;
    }

    /**
	 * Submits the job to Sun Grid Engine (SGE) via DRMAA.
	 * 
	 * @return a {@link List} of SGE job IDs.
	 * 
	 * @throws DrmaaException
	 * @throws MissingArgumentException
	 */
    @SuppressWarnings("unchecked")
    public List<String> submit() throws DrmaaException, MissingArgumentException {
        logger.debug("In submit.");
        List<String> jobIds = new ArrayList<String>();
        SessionManager sm = SessionManager.getInstance();
        Session session = sm.getSession();
        try {
            logger.debug("Creating JobTemplate.");
            JobTemplate job = session.createJobTemplate();
            if (remoteCommand == null) {
                throw new MissingArgumentException("Remote executable not specified.");
            }
            job.setRemoteCommand(remoteCommand);
            if (arguments != null && !arguments.isEmpty()) {
                logger.debug("Configuring job arguments. Total number of arguments: " + arguments.size());
                job.setArgs(arguments);
            }
            if (name != null) {
                job.setJobName(name);
            }
            if (emails != null && !emails.isEmpty()) {
                Set<String> emailSet = new HashSet<String>();
                emailSet.addAll(emails);
                job.setEmail(emailSet);
            }
            if (workingDir != null) {
                logger.debug("Configuring job working directory.");
                job.setWorkingDirectory(":" + workingDir);
            }
            if (isArrayJob) {
                fixPathsWithParametricTokens();
            }
            if (output != null) {
                logger.debug("Configuring job output path.");
                job.setOutputPath(":" + output);
            }
            if (error != null) {
                logger.debug("Configuring job error path.");
                job.setErrorPath(":" + error);
            }
            if (input != null) {
                logger.debug("Configuring job input.");
                job.setInputPath(":" + input);
            }
            if (environment != null && !environment.isEmpty()) {
                logger.debug("Setting job environment.");
                Properties envProp = new Properties();
                envProp.putAll(environment);
                job.setJobEnvironment(envProp);
            }
            if (runningTimeMinutes != 0) {
                long seconds = runningTimeMinutes * 60;
                logger.debug("Setting the hard maximum running time: " + seconds + " seconds.");
                job.setHardRunDurationLimit(seconds);
            }
            String nativeSpecification = getNativeSpecifications();
            if (nativeSpecification != null && nativeSpecification.length() > 0) {
                logger.debug("Setting native SGE job specifications.");
                job.setNativeSpecification(nativeSpecification);
            } else {
                logger.debug("No native specifications to set.");
            }
            logger.info("Submitting command to SGE via DRMAA.");
            if (isArrayJob()) {
                logger.info("Submitting an array job of size: " + times);
                jobIds = (List<String>) session.runBulkJobs(job, 1, times, 1);
                logger.debug("Array job master ID is " + jobIds.get(0).split("\\.")[0]);
            } else {
                logger.info("Submitting a singleton job (non-array).");
                String jobId = session.runJob(job);
                jobIds.add(jobId);
            }
            session.deleteJobTemplate(job);
            if (jobIds.size() == 1) {
                logger.debug("Grid job ID: " + jobIds.get(0));
            } else {
                logger.debug("Generated " + jobIds.size() + " grid jobs (an array job).");
            }
        } finally {
            sm.releaseSession(session);
        }
        return jobIds;
    }

    private void fixPathsWithParametricTokens() {
        logger.debug("In fixPathsWithParametricTokens.");
        String token = HTCConfig.PARAMETRIC_TOKEN;
        if (output != null) {
            output = output.replace(token, JobTemplate.PARAMETRIC_INDEX);
        }
        if (error != null) {
            error = error.replace(token, JobTemplate.PARAMETRIC_INDEX);
        }
        if (input != null) {
            input = input.replace(token, JobTemplate.PARAMETRIC_INDEX);
        }
    }

    /**
	 * A method designed to handle the portions of the grid job and submission that are not
	 * covered by the DRMAA API. This includes issues such as memory requirements, which
	 * queue to run on, whether the job is re-runnable or not, what hosts and operating systems
	 * to run on, etc... All these are handled through DRMAAA's support for native grid
	 * specifications.
	 * 
	 * @return a <code>String</code>
	 * @see "DRMAA setNativeSpecification(String)"
	 */
    private String getNativeSpecifications() {
        logger.debug("In getNativeSpecifications.");
        StringBuffer nativeSpecification = new StringBuffer();
        if (additionalNativeSpec != null && additionalNativeSpec.length() > 0) {
            nativeSpecification.append(additionalNativeSpec);
        }
        if (project != null) {
            logger.debug("Setting SGE job project to: " + project);
            nativeSpecification.append(" -P " + project);
        }
        if (queue != null) {
            logger.debug("Formulating queue specification.");
            nativeSpecification.append(" -q ").append(queue);
        }
        if (rerunnable) {
            logger.debug("Formulating re-runnable native specification.");
            nativeSpecification.append(" -r y");
        }
        if (notify) {
            logger.debug("Configuring job to receive USR1 and USR2 signals from the grid.");
            nativeSpecification.append(" -notify");
        }
        if (hosts != null && !hosts.isEmpty()) {
            String csvHosts = StringUtils.joinList(hosts, ",");
            logger.debug("Configuring which hosts to run the job on.");
            nativeSpecification.append(" -l hostname=").append(csvHosts);
        }
        if (operatingSystems != null && !operatingSystems.isEmpty()) {
            ArrayList<String> sgeOsSpecs = new ArrayList<String>();
            if (operatingSystems.contains("linux")) {
                sgeOsSpecs.add("lx*");
            }
            if (operatingSystems.contains("solaris")) {
                sgeOsSpecs.add("sol*");
            }
            if (operatingSystems.contains("osx")) {
                sgeOsSpecs.add("darwin*");
            }
            if (operatingSystems.contains("linux64")) {
                sgeOsSpecs.add("lx*64");
            }
            if (operatingSystems.contains("linux32")) {
                sgeOsSpecs.add("lx*x86");
            }
            if (operatingSystems.contains("solaris64")) {
                sgeOsSpecs.add("sol*64");
            }
            if (operatingSystems.contains("solaris32")) {
                sgeOsSpecs.add("sol*x86");
            }
            String os = StringUtils.joinList(sgeOsSpecs, "|");
            logger.debug("Configuring which operating systems/architectures to run the job on.");
            nativeSpecification.append(" -l arch=").append(os);
        }
        if (memory > 0) {
            nativeSpecification.append(" -l mem_total=").append(memory).append("M");
        }
        if (error != null && output != null && output.equals(error)) {
            logger.debug("Since the the job output and input are identical, they will be merged.");
            nativeSpecification.append(" -j y");
        }
        String finalSpec = nativeSpecification.toString().trim();
        logger.debug("Native specification: \"" + finalSpec + "\"");
        return finalSpec;
    }

    /**
	 * Returns whether this job represents an array job or not. An array job is
	 * an array of identical tasks being differentiated only by an index number
	 * and being treated by Grid Engine almost like a series of jobs. This is
	 * useful to execute large amounts of work in parameter sweep fashion.
	 * 
	 * @return a <code>boolean</code>
	 * @see "qsub documentation / manpage"
	 * @see #getTimes()
	 * @see #setTimes(int)
	 * @see org.ggf.drmaa.Session#runBulkJobs(JobTemplate, int, int, int)
	 */
    public boolean isArrayJob() {
        return isArrayJob;
    }

    /**
	 * Set the number of times the job will execute on the grid. If the number of times
	 * returned is greater than 1, then job represents an "array" job, in which 1 submission
	 * can generate many element jobs of an array in parameter sweep fashion.
	 * 
	 * @return an <code>int</code>
	 * @see #getTimes()
	 * @see org.ggf.drmaa.Session#runBulkJobs(JobTemplate, int, int, int)
	 */
    public int getTimes() {
        return times;
    }

    /**
	 * Set the number of times the job will execute on the grid. If the number of times
	 * set is greater than 1, then job will be an "array" job.
	 * 
	 * @param times an <code>int</code>
	 * @see org.ggf.drmaa.Session#runBulkJobs(JobTemplate, int, int, int)
	 */
    public void setTimes(int times) {
        logger.debug("In setTimes: " + times);
        if (times <= 0) {
            throw new IllegalArgumentException("The number of times a grid job is to execute must be a positive integer greater than 0.");
        }
        if (times == 1) {
            this.times = times;
            isArrayJob = false;
        } else {
            logger.debug("Job is now an array job.");
            this.times = times;
            isArrayJob = true;
        }
    }

    /**
	 * Set an opaque <code>String</code> used as an additional native grid job
	 * specification. This specification is eventually passed to SGE's
	 * submission process as it if were specified on the <code>qsub</code> command line.
	 * 
	 * @param passThrough an opaque <code>String</code> to be passed to the execution environment scheduler
	 * @see "SGE qsub documentation / man page"
	 * @see org.ggf.drmaa.JobTemplate#setNativeSpecification(String)
	 * @see "DRMAA documentation for native specifications"
	 */
    public void setPassthrough(String passThrough) {
        logger.debug("In setPassthrough.");
        this.additionalNativeSpec = passThrough;
    }

    /**
	 * Get the <code>String</code> used as an additional native grid job
	 * specification.
	 * 
	 * @return a <code>String</code> with the configured specification
	 */
    public String getPassthrough() {
        return additionalNativeSpec;
    }

    /**
	 * Set which hosts are acceptable to run the job on.
	 * 
	 * @param hosts a {@link List} of Strings
	 */
    public void setHosts(List<String> hosts) {
        logger.debug("In setHosts.");
        this.hosts = hosts;
    }

    /**
	 * Set which hosts are acceptable to run the job on.
	 * 
	 * @param hosts an array of Strings.
	 */
    public void setHosts(String[] hosts) {
        logger.debug("In setHosts.");
        this.hosts = Arrays.asList(hosts);
    }

    /**
	 * Get the maximum runtime the job may use (in minutes).
	 * 
	 * @return a <code>long</code> with the runtime limit in minutes.
	 * @see org.ggf.drmaa.JobTemplate#getHardRunDurationLimit()
	 */
    public long getMaxRuntime() {
        return runningTimeMinutes;
    }

    /**
	 * Set the maximum runtime for a job in minutes.
	 * 
	 * @param runningTimeMinutes the max run time in minutes, a <code>long</code>.
	 * @see org.ggf.drmaa.JobTemplate#setHardRunDurationLimit(long)
	 */
    public void setMaxRuntime(long runningTimeMinutes) {
        if (runningTimeMinutes <= 0) {
            throw new IllegalArgumentException("Maximum running time must be positive.");
        }
        this.runningTimeMinutes = runningTimeMinutes;
    }

    /**
	 * Set the memory requirement for the job, in megabytes. Therefore for
	 * a job requiring 2 gigabytes of RAM, the caller would use
	 * <p>
	 * <code>job.setMemory(2000);</code>
	 * 
	 * @param megabytes  an <code>int</code> of how many megabytes are required.
	 */
    public void setMemory(int megabytes) {
        logger.debug("In setMemory: Megabytes: " + megabytes);
        this.memory = megabytes;
    }

    /**
	 * Return the memory requirement that has been configured for this job
	 * (in megabytes).
	 * 
	 * @return a <code>int</code> containing the memory requirement in megabytes
	 */
    public int getMemory() {
        return memory;
    }

    /**
	 * Set the operating systems that the job may run on.
	 * Acceptable operating systems are: linux, solaris,
	 * linux32, solaris32, linux64, solaris64 and osx.
	 * 
	 * @param operatingSystems a {@link List} of Strings.
	 */
    public void setOperatingSystems(List<String> operatingSystems) {
        logger.debug("In setOperatingSystems(List<String>).");
        this.operatingSystems = operatingSystems;
    }

    /**
	 * Set the operating systems that the job may run on.
	 * Acceptable operating systems are: linux, solaris,
	 * linux32, solaris32, linux64, solaris64 and osx.
	 * 
	 * @param operatingSystems an array of Strings.
	 */
    public void setOperatingSystems(String[] operatingSystems) {
        logger.debug("In setOperatingSystems(String[]).");
        this.operatingSystems = Arrays.asList(operatingSystems);
    }

    /**
	 * Return the list of operating systems that have been configured.
	 * 
	 * @return a {@link List} of Strings.
	 */
    public List<String> getOperatingSystems() {
        return operatingSystems;
    }

    /**
	 * Add an email address to be notified when the grid completes the job.
	 * Multiple email addresses can be configured with successive calls.
	 * No validation is done to ensure the email address is valid.
	 * 
	 * @param email a <code>String</code>
	 */
    public void addEmail(String email) {
        logger.debug("In addEmail: " + email);
        if (emails == null) {
            emails = new ArrayList<String>();
        }
        emails.add(email);
    }

    /**
	 * Get the registered list of email addresses to be notified when
	 * the job completes
	 * 
	 * @return a {@link List} of email addresses
	 */
    public List<String> getEmails() {
        return emails;
    }
}
