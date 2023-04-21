package com.phloc.commons.tree.withid.unique;

import javax.annotation.concurrent.NotThreadSafe;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;

/**
 * A managed tree is a specialized version of the tree, where each item is
 * required to have a unique ID so that item searching can be performed quite
 * easily.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        The type of the key elements for the tree.
 * @param <VALUETYPE>
 *        The type of the elements contained in the tree
 */
@NotThreadSafe
public class DefaultTreeWithGlobalUniqueID<KEYTYPE, VALUETYPE> extends BasicTreeWithGlobalUniqueID<KEYTYPE, VALUETYPE, DefaultTreeItemWithID<KEYTYPE, VALUETYPE>> {

    public DefaultTreeWithGlobalUniqueID() {
        this(new DefaultTreeItemWithUniqueIDFactory<KEYTYPE, VALUETYPE>());
    }

    public DefaultTreeWithGlobalUniqueID(final ITreeItemWithUniqueIDFactory<KEYTYPE, VALUETYPE, DefaultTreeItemWithID<KEYTYPE, VALUETYPE>> aFactory) {
        super(aFactory);
    }
}
