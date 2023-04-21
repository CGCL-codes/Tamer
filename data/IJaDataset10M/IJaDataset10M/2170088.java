package com.google.gwt.event.logical.shared;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * A widget that implements this interface is a public source of
 * {@link OpenEvent} events.
 *
 * @param <T> the type being opened
 */
public interface HasOpenHandlers<T> extends HasHandlers {

    /**
   * Adds an {@link OpenEvent} handler.
   *
   * @param handler the handler
   * @return the registration for the event
   */
    HandlerRegistration addOpenHandler(OpenHandler<T> handler);
}
