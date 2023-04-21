package edu.oswego.cs.dl.util.concurrent;

/**
 * Thrown by Barrier upon interruption of participant threads
 **/
public class BrokenBarrierException extends RuntimeException {

    /** 
   * The index that barrier would have returned upon
   * normal return;
   **/
    public final int index;

    /**
   * Constructs a BrokenBarrierException with given index
   **/
    public BrokenBarrierException(int idx) {
        index = idx;
    }

    /**
	 * Constructs a BrokenBarrierException with the
	 * specified index and detail message.
	 */
    public BrokenBarrierException(int idx, String message) {
        super(message);
        index = idx;
    }
}
