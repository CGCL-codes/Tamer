package com.google.gwt.event.dom.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * A widget that implements this interface provides registration for
 * {@link BlurHandler} instances.
 */
public interface HasBlurHandlers extends HasHandlers {

    /**
   * Adds a {@link BlurEvent} handler.
   * 
   * @param handler the blur handler
   * @return {@link HandlerRegistration} used to remove this handler
   */
    HandlerRegistration addBlurHandler(BlurHandler handler);
}
