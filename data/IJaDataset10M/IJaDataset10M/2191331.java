package uk.ac.lkl.migen.system.ai.um;

/**
 * An object linked to an ID in an {@link ObjectMap}. 
 * 
 * @author sergut
 */
public interface MappedObject {

    /**
     * Returns the id of this object. 
     * return the id of this object.
     */
    int getId();

    /**
     * Returns the name of this object
     * @return the name of this object.
     */
    String getName();
}
