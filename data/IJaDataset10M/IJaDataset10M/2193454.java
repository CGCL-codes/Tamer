package uebung06.as.aufgabe02;

/**
 * Runtime exception thrown when one tries to perform operation on an empty
 * priority queue.
 */
public class EmptyPriorityQueueException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmptyPriorityQueueException(String err) {
        super(err);
    }
}
