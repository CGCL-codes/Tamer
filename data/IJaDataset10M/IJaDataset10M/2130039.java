package org.gridbus.broker.common;

import org.apache.log4j.Logger;
import org.gridbus.broker.common.security.UserCredential;
import org.gridbus.broker.constants.JobStatus;
import org.gridbus.broker.exceptions.GridBrokerException;
import org.gridbus.broker.exceptions.JobSubmissionFailedException;
import org.gridbus.scs.common.SCSJob;

/**
 * @author krishna
 */
final class Dispatcher implements Runnable {

    /**
     * Logger for this class
     */
    private final Logger logger = Logger.getLogger(Dispatcher.class);

    private Job job;

    private String applicationID;

    private SCSJob scsJob;

    private BrokerStorage store = null;

    /**
	 * @throws GridBrokerException
	 */
    Dispatcher(Job job) throws GridBrokerException {
        super();
        store = BrokerStorage.getInstance();
        this.job = job;
        applicationID = job.getApplication().getId();
    }

    Dispatcher(SCSJob job) throws GridBrokerException {
        super();
        store = BrokerStorage.getInstance();
        this.scsJob = job;
        applicationID = job.getApplication().getId();
    }

    public void run() {
        dispatchJob(scsJob);
    }

    private void setJobCredentials(Job job) {
        UserCredential uc = store.getCredentialsForService(applicationID, job.getServer());
        job.setUserCredential(uc);
    }

    private void dispatchJob(Job job) {
        synchronized (job) {
            try {
                job = store.getJob(applicationID, job.getName());
                job.updateStatus(JobStatus.STAGE_IN);
                store.saveJob(job);
                setJobCredentials(job);
                ComputeServer cs = job.getServer();
                cs.submitJob(job);
                logger.info("Dispatched job " + job.getName() + " to server " + cs.getHostname());
                job.updateStatus(JobStatus.SUBMITTED);
                store.saveJob(job);
                cs.commitJob(job);
                store.saveJob(job);
            } catch (Exception e) {
                if (e instanceof JobSubmissionFailedException) {
                    logger.warn("Error submitting job." + job.getName() + ". Resetting it... : ", e);
                } else {
                    logger.warn("Error dispatching job." + job.getName() + ". Resetting it...", e);
                }
                try {
                    job.reset();
                    store.saveJob(job);
                } catch (GridBrokerException e1) {
                    logger.fatal("FATAL ERROR saving reset-job." + job.getName() + " to database. ", e1);
                }
            }
        }
    }

    private void dispatchJob(SCSJob job) {
        synchronized (job) {
            try {
                job = store.getSCSJob(applicationID, job.getName());
                job.updateStatus(JobStatus.STAGE_IN);
                store.saveJob(job);
                ComputeServer cs = job.getServer();
                job.updateStatus(JobStatus.SUBMITTED);
                cs.submitJob(job);
                System.err.println("SCS JOB Dispatched job " + job.getName() + " to server " + cs.getHostname());
                store.saveJob(job);
            } catch (Exception e) {
                if (e instanceof JobSubmissionFailedException) {
                    System.err.println("Error submitting job." + job.getName() + ". Resetting it... : ");
                    e.printStackTrace();
                } else {
                    System.err.println("Error dispatching job." + job.getName() + ". Resetting it...");
                    e.printStackTrace();
                }
                try {
                    job.reset();
                    store.saveJob(job);
                } catch (GridBrokerException e1) {
                    logger.fatal("FATAL ERROR saving reset-job." + job.getName() + " to database. ", e1);
                }
            }
        }
    }
}
