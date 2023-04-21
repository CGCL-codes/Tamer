package com.phloc.commons.parent.impl;

import java.util.Collection;
import javax.annotation.Nullable;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.parent.IChildrenProviderWithID;
import com.phloc.commons.parent.IHasChildren;

/**
 * An implementation of the {@link IChildrenProviderWithID} interface that works
 * with all types that implement {@link IHasChildren} and {@link IHasID}.
 * 
 * @author philip
 * @param <CHILDTYPE>
 *        The data type of the child objects.
 */
public class ChildrenProviderHasChildrenWithID<KEYTYPE, CHILDTYPE extends IHasChildren<CHILDTYPE> & IHasID<KEYTYPE>> extends ChildrenProviderHasChildren<CHILDTYPE> implements IChildrenProviderWithID<KEYTYPE, CHILDTYPE> {

    @Nullable
    public CHILDTYPE getChildWithID(@Nullable final CHILDTYPE aCurrent, @Nullable final KEYTYPE aID) {
        if (aCurrent != null) {
            final Collection<? extends CHILDTYPE> aChildren = aCurrent.getChildren();
            if (aChildren != null) for (final CHILDTYPE aChild : aChildren) if (aChild != null && EqualsUtils.equals(aChild.getID(), aID)) return aChild;
        }
        return null;
    }
}
