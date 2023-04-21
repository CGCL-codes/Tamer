package ch.trackedbean.binding.mapping;

import ch.trackedbean.tracking.*;

/**
 * Interface for elements with status dependency.
 * 
 * @author M. Hautle
 */
public interface IStatusDependent {

    /**
     * This method is called when the state of a property has changed
     * 
     * @param status The new state of the property
     */
    void setStatus(Status status);
}
