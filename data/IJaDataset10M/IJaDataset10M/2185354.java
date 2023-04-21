package com.controltier.ctl.tasks.controller;

import com.controltier.ctl.types.controller.TaskSequence;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.types.Reference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Mar 19, 2005
 * Time: 7:40:28 AM
 *
 * @ant.task name="tasksequence-add"
 */
public class TaskSequenceAdd extends Task implements TaskContainer {

    /**
     * List of tasks that are added
     */
    private List tasks;

    public void addTask(final Task task) {
        tasks.add(task);
    }

    public TaskSequenceAdd() {
        tasks = new ArrayList();
    }

    public Reference getRefid() {
        return refid;
    }

    public void setRefid(final Reference refid) {
        this.refid = refid;
    }

    /**
     * refid is the id created via TaskSequenceCreate
     */
    private Reference refid;

    private void validateAttributes() {
        if (null == refid) {
            throw new BuildException("refid attribute not set.");
        }
    }

    public void execute() {
        validateAttributes();
        final Object o = getRefid().getReferencedObject(getProject());
        if (null == o) {
            throw new BuildException("refid doesn't point to an object");
        }
        getProject().log("number of tasks to be added: " + tasks.size(), Project.MSG_DEBUG);
        if (o instanceof TaskSequence && tasks.size() > 0) {
            final TaskSequence sequence = (TaskSequence) o;
            for (Iterator iter = tasks.iterator(); iter.hasNext(); ) {
                final Task t = (Task) iter.next();
                sequence.addTask(t);
                getProject().log(t.getTaskName() + " task added to sequence", Project.MSG_VERBOSE);
            }
            getProject().log("number of tasks now in sequence: " + sequence.getTasks().size(), Project.MSG_VERBOSE);
        }
    }
}
