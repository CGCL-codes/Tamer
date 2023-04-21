package nl.BobbinWork.diagram.model;

import java.util.List;

/**
 * Diagram section containing multiple threads segments.
 * 
 * 
 * @author J. Pol
 */
public abstract class MultipleThreadsPartition extends Partition {

    /** Threads going into and coming out of a Group/Stitch/Cross/Twist. */
    private Connectors<ThreadSegment> threadConnectors;

    MultipleThreadsPartition() {
    }

    Connectors<ThreadSegment> getThreadConnectors() {
        return threadConnectors;
    }

    /**
     * Puts thread segments in the ins and outs. The constructor of the subclasses
     * Cross and Twist offer the back and front segments in the correct order for
     * the ins. The Connectors class sets the outs in the the reversed order.
     * 
     * @param threadConnectors
     */
    void setThreadConnectors(Connectors<ThreadSegment> threadConnectors) {
        this.threadConnectors = threadConnectors;
    }

    public Bounds<ThreadSegment> getBounds() {
        if (threadConnectors == null) return null;
        return threadConnectors.getBounds();
    }

    public int getNrOfPairs() {
        if (threadConnectors == null) return 0;
        final List<ThreadSegment> ins = threadConnectors.getIns();
        if (ins == null) return 0;
        return ins.size();
    }
}
