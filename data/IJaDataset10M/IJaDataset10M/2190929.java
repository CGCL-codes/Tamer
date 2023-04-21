package com.phloc.commons.collections.pair;

import javax.annotation.Nullable;

/**
 * Represents a basic read only pair.
 * 
 * @author philip
 * @param <DATA1TYPE>
 *        First type.
 * @param <DATA2TYPE>
 *        Second type.
 */
public interface IReadonlyPair<DATA1TYPE, DATA2TYPE> {

    /**
   * @return The first element. May be <code>null</code>.
   */
    @Nullable
    DATA1TYPE getFirst();

    /**
   * @return The second element. May be <code>null</code>.
   */
    @Nullable
    DATA2TYPE getSecond();
}
