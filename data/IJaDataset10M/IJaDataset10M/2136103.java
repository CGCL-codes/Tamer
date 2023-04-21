package net.solarnetwork.loadtest;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Stateful information for a single node task.
 * 
 * @author matt
 * @version $Revision: 1211 $
 */
public class NodeState {

    private ExecutorService executor;

    private Long nodeId;

    private Map<String, Long> locationIds;

    private Date startDate;

    private Date endDate;

    private Calendar date;

    private Map<String, TaskState> tasks;

    public NodeState(Long nodeId, Map<String, Long> locationIds, Calendar date, Date endDate, int taskCount, ExecutorService executor) {
        this.nodeId = nodeId;
        this.locationIds = Collections.unmodifiableMap(locationIds);
        this.date = date;
        this.startDate = date.getTime();
        this.endDate = endDate;
        this.executor = executor;
        tasks = new LinkedHashMap<String, TaskState>(taskCount);
    }

    public TaskState addTask(String taskName) {
        TaskState ts = new TaskState(this, taskName);
        tasks.put(taskName, ts);
        return ts;
    }

    public Set<String> getTaskNames() {
        return tasks.keySet();
    }

    public TaskState getTaskState(String taskName) {
        return tasks.get(taskName);
    }

    public boolean isFinished() {
        synchronized (date) {
            return date.getTimeInMillis() >= endDate.getTime();
        }
    }

    /**
	 * @return the nodeId
	 */
    public Long getNodeId() {
        return nodeId;
    }

    /**
	 * @return the locationIds
	 */
    public Map<String, Long> getLocationIds() {
        return locationIds;
    }

    /**
	 * @return the startDate
	 */
    public Date getStartDate() {
        return startDate;
    }

    /**
	 * @return the endDate
	 */
    public Date getEndDate() {
        return endDate;
    }

    /**
	 * @return the executor
	 */
    public ExecutorService getExecutor() {
        return executor;
    }
}
