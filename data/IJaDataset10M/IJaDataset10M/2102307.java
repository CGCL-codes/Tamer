package com.phloc.commons.id;

import javax.annotation.Nonnull;

/**
 * Interface for objects having a long ID.
 * 
 * @author philip
 * @param <VALUETYPE>
 *        Object type
 */
public interface ISimpleLongIDProvider<VALUETYPE> {

    /**
   * Get the ID of the passed object.
   * 
   * @param aObject
   *        The object who's ID is to be retrieved. May not be <code>null</code>
   *        .
   * @return The ID of the object.
   */
    long getID(@Nonnull VALUETYPE aObject);
}
