package com.hongbo.cobweb.nmr.core;

import com.hongbo.cobweb.nmr.api.internal.InternalReference;

/**
 * An internal reference that keeps an internal cache of matching endpoints.
 * Such references need to be notified when the endpoint list changed to
 * update this cache.
 */
public interface CacheableReference extends InternalReference {

    void setDirty();
}
