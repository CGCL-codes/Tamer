package com.phloc.commons.aggregate;

import java.util.Collection;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Always returns <code>null</code>.
 * 
 * @author philip
 */
@Immutable
public final class AggregatorAlwaysNull<SRCTYPE, DSTTYPE> implements IAggregator<SRCTYPE, DSTTYPE> {

    @Nullable
    public DSTTYPE aggregate(@Nullable final Collection<SRCTYPE> aResults) {
        return null;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AggregatorAlwaysNull<?, ?>)) return false;
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
