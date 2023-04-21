package org.zkoss.ganttz;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.zkoss.ganttz.adapters.IDisabilityConfiguration;
import org.zkoss.ganttz.data.Task;
import org.zkoss.ganttz.data.TaskContainer;
import org.zkoss.ganttz.data.TaskContainer.IExpandListener;
import org.zkoss.zk.ui.ext.AfterCompose;

/**
 * This class contains the information of a task container. It can be modified
 * and notifies of the changes to the interested parties. <br/>
 * @author Lorenzo Tilve Álvaro <ltilve@igalia.com>
 */
public class TaskContainerComponent extends TaskComponent implements AfterCompose {

    public static TaskContainerComponent asTask(Task taskContainerBean, IDisabilityConfiguration disabilityConfiguration) {
        return new TaskContainerComponent((TaskContainer) taskContainerBean, disabilityConfiguration);
    }

    private List<TaskComponent> subtaskComponents = new ArrayList<TaskComponent>();

    private transient IExpandListener expandListener;

    public TaskContainerComponent(final TaskContainer taskContainer, final IDisabilityConfiguration disabilityConfiguration) {
        super(taskContainer, disabilityConfiguration);
        if (!taskContainer.isContainer()) {
            throw new IllegalArgumentException();
        }
        this.expandListener = new IExpandListener() {

            @Override
            public void expandStateChanged(boolean isNowExpanded) {
                updateClass();
            }
        };
        taskContainer.addExpandListener(expandListener);
        for (Task task : taskContainer.getTasks()) {
            getCurrentComponents().add(createChild(task));
        }
    }

    private TaskComponent createChild(Task task) {
        return TaskComponent.asTaskComponent(task, disabilityConfiguration, false);
    }

    @Override
    protected void publishDescendants(Map<Task, TaskComponent> resultAccumulated) {
        for (TaskComponent taskComponent : getCurrentComponents()) {
            taskComponent.publishTaskComponents(resultAccumulated);
        }
    }

    @Override
    protected void remove() {
        if (isExpanded()) {
            for (TaskComponent subtaskComponent : getCurrentComponents()) {
                subtaskComponent.remove();
            }
        }
        super.remove();
    }

    private List<TaskComponent> getCurrentComponents() {
        ListIterator<TaskComponent> listIterator = subtaskComponents.listIterator();
        while (listIterator.hasNext()) {
            if (!getTaskContainer().contains(listIterator.next().getTask())) {
                listIterator.remove();
            }
        }
        return subtaskComponents;
    }

    public boolean isExpanded() {
        return getTaskContainer().isExpanded();
    }

    private TaskContainer getTaskContainer() {
        return (TaskContainer) getTask();
    }

    @Override
    protected String calculateCSSClass() {
        return super.calculateCSSClass() + " " + (getTaskContainer().isExpanded() ? "expanded" : "closed");
    }
}
