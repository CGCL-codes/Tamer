package org.axsl.foR;

import org.axsl.foR.fo.Marker;
import org.axsl.foR.fo.RetrieveMarker;

/**
 * Provides the FO Tree with contextual information for cases where its contents
 * may be used differently in one context than in another.
 */
public interface FOContext {

    /**
     * <p>Tells the FO Tree which {@link RetrieveMarker} instance should be the
     * grafting point if a {@link Marker} instance is found in the FO Tree.</p>
     *
     * @return The point to which <code>marker</code> contents found should be
     * grafted.
     */
    RetrieveMarker getRetrieveMarker();
}
