package com.xsm.lite.event;

/**
 * Represents the response to a request that was processed remotely.
 * These events should be as light as possible, since they would be transported to/from a remote location.
 * 
 * @author Sony Mathew
 */
public abstract class RemoteResponseEvent extends ResponseEvent {

    /**
     * Not to be used - only for serialization purposes.
     * 
     * author Sony Mathew
     */
    public RemoteResponseEvent() {
    }

    public RemoteResponseEvent(String requestKey, Status status) {
        super(requestKey, status);
    }

    /**
     * Indicate whether this response can be cached by its requestKey.
     * By default all responses are NOT cacheable.
     * 
     * author Sony Mathew
     */
    public boolean canCache() {
        return false;
    }
}
