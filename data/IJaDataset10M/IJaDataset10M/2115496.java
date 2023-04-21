package org.yawlfoundation.yawl.cost.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Map;

/**
 * Stores the set of timestamps for a task (i.e. across a number of event nodes)
 */
class TaskTimings {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private String taskName;

    private String instanceID;

    private String scheduled;

    private String allocated;

    private Map<String, String> started;

    private Map<String, String> completed;

    private Map<String, String> suspended;

    private Map<String, Long> totalSuspended;

    private boolean processed = false;

    /**
     * Constructs a new TaskTimings object
     * @param event the initial event for a particular task instance
     */
    public TaskTimings(UnbundledEvent event) {
        taskName = event.getName();
        instanceID = event.getInstance();
    }

    /**
     * Adds the timestamp for the transition found in an event
     * @param event the event to store the timestamp for
     */
    public void update(UnbundledEvent event) {
        processIfSuspended(event);
        if (event.hasTransition("schedule")) {
            scheduled = event.getTimestamp();
        } else if (event.hasTransition("allocate")) {
            allocated = event.getTimestamp();
        } else if (event.hasTransition("start")) {
            if (started == null) started = new Hashtable<String, String>();
            started.put(event.getInstance(), event.getTimestamp());
            if (allocated == null) allocated = event.getTimestamp();
        } else if (event.isCompletedTransition()) {
            if (completed == null) completed = new Hashtable<String, String>();
            completed.put(event.getInstance(), event.getTimestamp());
        } else if (event.hasTransition("suspend")) {
            if (suspended == null) suspended = new Hashtable<String, String>();
            suspended.put(event.getInstance(), event.getTimestamp());
        } else if (event.hasTransition("withdraw")) {
            allocated = null;
        }
    }

    /**
     * @return the number of msecs that expired from when the task was offered
     * until when it was allocated to a resource
     */
    public long getAssignedTime() {
        return timeDiff(allocated, scheduled);
    }

    /**
     * @param instance the instance id of the work item
     * @return the number of msecs that expired from when the work item was
     * allocated until when it was started by a resource
     */
    public long getAllocatedTime(String instance) {
        return timeDiff(started.get(instance), allocated);
    }

    /**
     * @param instance the instance id of the work item
     * @return the number of msecs that expired from when the work item was started
     * until when it was completed or cancelled
     */
    public long getBusyTime(String instance) {
        return timeDiff(completed.get(instance), started.get(instance));
    }

    /**
     * @param instance the instance id of the work item
     * @return the number of msecs that expired from when the work item was enabled
     * until when it was started
     */
    public long getInactiveTime(String instance) {
        return timeDiff(started.get(instance), scheduled);
    }

    /**
     * @param instance the instance id of the work item
     * @return the number of msecs that expired from when the work item was started
     * until when it was completed or cancelled, less any time spent suspended
     */
    public long getActiveTime(String instance) {
        return getBusyTime(instance) - getSuspendedTime(instance);
    }

    /**
     * @param instance the instance id of the work item
     * @return the number of msecs that expired while the work item was suspended
     */
    public long getSuspendedTime(String instance) {
        if (totalSuspended != null) {
            Long time = totalSuspended.get(instance);
            if (time != null) {
                return time;
            }
        }
        return 0;
    }

    public void setProcessed(boolean b) {
        processed = b;
    }

    public boolean isProcessed() {
        return processed;
    }

    /**
     * If the last event processed was a suspend event, calculate and store
     * how long the work item was suspended for
     * @param event the current event
     */
    private void processIfSuspended(UnbundledEvent event) {
        String instance = event.getInstance();
        if (isSuspended(instance) && (!event.hasTransition("suspend"))) {
            String suspTimestamp = suspended.remove(instance);
            if (suspTimestamp != null) {
                long suspTime = timeDiff(event.getTimestamp(), suspTimestamp);
                if (suspTime > -1) {
                    addSuspendedTime(instance, suspTime);
                }
            }
        }
    }

    /**
     * Calculate and store a period of suspension for a work item
     * @param instance the work item instance id
     * @param time the amount of time to store
     */
    private void addSuspendedTime(String instance, long time) {
        if (totalSuspended == null) totalSuspended = new Hashtable<String, Long>();
        Long prevTime = totalSuspended.get(instance);
        if (prevTime != null) {
            time += prevTime;
        }
        totalSuspended.put(instance, time);
    }

    /**
     * Checks whether the last processed event was a suspend event
     * @param instance the work item instance
     * @return true if the last event for this work item was a suspend event
     */
    private boolean isSuspended(String instance) {
        return (suspended != null) && suspended.containsKey(instance);
    }

    /**
     * Converts a timestamp string to a msec representation of it
     * @param timestamp the timestamp to convert
     * @return its msec equivalent, or -1 if the timestamp can't be parsed
     */
    private long timestampToLong(String timestamp) {
        if (timestamp == null) return -1;
        try {
            return sdf.parse(timestamp).getTime();
        } catch (ParseException pe) {
            return -1;
        }
    }

    /**
     * Calculates the number of msecs difference between two timestamp strings
     * @param later the later timestamp
     * @param earlier the earlier timestamp
     * @return the number of msecs difference
     */
    private long timeDiff(String later, String earlier) {
        return timeDiff(timestampToLong(later), timestampToLong(earlier));
    }

    /**
     * Calculates the number of msecs difference between two times
     * @param later the later time
     * @param earlier the earlier time
     * @return later-earlier, iff each has a value > -1 and later > earlier,
     * otherwise -1 is returned
     */
    private long timeDiff(long later, long earlier) {
        return (later > -1) && (earlier > -1) && (later > earlier) ? (later - earlier) : -1;
    }
}
