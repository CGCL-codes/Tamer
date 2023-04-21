package org.gridbus.scs.common;

import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.gridbus.broker.common.ApplicationContext;
import org.gridbus.broker.common.ComputeServer;
import org.gridbus.broker.common.Job;
import org.gridbus.broker.common.WorkUnit;
import org.gridbus.broker.constants.JobStatus;
import org.gridbus.broker.constants.JobType;
import org.gridbus.broker.exceptions.GridBrokerException;
import org.gridbus.broker.jobdescription.JobEnvironment;
import org.gridbus.broker.jobdescription.JobRequirement;
import org.gridbus.broker.util.BrokerUtil;

/**
 * 
 * @author Tom Kobialka
 *
 */
public class SCSJob extends WorkUnit {

    private final transient Logger logger = Logger.getLogger(SCSJob.class);

    private JobRequirement requirements = null;

    private JobEnvironment environment = null;

    private String queueName = null;

    private String localWorkingDirectory = null;

    private String result = null;

    private String handle = null;

    /**
	 * Constructor 
	 */
    public SCSJob() {
        variableTable = new Hashtable();
        requirements = new JobRequirement();
        environment = new JobEnvironment();
        status = JobStatus.READY;
        type = JobType.SCS_GETOBSRV;
    }

    SCSJob(String name) {
        this();
        this.name = name;
    }

    /**
     * Constructor - with unique job id and job type passed as parameters.
     * @param name
     * @param type 
     */
    SCSJob(String name, int type) {
        this();
        this.name = name;
        this.type = type;
    }

    /**
	 * Returns the server to which the job is allocated
	 * @return server - the Compute Server if the job is allocated to a server otherwise null 
	 */
    public ComputeServer getServer() {
        return (ComputeServer) service;
    }

    /**
	 * Sets the server to which the job is to be allocated.
	 * @param server
	 */
    public void setServer(ComputeServer server) {
        this.service = server;
    }

    /**
	 * Resets the job status to READY and sets the server to null.
	 * terminate/clean up. 
	 */
    public void reset() {
        System.err.println("SCS Job reset");
        ComputeServer server = getServer();
        if (server != null) {
            logger.debug("Resetting job " + this.name + " (" + this.getStatusString() + ") on server: " + server.getHostname());
        } else {
            logger.debug("Resetting job " + this.name + " (" + this.getStatusString() + ") on server: NULL");
        }
        updateStatus(JobStatus.READY);
        setHandle(null);
        setSubmittedTime(0);
        setCompletedTime(0);
        setQueueName(null);
        setServer(null);
        try {
            setLocalWorkingDirectory(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    /**
	 * @return queueName name
	 */
    public String getQueueName() {
        return this.queueName;
    }

    /**
	 * Get the unique handle for the job (middleware-dependent)
	 * @return job handle
	 */
    public String getHandle() {
        return handle;
    }

    /**
	 * Sets the unique handle for the job which comes from the 
	 * middleware. This handle would the identifier object using which
	 * we can query the job status / access the job details on the remote side. 
	 * @param handle
	 */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
	 * @return Returns the requirements.
	 */
    public JobRequirement getRequirements() {
        return requirements;
    }

    /**
	 * @param requirements The requirements to set.
	 */
    public void setRequirements(JobRequirement requirements) {
        this.requirements = requirements;
    }

    /**
	 * @return Returns the environment.
	 */
    public JobEnvironment getEnvironment() {
        return environment;
    }

    /**
	 * @param environment The environment to set.
	 */
    public void setEnvironment(JobEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Returns the integer value of the job type.
     * @return job status
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the job type.
     * @param type 
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
	 * Sets the job status
	 * @param status
	 */
    public void updateStatus(int status) {
        System.err.println("IN UPDATESTATUS");
        this.status = status;
        if (getServer() != null) {
            System.err.println("Event: status changed for job: " + getName() + "(" + getStatusString() + ") on server: " + getServer().getHostname());
        } else {
            System.err.println("Event: status changed for job: " + getName() + "(" + getStatusString() + ")");
        }
        try {
            ApplicationContext.notifyListeners_Blocking(this);
        } catch (Exception e) {
            logger.warn("ERROR in statusChanged Event-handler", e);
        }
    }

    /**
	 * Returns the integer value of the job status.
	 * @return job status
	 */
    public int getStatus() {
        return status;
    }

    /**
	 * Returns the status in the form of a string. The staus can be any one of
	 * "unsubmitted","submitted","active","pending","done" or "failed"
	 *
	 * @return status string
	 */
    public String getStatusString() {
        return JobStatus.stringValue(status);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLocalWorkingDirectory() {
        if (localWorkingDirectory == null) {
            localWorkingDirectory = application.getLocalOutputDirectory();
            localWorkingDirectory = BrokerUtil.combinePath(localWorkingDirectory, this.name);
        }
        return localWorkingDirectory;
    }

    /**
     * Sets the local working directory (where the broker puts the job output files locally on the 
     * broker machine).
     * 
     * This path is relative to the current directory. (i.e where the broker is running / started)
     * 
     * Can only be set prior to a job being scheduled.
     * 
     * @return
     */
    public void setLocalWorkingDirectory(String localWorkingDirectory) throws GridBrokerException {
        if (status != JobStatus.READY) {
            throw new GridBrokerException("Job's local working directory can only be changed while in (" + JobStatus.stringValue(JobStatus.READY) + ") state. Current Job state is (" + JobStatus.stringValue(status) + ")");
        }
        this.localWorkingDirectory = localWorkingDirectory;
    }
}
