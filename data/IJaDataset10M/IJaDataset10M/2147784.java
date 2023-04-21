package ch.iserver.ace.algorithm;

import java.io.Serializable;

/**
 * This interface represents the concept of a timestamp. Timestamps is a general
 * concept, so this interface remains empty.
 * 
 * @see ch.iserver.ace.algorithm.VectorTime
 */
public interface Timestamp extends Serializable {

    /**
	 * Retrieves the components of the timestamp as an int array. The exact
	 * representation is up to the concrete Timestamp implementation.
	 * 
	 * @return the components of the Timestamp implementation
	 */
    int[] getComponents();
}
