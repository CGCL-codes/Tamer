package net.sf.access.api.arc;

/**
 * This Class contains all the fixed information related to ARC user interface;
 * all the existing job status, some messages returned by the user interface, ... .
 * Status description referred to ARC User Interface User�s Manual [NORDUGRID-MANUAL-1 1/3/2009]
 * 
 * @author Francesco Lelli
 *
 */
public final class Constant {

    /**
	 * job has reached the site 
	 */
    public static final String JOB_STATUS_ACCEPTING = "ACCEPTING";

    /**
	 * job submitted but not yet processed
	 */
    public static final String JOB_STATUS_ACCEPTED = "ACCEPTED";

    /**
	 * input �les are being retreived 
	 */
    public static final String JOB_STATUS_PREPARING = "PREPARING";

    /**
	 * input �les are retreived 

	 */
    public static final String JOB_STATUS_PREPARED = "PREPARED";

    /**
	 * interaction with LRMS ongoing
	 */
    public static final String JOB_STATUS_SUBMITTING = "SUBMITTING";

    /**
	 * job is queued by LRMS
	 */
    public static final String JOB_STATUS_Q = "INLRMS:Q";

    /**
	 * job is running
	 */
    public static final String JOB_STATUS_R = "INLRMS:R";

    /**
	 * job is suspended 
	 */
    public static final String JOB_STATUS_S = "INLRMS:S";

    /**
	 * job is �nishing in LRMS
	 */
    public static final String JOB_STATUS_E = "INLRMS:E";

    /**
	 * job is in any other LRMS state
	 */
    public static final String JOB_STATUS_O = "INLRMS:O";

    /**
	 * job is being cancelled by user request
	 */
    public static final String JOB_STATUS_KILLING = "KILLING";

    /**
	 * job is completed in LRMS
	 */
    public static final String JOB_STATUS_EXECUTED = "EXECUTED";

    /**
	 * output �les are being transferred
	 */
    public static final String JOB_STATUS_FINISHING = "FINISHING";

    /**
	 * job is �nished
	 */
    public static final String JOB_STATUS_FINISHED = "FINISHED";

    /**
	 * job is �nished with an error 
	 */
    public static final String JOB_STATUS_FAILED = "FAILED";

    /**
	 * job is cancelled by user request
	 */
    public static final String JOB_STATUS_KILLED = "KILLED";

    /**
	 * job is removed due to expiration time
	 */
    public static final String JOB_STATUS_DELETED = "DELETED";

    public static final String LIST_OF_VALID_JOB_STATUS = "ACCEPTING,ACCEPTED,PREPARING,PREPARED,SUBMITTING,INLRMS:Q,INLRMS:R,INLRMS:S,INLRMS:E,INLRMS:O,KILLING,EXECUTED,FINISHING,FINISHED,FAILED,KILLED,DELETED";

    public static final String REGEXP_JOBID = "([^\\\"]+)jobid:([^\\\"]+)";

    public static final String REGEXP_JOB_INFO = "Job ([^\\\\\\\"]+)Job Name: ([^\\\\\\\"]+)Status: ([^\\\\\\\"]+)";

    public static final String REGEXP_EXIT_CODE = "([^\\\\\\\"]+)Exit Code: ([^\\\\\\\"]+)";

    public static final String REGEXP_STATUS = "Status: ([^\\\\\\\"]+)";

    public static final String REGEXP_EXIT = "Exit Code: ([^\\\\\\\"]+)";

    public static final String REGEXP_ERROR = "Error: ([^\\\\\\\"]+)";

    public static final String REGEXP_FOLDER_ID = "([^\\\\\\\"]+)://([^\\\\\\\"]+)/([^\\\\\\\"]+)";

    public static final String REGEXP_PROCESSED = "([^\\\"]+)Jobs processed: ([^\\\\\\\"]+)";

    public static final String REGEXP_PROCESSED2 = "Jobs processed: ([^\\\\\\\"]+)";

    public static final String REGEXP_KILLED = "killed:([^\\\"]+)";

    public static final String MESSAGE_TARGET_REJECT_JOB = "Job submission failed due to: All targets rejected job requests";

    public static final String MESSAGE_JOB_SUBMISSION_FAILED = "Job submission failed";

    public static final String MESSAGE_JOB_NOT_FOUND = "Job information not found";

    public static final String MESSAGE_MALFORMED_URL = "Malformed URL:";

    public static final String MESSAGE_XRSL_NOT_PARSED = "Xrsl string could not be parsed";

    public static final String MESSAGE_PROXY_EXPIRED = "The proxy has expired";

    public static final String MESSAGE_PROXY_NOT_FOUND = "Could not determine location of a proxy certificate:";

    public static final String MESSAGE_INVALID_JN_JID = "No valid jobnames/jobids given";

    public static final String MESSAGE_NO_TARGETS = "No targets available for job-submission";

    public static final String MESSAGE_CALLBACK = "Stale FTPControl callback called";

    public static final String MESSAGE_TAGETS_REJECT = "Job submission failed due to: All targets rejected job requests";

    public static final String MESSAGE_JOB_SUBMITTED = "Job submitted with jobid:";

    public static final String MESSAGE_NO_VALID_JOBID_NAME = "No valid jobnames/jobids given";

    public static final String MESSAGE_SUCCESSFULY_DOWNLOADED = "successfuly downloaded:";

    public static final CharSequence MESSAGE_WRONG_PASSWORD = "wrong pass phrase";

    public static final CharSequence MESSAGE_AUTH_FAILED = "Authentication failed";

    public static final String TOKEN_NEW_LINE = "\n";

    public static final String TOKEN_COMA = ",";

    public static final String FILE_EXPECT_PROXY = "expect_proxy.exp";

    public static final String FILE_EXPECT_SLCS = "expect_slcs.exp";

    public static final String FILE_ENV_SH = "env.sh";

    public static final String FILE_RESOURCE_PROPERTIES = "resource.properties";

    public static final String FILE_COMMAND_SH = "command.sh";

    public static final String DIRECTORY_COMMON = "common";

    public static final int TIMES_NO_JOB_DESCRIPTION = 40;

    public static final long WAITING_TIME = 1000;
}
