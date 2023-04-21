package com.sworddance.taskcontrol;

import java.util.Comparator;

/**
 * used to determine which tasks to release next to the eligible run queue.
 * <ol>
 * <li>t1 (ready to run) < t2 (not ready to run)</li>
 * <li>t1.sequenceId (not ready to run) < t2.sequenceId (not ready to run)</li>
 * </ol>
 * The sorting of the not-ready-to-run tasks could be made more complex by for
 * example, looking at the dependencies but if they are not ready to run then
 * why bother, unless trying to figure out why a task did not run, and the tasks
 * that directly dependent on failed tasks need to bubble to surface.
 */
public class PossibleWorkItemComparator implements Comparator<PrioritizedTask>, Cloneable {

    private boolean completeSort;

    public PossibleWorkItemComparator() {
    }

    public PossibleWorkItemComparator(boolean completeSort) {
        this.setCompleteSort(completeSort);
    }

    /**
     * Compare two PrioritizedTasks and return the sort order for these tasks.
     *
     * @param left
     * @param right
     * @return result of comparing left and right.
     */
    public int compare(PrioritizedTask left, PrioritizedTask right) {
        if (left.isReadyToRun()) {
            if (!right.isReadyToRun()) {
                return -1;
            }
        } else if (right.isReadyToRun()) {
            return 1;
        } else if (isCompleteSort()) {
            if (left instanceof DefaultDependentPrioritizedTask && ((DefaultDependentPrioritizedTask) left).isDependentOn(right)) {
                return 1;
            }
            if (right instanceof DefaultDependentPrioritizedTask && ((DefaultDependentPrioritizedTask) right).isDependentOn(left)) {
                return -1;
            }
        }
        return left.getPriority() == right.getPriority() ? left.getSequence() - right.getSequence() : right.getPriority() - left.getPriority();
    }

    public void setCompleteSort(boolean completeSort) {
        this.completeSort = completeSort;
    }

    public boolean isCompleteSort() {
        return completeSort;
    }

    /**
     * using clone to avoid changing behavior of other threads that may be using
     * this comparator.
     *
     * @see java.lang.Object#clone()
     */
    @Override
    public PossibleWorkItemComparator clone() {
        return new PossibleWorkItemComparator(this.completeSort);
    }
}
