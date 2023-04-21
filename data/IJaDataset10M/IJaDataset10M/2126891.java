package mipt.math.sys.schedule.online;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import mipt.math.Number;
import mipt.math.sys.schedule.ScheduleEnvironment;
import mipt.math.sys.schedule.ScheduleProblem;
import mipt.math.sys.schedule.Task;
import mipt.math.sys.schedule.Work;
import mipt.math.sys.schedule.Worker;

/**
 * The (current) result of online scheduling (when workers are resource pools).
 * @see SlotSchedule
 * In contrast to {@link SlotSchedule}, if one task is assigned to several slots,
 *  only first slots' lists contain the task.
 * Note that this object is input data too (initial adding slots + each time when the new job appears)
 *  so it stores the available efforts ("capacity") for each time slot - through a list of EmptyTasks.
 * Some interface methods {@link #getWorkerTasks(Worker)} and {@link #getTasks(Number, double)}
 *  has no sense for online scheduling however the last one is formally implemented
 *  through {@link #getTasks(Number)}.   
 * @author Evdokimov
 */
public class PreliminarySchedule extends SlotSchedule {

    /**
	 * For the problem to be compete, addSlot must be called.
	 * @param problem
	 */
    public PreliminarySchedule(ScheduleProblem problem) {
        super(problem);
    }

    /**
	 * Adds or replaces the given slot to the schedule with the given capacity for each worker (resource pool).
	 * @param slotIndex - can be -1 (to add to the end) or can be more than the last index (to create empty slots between). 
	 * @param capacities - capacities for each worker (resource pool).
	 * @param workers - the size of list must be the same as in capacities.
	 */
    public void addSlot(int slotIndex, List<Number> capacities, Collection<Worker> workers) {
        addSlotTasks(slotIndex, createEmptyTasks(slotIndex, capacities, workers));
    }

    /**
	 * 
	 */
    protected final List<Task> createEmptyTasks(int slotIndex, List<Number> capacities, Collection<Worker> workers) {
        List<Task> ets = initList(capacities.size());
        Iterator<Number> c = capacities.iterator();
        Iterator<Worker> w = workers.iterator();
        while (c.hasNext() && w.hasNext()) {
            ets.add(createEmptyTask(slotIndex, c.next(), w.next()));
        }
        return ets;
    }

    /**
	 * Overridden in order to a) add task to list only 1 time; b) not use task's start time to reserve.
	 * It is implied that only one resource from the resource pool (worker) will do the task:
	 *  even if there is much capacity in the given slot, reservation is limited to the slot duration.
	 */
    @Override
    protected Number addTask(EmptyTask available, int slotIndex, Task task, Number reserved) {
        Number existing = getSlotDuration().copy().add(-getCapacityReserve()), required = task.getDuration();
        if (reserved == null) {
            getTasks(slotIndex).add(task);
        } else {
            required = Number.minus(required, reserved);
        }
        return reserve(available, slotIndex, reserved, existing, required);
    }

    /**
	 * Overridden because the task is added to first slot only.
	 */
    @Override
    protected void removeFailedTask(int slotIndex, Task task) {
        Number unreserved = null;
        while (slotIndex >= 0) {
            unreserved = removeTask(slotIndex--, task, unreserved);
        }
    }

    /**
	 * Returns capacity of the first worker who can do the given work (there is only one such worker if workers are resource pools)
	 */
    public final EmptyTask getEmptyTask(int slotIndex, Work work, ScheduleProblem.Filter workerFilter) {
        List<Task> tasks = getEmptyTasks(slotIndex);
        if (tasks == null) return null;
        for (Task t : tasks) {
            if (workerFilter.isWorkDoable(work, t.getWorker())) return (EmptyTask) t;
        }
        return null;
    }

    /**
	 * Returns the number of individual workers in the given group i.e.
	 *  the number of tasks which can be performed simultaneously 
	 * Is computed as {@link #getCapacity(int, Worker)}/{@link #getSlotDuration()}
	 *  although this formula does not take gaps into account.
	 * The result can be not integer if not all individual workers work all the slot time.
	 */
    public Number getConcurrency(int slotIndex, Worker worker) {
        Number c = getCapacity(slotIndex, worker);
        return c == null ? null : c.copy().mult(1. / ScheduleEnvironment.getInstance().getScheduleSlot());
    }
}
