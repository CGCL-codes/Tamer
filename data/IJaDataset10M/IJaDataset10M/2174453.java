package org.omg.DsObservationAccess;

/**
 * Interface definition: ObservationDataIterator.
 * 
 * @author OpenORB Compiler
 */
public interface ObservationDataIteratorOperations extends org.omg.DsObservationAccess.AbstractManagedObjectOperations {

    /**
     * Operation max_left
     */
    public int max_left();

    /**
     * Operation next_n
     */
    public boolean next_n(int n, org.omg.DsObservationAccess.ObservationDataSeqHolder observation_data_seq);
}
