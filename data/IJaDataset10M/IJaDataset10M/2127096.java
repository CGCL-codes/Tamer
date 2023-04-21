package com.phloc.commons.filter;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import com.phloc.commons.lang.GenericReflection;

@Immutable
public final class FilterNotNull<DATATYPE> implements IFilter<DATATYPE> {

    private static final FilterNotNull<Object> s_aInstance = new FilterNotNull<Object>();

    private FilterNotNull() {
    }

    public boolean matchesFilter(final DATATYPE aValue) {
        return aValue != null;
    }

    @Nonnull
    public static <DATATYPE> FilterNotNull<DATATYPE> getInstance() {
        return GenericReflection.<FilterNotNull<Object>, FilterNotNull<DATATYPE>>uncheckedCast(s_aInstance);
    }
}
