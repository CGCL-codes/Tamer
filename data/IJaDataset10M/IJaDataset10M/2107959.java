package mipt.math.sys.schedule.online;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mipt.math.Number;
import mipt.math.sys.schedule.AbstractScheduler;
import mipt.math.sys.schedule.Job;
import mipt.math.sys.schedule.ScheduleProblem;
import mipt.math.sys.schedule.Task;
import mipt.math.sys.schedule.TasksJob;
import mipt.math.sys.schedule.Worker;
import mipt.math.sys.schedule.rule.ERDRule;
import mipt.math.sys.schedule.rule.Rule;

/**
 * Solver of online scheduling problem: receives jobs one-by-one.
 * TO DO: support receiving "job packages" because it's often more efficient to schedule
 *  jobs with the same property (skill set, type, location) simultaneously.
 * Has a rule to decide where to schedule the job.
 * Does not perform real optimization (rules are heuristics).
 * This implementation does not support precedence constraints on the works
 *  of the given job (TO DO: subclass with this ability).
 * By default, uses ERD rule: attempts to fill schedule from the beginning.
 *  For optimization, stores a map <worker,slot> where slot is the first slot
 *  where the worker (resource pool with the given skills) have non-zero capacity.
 * @author Evdokimov
 */
public class PreliminaryScheduler extends AbstractScheduler {

    private Map<Worker, Integer> startSlots;

    private Rule<Job> jobRule;

    /**
	 * @return jobRule
	 */
    public final Rule<Job> getJobRule() {
        if (jobRule == null) jobRule = initJobRule();
        return jobRule;
    }

    protected Rule<Job> initJobRule() {
        return new ERDRule();
    }

    /**
	 * @param jobRule
	 */
    public void setJobRule(Rule<Job> jobRule) {
        this.jobRule = jobRule;
    }

    /**
	 * @return startSlots
	 */
    protected final Map<Worker, Integer> getFirstAvailableSlots() {
        if (startSlots == null) startSlots = new HashMap<Worker, Integer>(getSchedule().getSlotCount());
        return startSlots;
    }

    /**
	 * The main interface method of online scheduling.
	 * @param job - the job with release datetime and other properties;
	 *  can (but not must) be TasksJob e.g. to configure the task by client.
	 * @return the sequence of tasks (ordered by start time) or null if failed
	 *  due to horizon or if the slot has no capacity of required skills
	 *  (failed job will be in schedule.getFailedJobs()).
	 */
    public List<Task> addJob(Job job) {
        TasksJob tj;
        if (job instanceof TasksJob) {
            tj = (TasksJob) job;
        } else {
            tj = job.useForJob(new TasksJob());
        }
        PreliminarySchedule schedule = getSchedule();
        List<Task> tasks = schedule.initList(tj.getTasks().size());
        Number time1 = getProblem().getHorizon();
        if (!(getJobRule() instanceof ERDRule)) throw new UnsupportedOperationException("PreliminaryScheduler now supports only ERD rule yet");
        for (Task t : tj.getTasks()) {
            Iterator<Worker> workers = getProblem().getWorkersFor(t.getWork());
            if (workers.hasNext()) {
                Worker worker = workers.next();
                t.setWorker(worker);
                Integer s = getFirstAvailableSlots().get(worker);
                int slot = s == null ? 0 : s.intValue();
                slot = findFirstAvailableSlot(slot, t, schedule);
                if (slot >= 0) {
                    t.setStart(schedule.getStart(slot));
                    getFirstAvailableSlots().put(worker, slot);
                    if (schedule.addTask(time1, slot, t)) {
                        tasks.add(t);
                        continue;
                    }
                }
            }
            schedule.addFailedJob(job);
            for (Task other : tasks) schedule.removeTask(other);
            return null;
        }
        Collections.sort(tasks, PreliminarySchedule.taskComparatorByStart);
        return tasks;
    }

    /**
	 * Either the slot found (with enough capacity) must be enough to contain the task.getDuration()
	 *  or it must be followed by slots with enough capacity too.
	 * @return -1 is 
	 */
    protected int findFirstAvailableSlot(int slot, Task t, PreliminarySchedule schedule) {
        int n = schedule.getSlotCount();
        EmptyTask available;
        Number slotDuration = schedule.getSlotDuration();
        main: do {
            available = schedule.getEmptyTask(slot, t.getWorker());
            if (slotDuration.compareTo(available.getCapacity()) > 0) continue;
            if (t.getDuration().compareTo(slotDuration) <= 0) break;
            Number remained = t.getDuration().copy();
            for (int slot1 = slot + 1; slot1 < n; slot1++) {
                available = schedule.getEmptyTask(slot1, t.getWorker());
                if (slotDuration.compareTo(available.getCapacity()) > 0) continue main;
                remained = remained.minus(slotDuration);
                if (remained.compareTo(slotDuration) <= 0) break main;
            }
        } while (++slot < n);
        return slot < n ? slot : -1;
    }

    /**
	 * @see mipt.math.sys.schedule.AbstractScheduler#initSchedule(mipt.math.sys.schedule.ScheduleProblem)
	 */
    protected PreliminarySchedule initSchedule(ScheduleProblem problem) {
        return new PreliminarySchedule(problem);
    }

    @Override
    public PreliminarySchedule getSchedule() {
        return (PreliminarySchedule) super.getSchedule();
    }
}
