package uk.ac.ed.rapid.data;

import uk.ac.ed.rapid.button.ButtonTable;
import uk.ac.ed.rapid.data.browser.FileBrowserTable;
import uk.ac.ed.rapid.data.filesystem.FileSystemTable;
import uk.ac.ed.rapid.exception.RapidException;
import uk.ac.ed.rapid.job.Job;
import uk.ac.ed.rapid.jobdata.JobData;
import uk.ac.ed.rapid.jobdata.JobDataTable;
import uk.ac.ed.rapid.jobdata.JobID;
import uk.ac.ed.rapid.jobdata.StaticTable;
import uk.ac.ed.rapid.jobsubmission.JobQueueTable;
import uk.ac.ed.rapid.persistence.PersistenceEngine;
import uk.ac.ed.rapid.plugin.PluginTable;
import uk.ac.ed.rapid.selection.SelectionTable;
import uk.ac.ed.rapid.symbol.SymbolTable;

/**
 * Container class for all tables and objects.
 *
 * @author Jos Koetsier
 */
public interface RapidData {

    /**
	 * Gets the job data of the job currently being edited
	 *
	 * @return job data
	 */
    public JobData getJobData();

    /**
	 * Convenience method. Determines if a job with this jobID exists
	 * @param jobID Id of the job to retrieve.
	 * @return 
	 */
    public boolean exists(JobID jobID);

    /**
	 * Convenience method.  Gets the job data of the job with a particular job ID
	 * @param jobID Id of the job to retrieve. 
	 * @return job data
	 */
    public JobData getJobData(JobID jobID) throws RapidException;

    /** Submits the job to the relevant queue and initialises a new job
	 *
	 * @return new jobData
	 * @throws RapidException
	 */
    public JobData submit() throws RapidException;

    /**
	 * Deletes a job data 
	 * @param jobID
	 * @throws RapidException
	 */
    public void delete(JobID jobID) throws RapidException;

    /**
	 * Getter method for Element table
	 *
	 * @return the stored ElementTable
	 */
    public SymbolTable getSymbolTable();

    /**
	 * Gets the global variable table
	 * @return the global variable table
	 */
    public SelectionTable getSelectionTable();

    /**
	 * Getter method for Plugin table
	 *
	 * @return the stored PluginTable
	 */
    public PluginTable getPluginTable();

    /**
	 * Getter method for table of jobs
	 *
	 * @return the stored JobTable
	 */
    public JobDataTable getJobDataTable();

    /**
	 * Gets the button table
	 * @return button table
	 */
    public ButtonTable getButtonTable();

    /**
	 * Gets the job template
	 *
	 * @return job
	 */
    public Job getJob();

    /**
	 * Getter method for table of job queues
	 *
	 * @return the stored Job queue Table
	 */
    public JobQueueTable getJobQueueTable();

    /**
	 * Getter method for table of filebrowsers
	 *
	 * @return the stored FileBrowserTable
	 */
    public FileBrowserTable getFileBrowserTable();

    /**
	 * Getter method for table of filesystems
	 *
	 * @return the stored FileSystemTable
	 */
    public FileSystemTable getFileSystemTable();

    /**
	 * Gets the table of static variables
	 * @return table of static variables
	 */
    public StaticTable getStaticTable();

    /**
	 * Sets the table of static variables
	 * @param staticTable table of static variables.
	 */
    public void setStaticTable(StaticTable staticTable);

    /**
	 * Getter method for rapid configuration object
	 *
	 * @return the stored RapidConfiguration
	 */
    public RapidConfiguration getRapidConfiguration();

    /**
	 * Get the username
	 *
	 * @return username
	 */
    public String getUserName();

    /**
	 * Sets the username of the authenticated user. Used for persistence
	 *
	 * @param userName
	 *            username
	 */
    public void setUserName(String userName);

    /**
	 * Gets the current page to load
	 * @return current page
	 */
    public String getPage() throws RapidException;

    /**
	 * Sets the current page
	 * @param page
	 */
    public void setPage(String page) throws RapidException;

    /**
	 * Gets the Job Persistence object
	 * @return job persistence object
	 */
    public PersistenceEngine getPersistenceEngine();

    /**
	 * 
	 * @param jobID
	 * @throws RapidException
	 */
    public void jobIsCompleted(String jobID) throws RapidException;

    /**
	 * Performs initialisation steps
	 * @throws RapidException
	 */
    public void initialise() throws RapidException;
}
