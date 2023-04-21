package edu.ucsb.ccs.jcontractor.examples.heap;

public abstract class PriorityQueue_CONTRACT implements PriorityQueue {

    protected boolean item_Precondition() {
        return !isEmpty();
    }

    protected boolean item_Postcondition(Comparable RESULT) {
        return (RESULT != null);
    }

    protected boolean clear_Postcondition(boolean RESULT) {
        return isEmpty();
    }

    protected boolean size_Postcondition(int RESULT) {
        return RESULT >= 0;
    }

    protected boolean put_Precondition(Comparable newItem) {
        return (newItem != null) && !isFull();
    }

    protected boolean put_Postcondition(Comparable newItem, Void RESULT) {
        return !isEmpty();
    }

    protected boolean remove_Postcondition(Comparable RESULT) {
        return !isFull() && (RESULT != null);
    }
}
