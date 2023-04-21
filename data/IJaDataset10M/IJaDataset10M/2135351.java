package com.phloc.commons.filter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import com.phloc.commons.lang.GenericReflection;

@Immutable
public final class FilterTrue<DATATYPE> implements IFilter<DATATYPE> {

    private static final FilterTrue<Object> s_aInstance = new FilterTrue<Object>();

    private FilterTrue() {
    }

    public boolean matchesFilter(final DATATYPE aValue) {
        return true;
    }

    @Nonnull
    public static <DATATYPE> FilterTrue<DATATYPE> getInstance() {
        return GenericReflection.<FilterTrue<Object>, FilterTrue<DATATYPE>>uncheckedCast(s_aInstance);
    }
}
