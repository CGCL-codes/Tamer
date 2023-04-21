package net.sf.mybatchfwk;

import java.util.Date;
import java.util.Iterator;
import net.sf.mybatchfwk.history.IExecutionHistory;

/**
 * A basic execution report.<br>
 * It contains informations about the batch execution: begin date, end date, identity of completed and failed tasks.<br>
 * It is used for creating reports (mail, logs, ...) after the batch has been completed or after a fatal error.<br>
 * <br>
 * User can create his own execution report to store extra informations about the completions and the failures.
 * 
 * @author J�r�me Bert�che (cyberteche@users.sourceforge.net)
 */
public class ExecutionReport {

    /**
	 * Begin date of the execution
	 */
    protected Date beginDate;

    /**
	 * End date of the execution
	 */
    protected Date endDate;

    /**
	 * The number of completed tasks
	 */
    protected volatile long numberOfCompletedTasks;

    /**
	 * The number of failed tasks
	 */
    protected volatile long numberOfFailedTasks;

    /**
	 * The execution history
	 */
    protected IExecutionHistory history;

    /**
	 * Default constructor
	 */
    public ExecutionReport() {
    }

    public ExecutionReport(IExecutionHistory history) {
        this.history = history;
    }

    /**
	 * Store informations about a task completion.
	 * @param task the completed task
	 */
    public void reportCompletion(ITask task) {
        numberOfCompletedTasks++;
    }

    /**
	 * Store informations about a task failure.
	 * @param task the failed task
	 * @param throwable the cause of the failure
	 */
    public void reportFailure(ITask task, Throwable throwable) {
        numberOfFailedTasks++;
    }

    /**
     * Return an iterator over the id of the completed tasks, provided by the execution history
     * @return
     * @throws BatchException
     */
    public Iterator completedTasksIdIterator() throws BatchException {
        if (history != null) {
            return history.completedTasksIdIterator();
        }
        return null;
    }

    /**
     * Return an iterator over the id of the failed tasks, provided by the execution history
     * @return
     * @throws BatchException
     */
    public Iterator failedTasksIdIterator() throws BatchException {
        if (history != null) {
            return history.failedTasksIdIterator();
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("[").append("beginDate='").append(beginDate).append("', endDate='").append(endDate).append("', numberOfCompletedTasks='").append(numberOfCompletedTasks).append("', numberOfFailedTasks='").append(numberOfFailedTasks).append("']");
        return buffer.toString();
    }

    /**
	 * Return the begin date of the execution
	 * @return a date
	 */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
	 * Set the begin date of the execution
	 * @param beginDate the begin date to set
	 */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    /**
	 * Return the end date of the execution
	 * @return a date
	 */
    public Date getEndDate() {
        return endDate;
    }

    /**
	 * Set the end date of the execution
	 * @param endDate the end date to set
	 */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
	 * @return Returns the numberOfCompletedTasks.
	 */
    public long getNumberOfCompletedTasks() {
        return numberOfCompletedTasks;
    }

    /**
	 * @return Returns the numberOfFailedTasks.
	 */
    public long getNumberOfFailedTasks() {
        return numberOfFailedTasks;
    }

    /**
	 * @return the history
	 */
    public IExecutionHistory getHistory() {
        return history;
    }

    /**
	 * @param history the history to set
	 */
    public void setHistory(IExecutionHistory history) {
        this.history = history;
    }
}
