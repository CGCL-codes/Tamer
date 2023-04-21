package org.omg.DsObservationTimeSeries;

/**
 * Interface definition: TimeSeriesIterator.
 * 
 * @author OpenORB Compiler
 */
public interface TimeSeriesIteratorOperations extends org.omg.DsObservationAccess.AbstractManagedObjectOperations {

    /**
     * Operation max_left
     */
    public int max_left();

    /**
     * Operation next_n
     */
    public boolean next_n(int n, org.omg.DsObservationTimeSeries.ValueSeqHolder curve_part);
}
