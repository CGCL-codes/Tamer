package com.google.gwt.event.dom.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * A widget that implements this interface provides registration for
 * {@link ContextMenuHandler} instances.
 */
public interface HasContextMenuHandlers extends HasHandlers {

    /**
   * Adds a {@link ContextMenuEvent} handler.
   * 
   * @param handler the context menu handler
   * @return {@link HandlerRegistration} used to remove this handler
   */
    HandlerRegistration addContextMenuHandler(ContextMenuHandler handler);
}
