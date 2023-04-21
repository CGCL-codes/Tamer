package org.datanucleus.store;

import java.util.Iterator;
import org.datanucleus.FetchPlan;

/**
 * Extent of objects within DataNucleus.
 * Represents objects of a type, optionally including the subclasses of that type.
 */
public interface Extent {

    /**
     * Accessor for candidate class of the extent.
     * @return Candidate class
     */
    Class getCandidateClass();

    /**
     * Accessor for whether this extent includes subclasses.
     * @return Whether subclasses are contained
     */
    boolean hasSubclasses();

    ExecutionContext getExecutionContext();

    FetchPlan getFetchPlan();

    /**
     * Accessor for an iterator over the extent.
     * @return The iterator
     */
    Iterator iterator();

    /**
     * Close all iterators and all resources for this extent.
     */
    void closeAll();

    /**
     * Close the specified iterator.
     * @param iterator The iterator
     */
    void close(Iterator iterator);
}
