package com.codemonster.surinam.export.lifecycle;

/**
 * This management interface is for routing interceptors so that they can participate in lifecycle callbacks.
 *
 * @author Samuel Provencher
 */
public interface InterceptorLifecycle {

    /**
     * This callback is for routing interceptors to know that they should flush any references
     * to services they might be holding on to. They may or may not reacquire a new reference at that
     * time (they could choose a lazy-loading strategy).
     */
    public void invalidateReferences();
}
