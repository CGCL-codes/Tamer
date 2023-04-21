package de.tu_berlin.math.coga.zet.viewer;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class FlowData {

    public final int iteration;

    public final double startTime;

    public final double endTime;

    public final double inflow;

    public final double waittime;

    public final double globalStart;

    public final double globalEnd;

    public final double colorDifference;

    public final double firstEnterExit;

    public final double firstLeaveExit;

    public final double firstAtHead;

    public final double lastEnterExit;

    public final double lastLeaveExit;

    public final double lastAtHead;

    public final double queueLengthForFirst;

    public final double queueLengthForLast;

    public final boolean capGreaterThanInflow;

    public final boolean waittimePositive;

    public FlowData(double startTime, double endTime, double inFlow, double waitTime, double capacity, double transitTime, double corridorCapacity, double exitPosition, int iteration, double globalStart, double globalEnd) {
        this.globalStart = globalStart;
        this.globalEnd = globalEnd;
        this.colorDifference = globalEnd - globalStart;
        this.startTime = startTime;
        this.endTime = endTime;
        this.inflow = inFlow;
        this.waittime = waitTime;
        this.iteration = iteration;
        capGreaterThanInflow = capacity > inflow;
        waittimePositive = waittime > 0;
        if (waittimePositive || !capGreaterThanInflow) {
            firstEnterExit = this.startTime + exitPosition * transitTime;
            firstLeaveExit = firstEnterExit + waittime;
            firstAtHead = firstLeaveExit + (1 - exitPosition) * transitTime;
            lastEnterExit = this.endTime + exitPosition * transitTime;
            lastLeaveExit = lastEnterExit + (waittime + (this.endTime - this.startTime) * (inflow - capacity) / capacity);
            lastAtHead = lastLeaveExit + (1 - exitPosition) * transitTime;
            queueLengthForFirst = waittime * capacity / corridorCapacity;
            queueLengthForLast = queueLengthForFirst + (this.endTime - this.startTime) * (inflow - capacity) / corridorCapacity;
        } else {
            firstAtHead = this.startTime + transitTime;
            lastAtHead = this.endTime + transitTime;
            firstEnterExit = 0;
            firstLeaveExit = 0;
            lastEnterExit = 0;
            lastLeaveExit = 0;
            queueLengthForFirst = 0;
            queueLengthForLast = 0;
        }
    }
}
