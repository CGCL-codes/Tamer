package org.opentrust.jsynch;

import org.opentrust.jsynch.misc.InsertionSortVector;

public class PriorityQueue extends LockableQueue {

    protected InsertionSortVector.SortStrategy sortStrategy = null;

    public PriorityQueue(InsertionSortVector.SortStrategy sortStrategy) {
        super();
        queueItems = new InsertionSortVector(sortStrategy);
    }

    public PriorityQueue(InsertionSortVector.SortStrategy sortStrategy, int maxItems) {
        super(maxItems);
        queueItems = new InsertionSortVector(sortStrategy, maxItems);
    }
}
