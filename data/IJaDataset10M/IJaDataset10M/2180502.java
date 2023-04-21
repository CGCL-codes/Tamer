package org.swiftgantt.swing.model;

import org.swiftgantt.core.GanttContext;
import org.swiftgantt.core.Time;
import org.swiftgantt.core.layout.LayoutTask;
import com.yxwang.common.gui.tree.TreeWidgetNode;

/**
 * A <code>TaskTreeNode</code> is an object that represent a task in project, it is task node in the tasks tree.<br/>
 * {@link java.beans.PropertyChangeEvent} event raised after:<br/>
 * task start time changed, task end time changed, task progress changed.
 * @author Yuxing Wang
 * @version 1.0
 */
public final class TaskTreeNode extends TreeWidgetNode {

    private static final long serialVersionUID = 1L;

    private LayoutTask data;

    public TaskTreeNode(LayoutTask task) {
        data = task;
    }

    public TaskTreeNode(GanttContext context) {
        data = context.getGanttModel().createTask("Create New Task");
    }

    public TaskTreeNode(GanttContext context, String nodeName, Time start, Time end) {
        data = context.getGanttModel().createTask(nodeName, start, end);
    }

    public LayoutTask getData() {
        return data;
    }

    public void setData(LayoutTask data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TaskTreeNode{" + "data=" + data + '}';
    }
}
