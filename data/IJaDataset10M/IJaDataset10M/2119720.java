package org.intellij.idea.plugins.xplanner.view;

import org.intellij.idea.plugins.xplanner.model.*;
import org.intellij.idea.plugins.xplanner.PluginConfiguration;

/**
 * @author karpov
 * @since 30.03.2004 18:06:11
 */
public class TaskView extends InfoView implements ModelUpdateListener {

    private final Task task;

    private final Model model;

    public TaskView(final PluginConfiguration configuration, final Task task, final Model model) {
        super(configuration);
        this.task = task;
        this.model = model;
        model.addModelUpdateListener(this);
        setTitle(task.getName());
        setCompletedPercent(task.getPercent());
        setDescription(task.getDescription());
    }

    public void removeNotify() {
        super.removeNotify();
        model.removeModelUpdateListener(this);
    }

    public void modelUpdated() throws ModelException {
        taskUpdated(task);
    }

    public void taskUpdated(final Task task) {
        if (this.task.equals(task)) {
            setTitle(task.getName());
            setCompletedPercent(task.getPercent());
            setDescription(task.getDescription());
        }
    }

    public void storyUpdated(final UserStory story) {
    }
}
