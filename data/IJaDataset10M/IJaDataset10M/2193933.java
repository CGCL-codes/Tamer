package com.phloc.commons.cache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.commons.name.IHasName;
import com.phloc.commons.state.EChange;

/**
 * Interface for a very simple Map-like cache.
 * 
 * @author philip
 * @param <KEYTYPE>
 *        Cache key type.
 * @param <VALUETYPE>
 *        Cache value type.
 */
public interface ISimpleCache<KEYTYPE, VALUETYPE> extends SimpleCacheMBean, IHasName {

    /**
   * Get the cached value associated with the passed key.
   * 
   * @param aKey
   *        The key to be looked up.
   * @return <code>null</code> if no such value is in the cache.
   */
    @Nullable
    VALUETYPE getFromCache(@Nullable KEYTYPE aKey);

    /**
   * Remove the given key from the cache.
   * 
   * @param aKey
   *        The key to be removed.
   * @return {@link EChange#CHANGED} upon success, {@link EChange#UNCHANGED} if
   *         the key was not within the cache,
   */
    @Nonnull
    EChange removeFromCache(@Nullable KEYTYPE aKey);
}
