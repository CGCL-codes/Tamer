package org.jcvi.vics.compute.service.common;

import org.apache.log4j.Logger;
import org.jcvi.vics.compute.api.ComputeBeanRemote;
import org.jcvi.vics.compute.api.EJBFactory;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Apr 8, 2010
 * Time: 12:46:08 PM
 */
public class SubmitJobAndWaitHelper {

    public final int WAIT_INTERVAL_IN_SECONDS = 5;

    String processName;

    Long taskId;

    Logger logger;

    public SubmitJobAndWaitHelper(String processName, Long taskId) {
        this.processName = processName;
        this.taskId = taskId;
    }

    public void startAndWaitTillDone() throws Exception {
        if (taskId == null || taskId == 0) {
            throw new Exception("taskId must be non-null and have non-zero value");
        }
        this.logger = ProcessDataHelper.getLoggerForTask(taskId.toString(), this.getClass());
        ComputeBeanRemote computeBean = EJBFactory.getRemoteComputeBean();
        if (computeBean == null) {
            String em = "computeBean is unexpectedly null";
            logger.error(em);
            throw new Exception(em);
        }
        logger.info("computeBean.submitJob() processName=" + processName + " taskId=" + taskId);
        computeBean.submitJob(processName, taskId);
        logger.info("starting waitForTask for taskId=" + taskId);
        waitForTask(taskId);
    }

    protected void waitForTask(Long taskId) throws Exception {
        ComputeBeanRemote computeBean = EJBFactory.getRemoteComputeBean();
        String[] taskStatus = null;
        while (taskStatus == null || !isTaskComplete(taskStatus[0])) {
            taskStatus = computeBean.getTaskStatus(taskId);
            Thread.sleep(WAIT_INTERVAL_IN_SECONDS * 1000);
        }
        if (!taskStatus[0].equals("completed")) {
            throw new Exception("Task " + taskId + " finished with non-complete status=" + taskStatus[0]);
        }
    }

    private boolean isTaskComplete(String status) {
        return status.equals("completed") || status.equals("error");
    }
}
