package com.phloc.commons.convert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.commons.collections.pair.IReadonlyPair;

/**
 * A unidirectional converter that extracts the first element from an
 * {@link IReadonlyPair}.
 * 
 * @author philip
 */
public final class UnidirectionalConverterPairFirst<DATA1TYPE, DATA2TYPE> implements IUnidirectionalConverter<IReadonlyPair<DATA1TYPE, DATA2TYPE>, DATA1TYPE> {

    @Nullable
    public DATA1TYPE convert(@Nullable final IReadonlyPair<DATA1TYPE, DATA2TYPE> aPair) {
        return aPair == null ? null : aPair.getFirst();
    }

    /**
   * Get a generic data converter that extracts the first element out of a pair.
   * 
   * @param <FIRST>
   *        First type of the pair
   * @param <SECOND>
   *        Second type of the pair.
   * @return The data converter capable of extracting the first item out of a
   *         pair.
   */
    @Nonnull
    public static <FIRST, SECOND> UnidirectionalConverterPairFirst<FIRST, SECOND> create() {
        return new UnidirectionalConverterPairFirst<FIRST, SECOND>();
    }
}
