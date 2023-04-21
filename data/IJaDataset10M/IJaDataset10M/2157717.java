package com.phloc.commons.parent.impl;

import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import com.phloc.commons.parent.IChildrenProviderSorted;
import com.phloc.commons.parent.IHasChildrenSorted;

/**
 * An {@link IChildrenProviderSorted} implementation for object implementing the
 * {@link IHasChildrenSorted} interface.
 * 
 * @author philip
 * @param <CHILDTYPE>
 *        The data type of the child objects.
 */
@Immutable
public final class ChildrenProviderHasChildrenSorted<CHILDTYPE extends IHasChildrenSorted<CHILDTYPE>> extends ChildrenProviderHasChildren<CHILDTYPE> implements IChildrenProviderSorted<CHILDTYPE> {

    @Override
    @Nullable
    public List<? extends CHILDTYPE> getChildren(@Nullable final CHILDTYPE aCurrent) {
        return aCurrent == null ? null : aCurrent.getChildren();
    }

    @Nullable
    public CHILDTYPE getChildAtIndex(@Nullable final CHILDTYPE aCurrent, @Nonnegative final int nIndex) {
        return aCurrent == null ? null : aCurrent.getChildAtIndex(nIndex);
    }
}
