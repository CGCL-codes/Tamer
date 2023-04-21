package scheduler;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import com.trolltech.qt.gui.QStandardItem;

/**
 * @author Sebastian Godlet
 */
public class Task implements Comparable<Task> {

    static int ID_COUNTER = 0;

    static int JOB_ROOT = -1;

    public static int MAX_PRIORITY = 0;

    public static int INITIAL_PRIORITY_RANGE = 10;

    /**
	 * @uml.property name="group"
	 */
    private ThreadGroup group;

    private int id;

    private Job root;

    private ArrayList<Job> jobList;

    private final long creationTime;

    private long lastExecutionTime;

    private long stopTime;

    private int numberOfExecutions;

    private int waitingTime;

    private int runningTime;

    private int lastRunningTime;

    private final QStandardItem[] tableRepresentation;

    private final int maxJob;

    private final BitSet jobsFinished;

    /**
	 * @uml.property name="priority"
	 */
    private int priority;

    public Task(int id, int priority, int maxJob) {
        this.id = id;
        this.jobsFinished = new BitSet(maxJob);
        this.maxJob = maxJob;
        this.jobList = new ArrayList<Job>(22);
        this.group = new ThreadGroup(Integer.toHexString(id));
        this.root = new Job(this, null, JOB_ROOT, -1);
        this.tableRepresentation = new QStandardItem[Environment.getInstance().getStatisticVariables().length];
        final QStandardItem main = new QStandardItem(maxJob, Environment.getInstance().getStatisticVariables().length);
        tableRepresentation[0] = main;
        main.setText("Task #" + id + " [" + priority + "]");
        main.setEditable(false);
        for (int i = 1; i < tableRepresentation.length; i++) tableRepresentation[i] = new QStandardItem();
        tableRepresentation[StatisticVariable.Status.ordinal()].setText("Waiting");
        this.priority = priority;
        System.out.println("T: " + id + " prio : " + priority);
        creationTime = System.currentTimeMillis();
        lastExecutionTime = -1l;
    }

    /**
	 * @return
	 * @uml.property name="priority"
	 */
    public int getPriority() {
        return priority;
    }

    public void decreasePriority() {
        priority++;
        setTableEntry(StatisticVariable.Name, "Task #" + id + " [" + priority + "]");
    }

    public QStandardItem[] getTableRepresentatation() {
        return tableRepresentation;
    }

    public int getID() {
        return id;
    }

    public Job getRoot() {
        return root;
    }

    private final Job[] getParentJobs(int[] ids) {
        if (ids == null || ids.length == 0) return new Job[] { root };
        final Job[] parents = new Job[ids.length];
        for (int i = 0; i < ids.length; i++) {
            parents[i] = jobList.get(ids[i]);
        }
        return parents;
    }

    public final int jobCount() {
        return jobList.size();
    }

    public final int numberOfExecutions() {
        return numberOfExecutions;
    }

    private void setTableEntry(final StatisticVariable var, Object value) {
        QStandardItem item = tableRepresentation[var.ordinal()];
        item.setText(value.toString());
    }

    public void activate() {
        final long newtime = System.currentTimeMillis();
        if (lastExecutionTime > 0) {
            waitingTime += newtime - lastExecutionTime - lastRunningTime;
            setTableEntry(StatisticVariable.WaitingTime, waitingTime);
            setTableEntry(StatisticVariable.AverageWaitingTime, (int) Math.round(1.0 * waitingTime / numberOfExecutions));
        } else setTableEntry(StatisticVariable.ResponseTime, newtime - creationTime);
        lastExecutionTime = newtime;
        setTableEntry(StatisticVariable.NumberOfExecutions, ++numberOfExecutions);
        setTableEntry(StatisticVariable.Status, "Running");
        group.interrupt();
    }

    public void deactive() {
        if (lastExecutionTime >= 0) {
            lastRunningTime = (int) (System.currentTimeMillis() - lastExecutionTime);
            runningTime += lastRunningTime;
            Environment.getInstance().getModel().updateUtilization(lastRunningTime - 10);
            setTableEntry(StatisticVariable.AverageRunningTime, (int) Math.round(1.0 * runningTime / numberOfExecutions));
            setTableEntry(StatisticVariable.RunningTime, runningTime);
            setTableEntry(StatisticVariable.Status, "Waiting");
        }
    }

    public final int getMaxJobCount() {
        return maxJob;
    }

    public Job createJob(final int[] pids) {
        int jid = ++Job.ID_COUNTER;
        int taskindex = jobList.size();
        final Job j = new Job(this, getParentJobs(pids), jid, taskindex);
        jobList.add(j);
        return j;
    }

    void markJobFinished(int taskid) {
        jobsFinished.set(taskid);
        if (isFinished()) {
            stopTime = System.currentTimeMillis();
            setTableEntry(StatisticVariable.Status, "Finished");
            setTableEntry(StatisticVariable.TurnaroundTime, stopTime - creationTime);
        }
    }

    public boolean isFinished() {
        return jobsFinished.cardinality() == jobCount();
    }

    public ThreadGroup getGroup() {
        return group;
    }

    public int compareTo(Task arg0) {
        final int signum = Integer.signum(priority - arg0.priority);
        return (signum == 0 ? Environment.getInstance().getRandom().nextInt(3) - 1 : signum);
    }
}
