package com.hongbo.cobweb.nmr.core;

/**
 * Exception thrown when using a closed channel.
 */
public class ChannelClosedException extends NmrRuntimeException {

    public ChannelClosedException() {
    }

    public ChannelClosedException(String message) {
        super(message);
    }

    public ChannelClosedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelClosedException(Throwable cause) {
        super(cause);
    }
}
