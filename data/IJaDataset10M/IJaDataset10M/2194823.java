package com.phloc.commons.hierarchy;

import javax.annotation.Nonnull;

/**
 * Interface for walking a hierarchy including possibilities to alter they way
 * how the hierarchy is iterated.
 * 
 * @author philip
 * @param <DATATYPE>
 *        The type of object to be iterated.
 */
public interface IHierarchyWalkerDynamicCallback<DATATYPE> extends IBaseHierarchyWalker {

    /**
   * Called before eventual children of the current item are iterated.
   * 
   * @param aItem
   *        The current item. May not be <code>null</code>.
   * @return A non-<code>null</code> status code that determines how to continue
   *         iteration.
   */
    @Nonnull
    EHierarchyCallbackReturn onItemBeforeChildren(@Nonnull DATATYPE aItem);

    /**
   * Called after eventual children of the current item were iterated.
   * 
   * @param aItem
   *        The current item. May not be <code>null</code>.
   * @return A non-<code>null</code> status code that determines how to continue
   *         iteration.
   */
    @Nonnull
    EHierarchyCallbackReturn onItemAfterChildren(@Nonnull DATATYPE aItem);
}
