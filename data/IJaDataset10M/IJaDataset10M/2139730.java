package org.nakedobjects.noa.reflect;

import org.nakedobjects.noa.adapter.NakedObject;

public interface OneToManyAssociation extends NakedObjectField, OneToManyFeature {

    /**
     * Add the specified element to this collection field in the specified object.
     */
    void addElement(NakedObject inObject, NakedObject element);

    /**
     * Remove all elements from this collection field in the specified object.
     */
    void clearCollection(NakedObject inObject);

    /**
     * Initialise this field in the specified object with the specified set of elements. This is strictly for
     * re-initialising the object and not for adding or removing a specific element, which is only done once.
     */
    void initCollection(NakedObject inObject, NakedObject[] elements);

    /**
     * set up a field within an object - this call should not be transfered to a remote object
     */
    void initElement(NakedObject inObject, NakedObject element);

    /**
     * Determines if the specified element can be added to the collection field. If allowed the add method can
     * be called with the same parameters.
     * 
     * @see #addElement(NakedObject, NakedObject)
     */
    Consent isValidToAdd(NakedObject container, NakedObject element);

    /**
     * Determines if the specified element can be removed from the collection field. If allowed the remove
     * method can be called with the same parameters.
     * 
     * @see #removeElement(NakedObject, NakedObject)
     */
    Consent isValidToRemove(NakedObject container, NakedObject element);

    /**
     * Remove the specified element from this collection field in the specified object.
     */
    void removeElement(NakedObject inObject, NakedObject element);
}
