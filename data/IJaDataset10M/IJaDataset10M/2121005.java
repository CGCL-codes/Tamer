package com.jpeterson.x10;

/**
 * Interface used by GatewayQueue to dispatch ControlEvents.
 */
public interface ControlEventDispatcher {

    /**
     * Dispatch ControlEvents.
     */
    public void dispatchControlEvent(ControlEvent event);
}
