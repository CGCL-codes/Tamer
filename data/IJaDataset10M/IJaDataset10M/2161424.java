package com.phloc.commons.aggregate;

import java.util.Collection;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Always use the complete list of results.
 * 
 * @author philip
 */
@Immutable
public class AggregatorUseAll<DATATYPE> implements IAggregator<DATATYPE, Collection<DATATYPE>> {

    @Nullable
    public Collection<DATATYPE> aggregate(@Nullable final Collection<DATATYPE> aResults) {
        return aResults;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AggregatorUseAll<?>)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeGenerator(this).getHashCode();
    }

    @Override
    public String toString() {
        return new ToStringGenerator(this).toString();
    }
}
