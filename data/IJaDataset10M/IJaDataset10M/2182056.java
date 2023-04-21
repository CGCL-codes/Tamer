package org.otfeed.protocol.connector;

/**
 * Low-level communication interface: a streamer that knows its session id.
 */
interface ISessionStreamer extends IStreamer {

    public String getSessionId();
}
