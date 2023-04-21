package org.datanucleus.sco;

/**
 * Representation of a wrapper for a mutable List SCO type supported.
 **/
public interface SCOList extends SCOCollection {

    /**
     * Overload the basic List set() method to allow turning off of the dependent-field
     * deletion process.
     * @param index The index to set the element at
     * @param element The element
     * @param allowDependentField Whether to allow dependent-field deletes
     * @return The previous object at this position
     */
    public Object set(int index, Object element, boolean allowDependentField);
}
