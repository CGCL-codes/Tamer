package org.atlantal.api.cms.id;

import org.atlantal.api.cms.util.ContentAccessMode;

/**
 * @author <a href="mailto:masurel@mably.com">Francois MASUREL</a>
 */
public interface CachedContentId extends ContentId {

    /**
     * @param cam cam
     * @return cacheid
     */
    String getCacheId(ContentAccessMode cam);

    /**
     * @return cacheid
     */
    boolean isCacheable();
}
