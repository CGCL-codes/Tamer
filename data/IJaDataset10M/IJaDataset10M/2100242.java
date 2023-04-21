package com.google.gwt.event.dom.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * A widget that implements this interface provides registration for
 * {@link DragHandler} instances.
 * 
 * <p>
 * <span style="color:red">Experimental API: This API is still under development
 * and is subject to change. </span>
 * </p>
 */
public interface HasDragHandlers extends HasHandlers {

    /**
   * Adds a {@link DragEvent} handler.
   * 
   * @param handler the drag handler
   * @return {@link HandlerRegistration} used to remove this handler
   */
    HandlerRegistration addDragHandler(DragHandler handler);
}
