package ro.pub.cs.minerva.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import ro.pub.cs.minerva.resource.Resource;
import ro.pub.cs.minerva.task.Task;
import ro.pub.cs.minerva.task.TaskDepsGraph;

/**
 * A task scheduler that uses topological sorting and simple load balancing over
 * a single resource
 * @author Stefan
 *
 */
public class SimpleTaskScheduler extends TaskScheduler {

    private class ResourceStatus {

        public int nextIdleTime = 0;

        public ResourceStatus() {
        }
    }

    private int currentTime = 0;

    private HashMap<String, ResourceStatus> resourceStatus;

    private List<Task> topologicOrderList = new LinkedList<Task>();

    public SimpleTaskScheduler() {
        super();
    }

    public void scheduleAllTasks() {
        while (hasTasksLeft()) scheduleNextTask();
    }

    public void scheduleNextTask() {
        if (!hasTasksLeft()) throw new SchedulerException("No tasks left to process");
        if (resources.size() == 0) throw new SchedulerException("No resources avaliable");
        Task nextTask = topologicOrderList.get(0);
        Iterator<Resource> resIterator = resources.values().iterator();
        Resource resource = resIterator.next();
        TaskMapping mapping = new TaskMapping(resource, nextTask);
        mapping.setStartTime(currentTime);
        mapping.setStatus(TaskMappingStatus.MAPPED);
        taskDepsGraph.removeNode(nextTask);
        topologicOrderList.remove(0);
        currentTime += nextTask.getTaskReqs().getProcessingTime();
        scheduleResult.addMapping(mapping);
    }

    public boolean hasTasksLeft() {
        return !topologicOrderList.isEmpty();
    }

    public void addTaskDepsInfo(TaskDepsGraph newDepsInfo) {
        super.addTaskDepsInfo(newDepsInfo);
        List<Task> newTopoList = taskDepsGraph.topologicalSort();
        topologicOrderList.clear();
        topologicOrderList.addAll(newTopoList);
    }
}
