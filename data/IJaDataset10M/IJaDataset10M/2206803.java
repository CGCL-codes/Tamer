package com.phloc.commons.tree.withid;

import javax.annotation.Nonnull;
import com.phloc.commons.factory.IHierarchicalFactoryWithParameter;
import com.phloc.commons.factory.IHierarchicalRootFactory;

/**
 * A factory interface that creates tree items.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        The key type.
 * @param <VALUETYPE>
 *        The value type to be contained in tree items.
 */
public interface ITreeItemWithIDFactory<KEYTYPE, VALUETYPE, ITEMTYPE extends ITreeItemWithID<KEYTYPE, VALUETYPE, ITEMTYPE>> extends IHierarchicalFactoryWithParameter<ITEMTYPE, KEYTYPE>, IHierarchicalRootFactory<ITEMTYPE> {

    /**
   * To be called once a tree item is removed from the owning tree. This method
   * is mainly important for the tree with globally unique IDs.
   * 
   * @param aItem
   *        The item that was removed.
   */
    void onRemoveItem(@Nonnull ITEMTYPE aItem);

    /**
   * To be called once a tree item is added to the owning tree. This method is
   * mainly important for the tree with globally unique IDs.
   * 
   * @param aItem
   *        The item that was added.
   */
    void onAddItem(@Nonnull ITEMTYPE aItem);
}
