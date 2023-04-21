package com.hongbo.cobweb.nmr.api.internal;

import java.util.Map;
import com.hongbo.cobweb.nmr.api.Endpoint;

/**
 * This interface represents a wrapper around an Endpoint
 * which is to be used internally to access the Channel.
 *
 * @version $Revision: $
 * @since 4.0
 */
public interface InternalEndpoint extends Endpoint {

    /**
     * Retrieve the endpoint id
     *
     * @return the id of the endpoint
     */
    String getId();

    /**
     * Retrieve the metadata associated with this endpoint
     *
     * @return a non null map containing the metadata
     */
    Map<String, ?> getMetaData();

    /**
     * Retrieve the channel associated with this endpoint.
     * This method is usually used by {@link Flow}s to deliver
     * exchanges to this endpoint.
     *
     * @return the channel for this endpoint
     */
    InternalChannel getChannel();

    /**
     * Retrieve the inner endpoint.
     *
     * @return the inner endpoint
     */
    Endpoint getEndpoint();
}
