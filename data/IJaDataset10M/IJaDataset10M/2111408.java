package com.colorado.denver.dataaccess.dataaccessobjects;

import java.util.List;

/**
 * Defines the functionality of a Data Access Object (DAO).
 * @author Dilan Perera
 * @param <TElement> the type of the element being used for persistence.
 */
public interface Dao<TElement> {

    /**
     * Counts the number of elements.
     * @return The number of elements.
     */
    long count();

    /**
     * Selects all elements.
     * @return All elements.
     */
    List<TElement> selectAll();

    /**
     * Selects the element with the given identifier.
     * @param id the identifier of the element.
     * @return The element identified by <code>id</code>.
     */
    TElement selectByID(long id);

    /**
     * Inserts the given element.
     * @param dataElement the element to be created.
     * @return The identifier of the created element.
     */
    long insert(TElement dataElement);

    /**
     * Updates the given element.
     * @param dataElement the element to be updated.
     * @return The number of records affected.
     */
    long update(TElement dataElement);

    /**
     * Forcefully updates the given element.
     * @param dataElement the element to be updated.
     * @return The number of records affected.
     */
    long updateForced(TElement dataElement);

    /**
     * Deletes the element with the given identifier and version.
     * @param id the identifier of the element.
     * @param version the version of the element.
     * @return The number of records affected.
     */
    long delete(long id, long version);

    /**
     * Forcefully deletes the element with the given identifier.
     * @param id the identifier of the element.
     * @return The number of records affected.
     */
    long deleteForced(long id);
}
