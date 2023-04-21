package edu.mit.wi.omnigene.analysis.datasource;

import edu.mit.wi.omnigene.util.*;
import java.util.*;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * AnalysisJobDataSourceBean to access Omnigene Anaysis tables
 *
 * @author  rajesh kuttan, Hui Gong
 * @version $Revision 1.6 $
 */
public interface AnalysisJobDataSource extends EJBObject {

    /**
     * Used to get list of submitted job info
     * @param classname
     * @throws OmnigeneException
     * @throws RemoteException
     * @return JobInfo Vector
     */
    public Vector getWaitingJob(String classname) throws OmnigeneException, RemoteException;

    /**
     * Submit a new job
     * @param taskID
     * @param parameter_info
     * @param inputfile
     * @throws OmnigeneException
     * @throws RemoteException
     * @return Job ID
     */
    public JobInfo addNewJob(int taskID, String parameter_info, String inputfile) throws OmnigeneException, RemoteException;

    /**
     * To update status of job with completed status
     *
     * @param jobNo
     * @param outputFilename
     * @throws OmnigeneException
     * @throws RemoteException
     * @return no. of records updated
     */
    public int updateJobWithCompletedStatus(int jobNo, String outputFilename) throws OmnigeneException, RemoteException;

    /**
     * To update status of job with Error status
     * @param jobNo
     * @param outputFilename
     * @throws OmnigeneException
     * @throws RemoteException
     * @return No of records updated
     */
    public int updateJobWithErrorStatus(int jobNo, String outputFilename) throws OmnigeneException, RemoteException;

    /**
     * Update job information like status and resultfilename
     * @param jobNo
     * @param jobStatusID
     * @param outputFilename
     * @throws OmnigeneException
     * @throws RemoteException
     * @return record count of updated records
     */
    public int updateJob(int jobNo, int jobStatusID, String outputFilename) throws OmnigeneException, RemoteException;

    /**
     * Fetches JobInformation
     * @param jobNo
     * @throws OmnigeneException
     * @throws RemoteException
     * @return <CODE>JobInfo</CODE>
     */
    public JobInfo getJobInfo(int jobNo) throws OmnigeneException, RemoteException;

    /**
     * Used to vet all avilable tasks
     * @throws OmnigeneException
     * @throws RemoteException
     * @return Vector of <CODE>TaskInfo</CODE>
     */
    public Vector getTasks() throws OmnigeneException, RemoteException;

    /**
     * Gets task from a task id
     * @throws OmnigeneException
     * @throws RemoteException
     * @return <CODE>TaskInfo</CODE>
     */
    public TaskInfo getTask(int taskID) throws OmnigeneException, RemoteException;

    /**
     * To register new task
     * @param taskName
     * @param description
     * @param parameter_info
     * @param className
     * @throws OmnigeneException
     * @throws RemoteException
     * @return task ID
     */
    public int addNewTask(String taskName, String description, String parameter_info, String className) throws OmnigeneException, RemoteException;

    /**
     * To remove registered task
     * @param taskID
     * @throws OmnigeneException
     * @throws RemoteException
     * @return No. of updated records
     */
    public int deleteTask(int taskID) throws OmnigeneException, RemoteException;

    /**
     * To update parameter info of a task
     * @param taskID
     * @param parameter_info
     * @throws OmnigeneException
     * @throws RemoteException
     * @return No. of updated records
     */
    public int updateTask(int taskID, String parameter_info) throws OmnigeneException, RemoteException;
}
