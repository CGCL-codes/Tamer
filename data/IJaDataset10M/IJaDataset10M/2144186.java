package org.jaffa.modules.scheduler.components.taskfinder;

import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.modules.scheduler.components.taskfinder.dto.TaskFinderOutDto;

/** Interface for TaskFinder components.
 */
public interface ITaskFinder {

    /** This should be invoked, when done with the component.
     */
    public void destroy();

    /** Searches for Task objects.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     * @return The search results.
     */
    public TaskFinderOutDto find() throws FrameworkException, ApplicationExceptions;

    /** Activates the Task in the Scheduler.
     * @param taskId the identifier for a Task.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public void activateTask(String taskId) throws FrameworkException, ApplicationExceptions;

    /** Inactivates the Task in the Scheduler.
     * @param taskId the identifier for a Task.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public void inactivateTask(String taskId) throws FrameworkException, ApplicationExceptions;

    /** Pauses the Scheduler.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public void pauseScheduler() throws FrameworkException, ApplicationExceptions;

    /** Starts the Scheduler.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public void startScheduler() throws FrameworkException, ApplicationExceptions;

    /** Stops the Scheduler.
     * NOTE: The scheduler cannot be re-started.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public void stopScheduler() throws FrameworkException, ApplicationExceptions;

    /** Removes all event logs that resulted from the execution of the scheduler.
     * @throws FrameworkException in case any internal error occurs.
     * @throws ApplicationExceptions Indicates application error(s).
     */
    public void clearEventLogs() throws FrameworkException, ApplicationExceptions;
}
