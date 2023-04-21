package com.phloc.commons.name;

import javax.annotation.Nullable;

/**
 * Default implementation of {@link INameProvider} for {@link IHasName} objects.
 * 
 * @author philip
 */
public final class NameProviderFromHasName implements INameProvider<IHasName> {

    @Nullable
    public String getName(@Nullable final IHasName aObject) {
        return aObject == null ? null : aObject.getName();
    }
}
