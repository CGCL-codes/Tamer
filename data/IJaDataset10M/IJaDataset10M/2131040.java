package org.apache.ws.commons.tcpmon.core.filter;

/**
 * A filter acting on a stream.
 */
public interface StreamFilter {

    /**
     * Determine whether this filter is read-only. A read-only filter will not
     * modify any data in the stream and exclusively use the skip operation to
     * advance in the stream. The information provided by this method may be
     * used to optimize processing of the stream.
     * 
     * @return <code>true</code> if this filter is read-only
     */
    boolean isReadOnly();

    /**
     * Invoke the filter. This method is called by {@link Pipeline}
     * when data is available for processing. The implementation can
     * modify the stream by discarding bytes from the stream and
     * inserting new data. If it doesn't wish to modify the stream,
     * it should skip the relevant parts, so that it will be processed
     * by the next filter in the pipeline.
     * <p>
     * An implementation is not required to process (skip or discard)
     * all the data available on each invocation. If after the invocation
     * of this method {@link Stream#available()} is non zero, the remaining
     * (unprocessed) data will be available again during the next invocation
     * of the filter. 
     * 
     * @param stream the stream to process
     */
    void invoke(Stream stream);
}
