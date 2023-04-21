package org.eclipse.mylyn.internal.tasks.ui.planner;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.mylyn.monitor.core.StatusHandler;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryQuery;
import org.eclipse.mylyn.tasks.core.AbstractTask;
import org.eclipse.mylyn.tasks.core.AbstractTaskContainer;
import org.eclipse.mylyn.tasks.core.TaskList;

/**
 * @author Ken Sueda
 * @author Mik Kersten
 * @author Rob Elves (scope report to specific categories and queries)
 */
public class TaskReportGenerator implements IRunnableWithProgress {

    private static final String LABEL_JOB = "Mylyn Task Activity Report";

    private boolean finished;

    private TaskList tasklist = null;

    private List<ITaskCollector> collectors = new ArrayList<ITaskCollector>();

    private List<AbstractTask> tasks = new ArrayList<AbstractTask>();

    private Set<AbstractTaskContainer> filterCategories;

    public TaskReportGenerator(TaskList tlist) {
        this(tlist, null);
    }

    public TaskReportGenerator(TaskList tlist, Set<AbstractTaskContainer> filterCategories) {
        tasklist = tlist;
        this.filterCategories = filterCategories != null ? filterCategories : new HashSet<AbstractTaskContainer>();
    }

    public void addCollector(ITaskCollector collector) {
        collectors.add(collector);
    }

    public void collectTasks() {
        try {
            run(new NullProgressMonitor());
        } catch (InvocationTargetException e) {
        } catch (InterruptedException e) {
            StatusHandler.log(e, "Could not collect tasks");
        }
    }

    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        Set<AbstractTaskContainer> rootElements;
        if (filterCategories.size() == 0) {
            rootElements = tasklist.getRootElements();
        } else {
            rootElements = filterCategories;
        }
        int estimatedItemsToProcess = rootElements.size();
        monitor.beginTask(LABEL_JOB, estimatedItemsToProcess);
        for (Object element : rootElements) {
            monitor.worked(1);
            if (element instanceof AbstractTask) {
                AbstractTask task = (AbstractTask) element;
                for (ITaskCollector collector : collectors) {
                    collector.consumeTask(task);
                }
            } else if (element instanceof AbstractRepositoryQuery) {
                AbstractRepositoryQuery repositoryQuery = (AbstractRepositoryQuery) element;
                for (AbstractTask task : repositoryQuery.getChildren()) {
                    for (ITaskCollector collector : collectors) {
                        collector.consumeTask(task);
                    }
                }
            } else if (element instanceof AbstractTaskContainer) {
                AbstractTaskContainer cat = (AbstractTaskContainer) element;
                for (AbstractTask task : cat.getChildren()) for (ITaskCollector collector : collectors) {
                    collector.consumeTask(task);
                }
            }
        }
        for (ITaskCollector collector : collectors) {
            tasks.addAll(collector.getTasks());
        }
        finished = true;
        monitor.done();
    }

    public List<AbstractTask> getAllCollectedTasks() {
        return tasks;
    }

    public boolean isFinished() {
        return finished;
    }
}
