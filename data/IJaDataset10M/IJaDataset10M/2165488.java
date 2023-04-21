package org.dcm4chee.xero.metadata.filter;

import java.util.concurrent.Future;

/**
 * This interface just combines a future computation and a cache item that computes a given size.  This allows
 * the object that computes the return value to contain the size rather than the item being cached to compute the
 * size - this is useful if the cached item isn't easily extended to add sizing information such as a string.
 * @author bwallace
 *
 * @param <V> defining the type of the object to be created.
 */
public interface SizeableFuture<V> extends Future<V>, CacheItem {
}
