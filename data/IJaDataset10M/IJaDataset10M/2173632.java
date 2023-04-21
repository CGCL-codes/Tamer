package org.atlantal.api.schedule.app;

import org.atlantal.api.schedule.JobEntry;
import org.atlantal.api.schedule.JobTaskType;
import org.atlantal.api.schedule.ScheduleException;
import org.atlantal.api.app.service.Service;

/***
 * ScheduleService interface.
 *
 * @author <a href="masurel@mably.com">Fran�ois MASUREL</a>
 */
public interface ScheduleService extends Service {

    /***
     * Get a specific Job from Storage.
     *
     * @param oid The int id for the job.
     * @return A JobEntry.
     * @throws ScheduleException TODO
     */
    JobEntry getJob(int oid) throws ScheduleException;

    /***
     * Get a specific Job from Storage by its identifier string.
     *
     * @param oid The String unique identifier for the job.
     * @return A JobEntry.
     * @throws ScheduleException TODO
     */
    JobEntry getJobByIdentifier(String oid) throws ScheduleException;

    /***
     * Get a specific task type from Storage.
     *
     * @param oid The int id for the task type.
     * @return A JobTaskType.
     * @throws ScheduleException TODO
     */
    JobTaskType getTaskType(int oid) throws ScheduleException;

    /***
     * Add a new job to the queue.
     *
     * @param je A JobEntry with the job to add.
     * @throws ScheduleException TODO
     */
    void addJob(JobEntry je) throws ScheduleException;

    /***
     * Remove a job from the queue.
     *
     * @param je A JobEntry with the job to remove.
     * @throws ScheduleException TODO
     */
    void removeJob(JobEntry je) throws ScheduleException;
}
