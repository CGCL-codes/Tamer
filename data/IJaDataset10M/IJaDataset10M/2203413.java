package org.gridbus.workflow.common;

import java.util.Hashtable;
import org.gridbus.broker.common.ApplicationContext;
import com.ibm.tspaces.TupleSpace;

/**
 * @author Luyao Wu(wuluyao@gmail.com) Master of Engineering in Distributed
 *         Computing 10th Nov, 2006
 */
public class TaskNode extends GraphNode {

    /**
	 * @param id
	 */
    private String name = null;

    protected WfTask wfTask = null;

    private Hashtable fromInputs = new Hashtable();

    private Hashtable fromTasks = new Hashtable();

    private int waitInput = 0;

    private Hashtable toInputs = new Hashtable();

    private TupleSpace tupleSpace = null;

    private boolean isActive = false;

    protected ApplicationContext application;

    public TaskNode(String taskName) {
        super(taskName, TASK_NODE);
    }

    public ApplicationContext getApplication() {
        return application;
    }

    /**
	 * @param application
	 *            The application to set.
	 */
    public void setApplication(ApplicationContext application) {
        this.application = application;
    }

    public void setActive(boolean a) {
        isActive = a;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setwTask(WfTask task) {
        this.wfTask = task;
    }

    public WfTask getwTask() {
        return wfTask;
    }

    public void setInput(String fromTask, String fromPort, String toPortNo) {
        fromTasks.put(toPortNo, fromTask);
        fromInputs.put(fromTask + ":" + fromPort, "");
        toInputs.put(toPortNo, fromTask + ":" + fromPort);
    }

    public String getFromTask(String fromTaskPortNo) {
        return (String) fromTasks.get(fromTaskPortNo);
    }

    public String getFromInput(String fromTaskPortNo) {
        return (String) fromInputs.get(fromTaskPortNo);
    }

    public String getToInput(String toPortNo) {
        return (String) toInputs.get(toPortNo);
    }

    public Hashtable getFromInputs() {
        return fromInputs;
    }

    public Hashtable getToInputs() {
        return toInputs;
    }

    public boolean isFromInputsEmpty() {
        boolean b = fromInputs.isEmpty();
        return b;
    }

    public boolean isToInputsEmpty() {
        int b = toInputs.size();
        if (b == 0) return false; else return true;
    }
}
