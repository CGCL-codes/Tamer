package de.hs_mannheim.visualscheduler.scheduling;

import java.util.ArrayList;

/**
 * The Class RoundRobin implement the Round-Robin scheduling algorithm.
 */
public class RoundRobin extends AbstractScheduler {

    private int index = 0;

    private int timeSliceLength;

    @Override
    protected String getAlgorithmName() {
        return "RoundRobin (RR)";
    }

    @Override
    protected ScheduleResult performSchedulingBlock(final ArrayList<Process> active) {
        index %= active.size();
        final ScheduleResult result = new ScheduleResult(active.get(index), Math.min(timeSliceLength, active.get(index).getTimeleft()));
        if (result.timeToWorkFor != active.get(index).getTimeleft()) {
            index++;
        }
        return result;
    }

    @Override
    protected void prepareForScheduling(int timeSliceLength) {
        this.timeSliceLength = timeSliceLength;
        index = 0;
    }

    @Override
    protected boolean isPreemptive() {
        return false;
    }
}
