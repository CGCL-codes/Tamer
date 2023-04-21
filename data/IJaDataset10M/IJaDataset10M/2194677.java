package com.cfdrc.sbmlforge.multitask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.cfdrc.sbmlforge.util.DateUtils;
import com.cfdrc.sbmlforge.util.MiscUtils;

/**
 * Models a process with multiple tasks, start/end time, etc.
 * 
 * @author John Siegel
 */
public class Process {

    /** task label */
    private String label;

    /** start time */
    private Date start;

    /** end time */
    private Date end;

    /** tasks */
    private List<Task> tasks;

    /** Exception */
    private Throwable exception;

    /**
	 * Create a process
	 * 
	 * @param label		Name of process 
	 */
    public Process(String label) {
        this.label = label;
        this.start = DateUtils.now();
        this.tasks = new ArrayList<Task>();
    }

    /**
	 * Add task to this process.
	 * 
	 * @param taskLabel	Name of task
	 * @return			Task with given name
	 */
    public Task addTask(String taskLabel) {
        getTasks().add(new Task(getTasks().size() + 1, taskLabel));
        return getLatestTask();
    }

    /**
	 * Get end date of process.
	 * 
	 * @return			End date of process
	 */
    public Date getEnd() {
        return end;
    }

    /**
	 * Set end date of process.
	 *  
	 * @param end		End date of process to be set
	 */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
	 * Retrieve name of process.
	 * 
	 * @return			Name of process
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * Retrieve start date of process.
	 * 
	 * @return			Start date of process
	 */
    public Date getStart() {
        return start;
    }

    /**
	 * Retrieve list of tasks for this process.
	 * 
	 * @return			List of tasks for this process
	 */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
	 * Retrieve time in seconds this process took to complete.
	 * 
	 * @return			Time in seconds process took to complete
	 */
    public int getProcessTimeInSeconds() {
        return DateUtils.differenceInSeconds(end == null ? DateUtils.now() : end, start);
    }

    /**
	 * Retrieve last task in stack.
	 * 
	 * @return			Last task in stack
	 */
    public Task getLatestTask() {
        return MiscUtils.last(tasks);
    }

    /**
	 * Is process complete?
	 * 
	 * @return			True if process is complete
	 */
    public boolean isComplete() {
        return end != null;
    }

    /**
	 * Set process as complete.
	 */
    public void endProcess() {
        end = DateUtils.now();
    }

    /**
	 * Does process have uncompleted task?
	 * 
	 * @return			True if uncompleted task remains
	 */
    public boolean hasOpenTask() {
        return getLatestTask() != null && !getLatestTask().isComplete();
    }

    /**
	 * Retrieve exception thrown in execution of this process.
	 * 
	 * @return			Exception thrown in execution of this process
	 */
    public Throwable getException() {
        return exception;
    }

    /**
	 * Set exception thrown in execution of this process.
	 * 
	 * @param exception	Exception thrown in execution of this process
	 */
    public void setException(Throwable exception) {
        this.exception = exception;
    }
}
