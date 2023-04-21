package de.objectcode.time4u.client.report.impl.is24;

import de.objectcode.time4u.client.report.impl.TaskPerDayAggregator;
import de.objectcode.time4u.store.IRepository;
import de.objectcode.time4u.store.Project;
import de.objectcode.time4u.store.Task;
import de.objectcode.time4u.store.WorkItemFilter;
import de.objectcode.time4u.store.meta.MetaValue;

public class IS24TaskPerDayAggregator extends TaskPerDayAggregator {

    public IS24TaskPerDayAggregator(IRepository repository, WorkItemFilter filter) {
        super(repository, filter);
    }

    @Override
    public String getProjectName(Project project) {
        MetaValue name = project.getMetaProperty("time4u_report.metaCategory.time4u-report.project", "is24Name");
        if (name == null || name.getValue() == null || name.getValue().toString().length() == 0) return super.getProjectName(project);
        return name.getValue().toString();
    }

    @Override
    public String getTaskName(Task task) {
        MetaValue name = task.getMetaProperty("time4u_report.metaCategory.time4u-report.task", "is24Name");
        if (name == null || name.getValue() == null || name.getValue().toString().length() == 0) return super.getTaskName(task);
        return name.getValue().toString();
    }
}
