package de.objectcode.time4u.client.ui.provider;

import java.util.Collection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import de.objectcode.time4u.client.store.api.ITaskRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.TaskFilter;

public class TaskContentProvider implements IStructuredContentProvider {

    private final ITaskRepository m_taskRepository;

    private final boolean m_onlyActive;

    public TaskContentProvider(final ITaskRepository taskRepository, final boolean onlyActive) {
        m_taskRepository = taskRepository;
        m_onlyActive = onlyActive;
    }

    public Object[] getElements(final Object inputElement) {
        try {
            if (inputElement instanceof ProjectSummary) {
                final ProjectSummary projectSummary = (ProjectSummary) inputElement;
                final Collection<TaskSummary> tasks = m_taskRepository.getTaskSummaries(TaskFilter.filterProjectTasks(projectSummary.getId(), m_onlyActive));
                if (tasks != null) {
                    return tasks.toArray();
                }
            }
        } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
        }
        return new Object[0];
    }

    public void dispose() {
    }

    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
    }
}
