package neissmodel.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import neissmodel.NeissModel;
import neissmodel.NeissModelLogger;
import neissmodel.PCONST;
import neissmodel.xml.NeissJobXML;
import neissmodel.xml.NoJobInXMLException;

/**
 * Simple class that holds information about jobs submitted to servers.
 *
 * @author Nick Malleson
 */
public class NeissJob {

    /** The ID of the user who submitted the job */
    private String userID = "";

    /** The ID of the job, as it will appear in the user database */
    private String jobID = "";

    /** A description of the job */
    private String description = "";

    /** The name of the <code>NeissModel</code> represented by this job */
    private String modelName = "";

    /** The time (in ms) that the job was submitted */
    private long submitTime = -1;

    /** The file where results of the job are being stored (could be on a
    * remote machine */
    private String resultsFile = "";

    private STATUS jobStatus;

    private static Logger LOGGER;

    static {
        LOGGER = NeissModelLogger.getLogger(NeissJob.class.getName());
    }

    /**
    * Used to create a new NeissJob when the user id and job id are known (i.e. a new job
    * has just been submitted, we know the user and the HPC will tell us the job id).
    * @param userID
    * @param jobID
    * @param modelName
    * @param resultsFile The location of the results of the job, will be added to the
    * @param submitTime The (aproximate) time that the job wa submitted.
    * user/jobs file.
    */
    public NeissJob(String userID, String jobID, String modelName, String resultsFile, long submitTime) throws NeissJobSubmittedUserException {
        this.userID = userID;
        this.jobID = jobID;
        this.modelName = modelName;
        this.resultsFile = resultsFile;
        this.submitTime = submitTime;
    }

    /**
    * Create a new <code>NeissJob</code> from the given xml and job id (the xml
    * can potentially hold numerous jobs).
    * @param xml
    * @param jobID
    * @throws NeissJobXMLConversionException If there was a problem creating the
    * job from the xml.
    * @throws NoJobInXMLException if the job was not found in the xml
    */
    public NeissJob(NeissJobXML xml, String jobID) throws NeissJobXMLConversionException, NoJobInXMLException {
        if (xml == null) {
            throw new NeissJobXMLConversionException("Input xml is null");
        } else if (jobID == null || jobID.equals("")) {
            throw new NeissJobXMLConversionException("JobID is null or empty string");
        }
        for (String id : xml.getJobIDs()) {
            if (id.equals(jobID)) {
                this.userID = xml.getUserID(jobID);
                this.jobStatus = STATUS.convertStatusFromString(xml.getStatus(jobID));
                this.jobID = jobID;
                this.modelName = xml.getName(jobID);
                this.description = xml.getDescription(jobID);
                return;
            }
        }
        throw new NeissJobXMLConversionException("Could not find job id '" + jobID + "' in xml");
    }

    /**
    * Add the information about the given job to the given NeissJobXML document
    * @param xmlDoc
    * @param theJob
    * @return True if the job was successful, false otherwise.
    */
    public static boolean addJobDataToXML(NeissJobXML xmlDoc, NeissJob theJob) {
        if (theJob == null || xmlDoc == null) {
            LOGGER.log(Level.SEVERE, "Could not add the job information to the xml " + "document because either the xml or the job is null");
            return false;
        }
        String id = theJob.getJobID();
        try {
            xmlDoc.addJob(id);
            xmlDoc.setName(theJob.getModelName(), id);
            xmlDoc.setUserID(theJob.getUserID(), id);
            xmlDoc.setDescription(theJob.getDescription(), id);
            xmlDoc.setSubmitTime(String.valueOf(theJob.getSubmitTime()), id);
            xmlDoc.setStatus(theJob.getStatus().toString(), id);
        } catch (NoJobInXMLException ex) {
            LOGGER.log(Level.SEVERE, "Could not add job to the given xml (internal error).", ex);
            return false;
        }
        return true;
    }

    /**
    * Get the location of the file (on the remote machine) where results are stored.

    */
    public String getResultsFile() {
        return this.resultsFile;
    }

    /**
    * Represent the status of jobs
    */
    public enum STATUS {

        RUNNING(1), COMPLETED(2), QUEUED(3), FAILED(4), UNKNOWN(5);

        private int statusNo;

        STATUS(int status) {
            this.statusNo = status;
        }

        /** Return the integer equivalent of this status */
        public int getStatusNo() {
            return this.statusNo;
        }

        public static STATUS convertStatusFromString(String s) {
            if (s.equals(STATUS.RUNNING.toString())) {
                return STATUS.RUNNING;
            } else if (s.equals(STATUS.COMPLETED.toString())) {
                return STATUS.COMPLETED;
            } else if (s.equals(STATUS.QUEUED.toString())) {
                return STATUS.QUEUED;
            } else if (s.equals(STATUS.FAILED.toString())) {
                return STATUS.FAILED;
            } else if (s.equals(STATUS.UNKNOWN.toString())) {
                return STATUS.UNKNOWN;
            } else {
                return null;
            }
        }
    }

    public String getDescription() {
        return description;
    }

    public String getJobID() {
        return jobID;
    }

    public String getModelName() {
        return modelName;
    }

    public String getUserID() {
        return userID;
    }

    public STATUS getStatus() {
        return this.jobStatus;
    }

    /** Convenience to return the integer eqivalent of this Job's
    * <code>STATUS</code>status
    */
    public int getStatusInt() {
        return this.jobStatus.statusNo;
    }

    public void setStatus(STATUS s) {
        this.jobStatus = s;
    }

    public long getSubmitTime() {
        return this.submitTime;
    }

    @Override
    public String toString() {
        return "Job (" + "User: " + (this.userID == null ? "null" : this.userID) + " ID: " + (this.jobID == null ? "null" : this.jobID) + " Status: " + (this.jobStatus == null ? "null" : this.jobStatus.toString()) + ")";
    }

    public class NeissJobXMLConversionException extends Exception {

        public NeissJobXMLConversionException(String message) {
            super(message);
        }
    }

    /**
    * Check equality based on the Job ID.
    * @param obj
    * @return Returns true if, and only iff, the given object is a
    * <code>NeissJob</code> with the same job ID as this one.
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NeissJob)) return false;
        NeissJob j = (NeissJob) obj;
        return j.getJobID().equals(this.getJobID());
    }

    /**
    * Return the hashcode, generated from this Job's ID. This is equivalent
    * to <code>job.getJobID().hashCode()</code>
    */
    @Override
    public int hashCode() {
        return this.getJobID().hashCode();
    }
}
