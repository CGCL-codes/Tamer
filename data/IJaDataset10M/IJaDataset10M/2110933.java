package server.jobs;

/**
 * Title:        ClusterServer
 * Description:  A clustered server to replicate state across separate JVMs.
 * Copyright:    Copyright (c) 2001
 * Company:
 *
 * Super-interface of BaseTaskInterface with task-specific exceptions.
 * Please refer to {@link BaseTaskInterface} for method documentation.
 *
 * @author Miron Roth
 * @version 1.0
 */
public interface ServerTaskInterface extends BaseTaskInterface {

    public String scheduleJob(String id, Object jobName, Schedule jobSchedule, JobConfig config) throws JobException;

    public void cancelJob(String id, boolean immediately) throws JobException;
}
