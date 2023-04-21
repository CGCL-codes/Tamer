package org.matsim.core.router.util;

import org.matsim.api.core.v01.network.Link;

/**
 * A data structure to store temporarily information used
 * by the Dijkstra-algorithm.
 */
public class DijkstraNodeData implements NodeData {

    private Link prev = null;

    private double cost = 0;

    private double time = 0;

    private int iterationID = Integer.MIN_VALUE;

    @Override
    public void resetVisited() {
        this.iterationID = Integer.MIN_VALUE;
    }

    @Override
    public void visit(final Link comingFrom, final double cost, final double time, final int iterID) {
        this.prev = comingFrom;
        this.cost = cost;
        this.time = time;
        this.iterationID = iterID;
    }

    @Override
    public boolean isVisited(final int iterID) {
        return (iterID == this.iterationID);
    }

    @Override
    public double getCost() {
        return this.cost;
    }

    @Override
    public double getTime() {
        return this.time;
    }

    @Override
    public Link getPrevLink() {
        return this.prev;
    }
}
