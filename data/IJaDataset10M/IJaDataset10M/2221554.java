package at.fhj.utils.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class keeps track of the progress of a time-consuming
 * process, splitting it into tasks and subtasks, so that meaningful
 * progress information can be presented to the user.
 * This class doesn't display or output the progress information
 * itself, instead it notifies registered listeners of the progress
 * and task completion events.
 * 
 * @author Ilya Boyandin
 */
public class ProgressTracker {

    public static final int TASK_NONE = -1;

    private double totalProgress = 0;

    private double taskProgress = 0;

    private String taskName;

    private int taskId;

    private double taskWeight;

    private double taskWeightMultiplier = 1.0;

    private double taskStart;

    private String subtaskName;

    private double subtaskWeight;

    private double subtaskWeightMultiplier = 1.0;

    private double subtaskStart;

    private double subtaskIncUnit = 1.0;

    private double taskIncUnit = 1.0;

    protected List progressListenerList = new ArrayList();

    protected List taskCompletionListenerList = new ArrayList();

    private List subtaskWeightMultipliers;

    private boolean isFinished;

    private boolean isCancelled;

    private long startTime = -1;

    private long taskStartTime = -1;

    public void reset() {
        taskName = null;
        taskStart = 0;
        taskWeight = 0;
        taskWeightMultiplier = 1.0;
        subtaskName = null;
        subtaskStart = 0;
        subtaskWeight = 0;
        subtaskWeightMultiplier = 1.0;
        taskProgress = 0;
        totalProgress = 0;
        if (subtaskWeightMultipliers != null) {
            subtaskWeightMultipliers.clear();
        }
        isFinished = false;
        isCancelled = false;
        taskStartTime = startTime = -1;
    }

    /**
   * Current running task progress
   */
    public double getTaskProgress() {
        return taskProgress;
    }

    /**
   * Current running subtask name 
   */
    public String getSubtaskName() {
        return subtaskName;
    }

    /**
   * Current running task name
   */
    public String getTaskName() {
        return taskName;
    }

    /**
   * Total progress
   */
    public double getTotalProgress() {
        return totalProgress;
    }

    public void multiplyTaskWeight(double alpha) {
        taskWeightMultiplier *= alpha;
    }

    public void multiplySubtaskWeight(double alpha) {
        if (subtaskWeightMultipliers == null) {
            subtaskWeightMultipliers = new ArrayList();
        }
        subtaskWeightMultiplier *= alpha;
        subtaskWeightMultipliers.add(new Double(alpha));
    }

    public void removeLastSubtaskWeightMultiplier() {
        if (subtaskWeightMultipliers != null) {
            final int size = subtaskWeightMultipliers.size();
            if (size > 0) {
                double alpha = ((Double) subtaskWeightMultipliers.remove(size - 1)).doubleValue();
                subtaskWeightMultiplier /= alpha;
            }
        }
    }

    /**
   * Starts a new task. If you want TaskCompletionListeners to be notified
   * of the completion of this task, use <code>startTask(taskName, taskId, taskWeight)</code>
   * instead.
   * 
   * @param taskName Name of the task (for displaying etc)
   * @param taskWeight A real value between 0 and 1. The sum of the weights of all
   *            tasks started during the process must be equal to 1.
   */
    public void startTask(String taskName, double taskWeight) {
        startTask(taskName, TASK_NONE, taskWeight);
    }

    /**
   * Starts a new task
   * 
   * @param taskName Name of the task (for displaying etc)
   * @param taskId If not equal to <code>TASK_NONE</code> the TaskCompletionListeners
   *          will be notified of the completion of this task.
   * @param taskWeight A real value between 0 and 1. The sum of the weights of all
   *            tasks started during the process must be equal to 1.
   */
    public void startTask(String taskName, int taskId, double taskWeight) {
        this.taskStart = totalProgress;
        this.taskName = taskName;
        this.taskId = taskId;
        this.taskWeight = taskWeight;
        this.subtaskName = null;
        this.taskProgress = 0;
        this.subtaskStart = 0;
        final long now = System.currentTimeMillis();
        this.taskStartTime = now;
        if (this.startTime == -1) {
            this.startTime = now;
        }
        fireProgressUpdated();
    }

    /**
   * @param d Inc value between 0.0 and 100.0 
   */
    public void setTaskIncUnit(double d) {
        taskIncUnit = d;
    }

    public void incTaskProgress() {
        incTaskProgress(taskIncUnit);
    }

    /**
   * @param d Inc value between 0.0 and 100.0 
   */
    public void incTaskProgress(double dProgress) {
        if (taskProgress < 100) {
            final double newProgress = taskProgress + dProgress;
            if (taskProgress > 100) {
                taskProgress = 100;
            }
            taskProgress = newProgress;
            totalProgress = taskStart + taskProgress * taskWeight * taskWeightMultiplier;
            fireProgressUpdated();
        }
    }

    public void taskCompleted() {
        totalProgress = taskStart + 100 * taskWeight * taskWeightMultiplier;
        taskProgress = 100;
        if (taskId != TASK_NONE) {
            fireTaskCompleted(taskId);
        }
        fireProgressUpdated();
    }

    public void startSubtask(String subtaskName, double subtaskWeight) {
        this.subtaskStart = taskProgress;
        this.subtaskName = subtaskName;
        this.subtaskWeight = subtaskWeight;
        fireProgressUpdated();
    }

    /**
   * @param d Inc value between 0.0 and 100.0 
   */
    public void setSubtaskIncUnit(double d) {
        subtaskIncUnit = d;
    }

    public void incSubtaskProgress() {
        incSubtaskProgress(subtaskIncUnit);
    }

    /**
   * @param dProgress Inc value between 0.0 and 100.0 
   */
    public void incSubtaskProgress(double dProgress) {
        if (taskProgress < 100) {
            taskProgress += dProgress * subtaskWeight * subtaskWeightMultiplier;
            if (taskProgress > 100) {
                taskProgress = 100;
            }
            totalProgress = taskStart + taskProgress * taskWeight * taskWeightMultiplier;
            fireProgressUpdated();
        }
    }

    public void subtaskCompleted() {
        taskProgress = subtaskStart + 100 * subtaskWeight * subtaskWeightMultiplier;
        totalProgress = taskStart + taskProgress * taskWeight * taskWeightMultiplier;
        fireProgressUpdated();
    }

    /**
   * Adds a progress multiplier for the case when processing starts not from the
   * first task. The multiplier will be chosen so, that the processing of all the
   * tasks beginning from <code>startTask</code> is stretched to 100%.
   * 
   * @param tasksWeights Weights of all the possible tasks (doubles between 0.0 and 1.0).
   *              The sum of the taskWeights must be equal to 1.0.
   * @param startTasks Index of the first task from which the processing starts
   */
    public void stretchTaskWeights(double[] taskWeights, int startTask) {
        if (startTask > 0) {
            multiplyTaskWeight(getStretchMultiplier(taskWeights, startTask));
        }
    }

    public void restretchTaskWeights(double[] taskWeights, int oldStartTask, int newStartTask) {
        if (oldStartTask > 0) {
            multiplyTaskWeight(1.0 / getStretchMultiplier(taskWeights, oldStartTask));
        }
        if (newStartTask > 0) {
            multiplyTaskWeight(getStretchMultiplier(taskWeights, newStartTask));
        }
    }

    private double getStretchMultiplier(double[] taskWeights, int startTask) {
        double r = 1.0;
        for (int i = 0; i < startTask; i++) {
            r -= taskWeights[i];
        }
        return 1.0 / r;
    }

    public void addProgressListener(ProgressListener l) {
        progressListenerList.add(l);
    }

    public void removeProgressListener(ProgressListener l) {
        progressListenerList.remove(l);
    }

    protected void fireProgressUpdated() {
        Iterator it = progressListenerList.iterator();
        while (it.hasNext()) {
            ((ProgressListener) it.next()).progressUpdated();
        }
    }

    public void addTaskCompletionListener(TaskCompletionListener l) {
        taskCompletionListenerList.add(l);
    }

    public void removeTaskCompletionListener(TaskCompletionListener l) {
        taskCompletionListenerList.remove(l);
    }

    protected void fireTaskCompleted(int taskId) {
        Iterator it = taskCompletionListenerList.iterator();
        while (it.hasNext()) {
            ((TaskCompletionListener) it.next()).taskCompleted(taskId);
        }
    }

    public void processFinished() {
        if (!isFinished) {
            isFinished = true;
            fireProcessFinished();
        }
    }

    private void fireProcessFinished() {
        Iterator it = progressListenerList.iterator();
        while (it.hasNext()) {
            ((ProgressListener) it.next()).processFinished();
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void processCancelled() {
        isCancelled = true;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    /**
   * @return  Estimated remaining time for the whole process,
   *       or -1, if the estimation is not possible. Note,
   *       that this estimation makes sense only when the
   *          progress is incremented more or less uniformly.
   */
    public long getEstimatedRemainingTime() {
        if (startTime >= 0) {
            final long now = System.currentTimeMillis();
            if (now - startTime < 100 || totalProgress < 1e-7) {
                return -1;
            }
            final double avgUnitTime = (double) (now - startTime) / totalProgress;
            return Math.round((100 - totalProgress) * avgUnitTime);
        } else {
            return -1;
        }
    }

    /**
   * @return  Estimated remaining time for the current task,
   *       or -1, if the estimation is not possible
   */
    public long getEstimatedTaskRemainingTime() {
        if (taskStartTime >= 0) {
            final long now = System.currentTimeMillis();
            if (now - taskStartTime < 100 || taskProgress < 1e-7) {
                return -1;
            }
            final double avgUnitTime = (double) (now - taskStartTime) / taskProgress;
            return Math.round((100 - taskProgress) * avgUnitTime);
        } else {
            return -1;
        }
    }
}
