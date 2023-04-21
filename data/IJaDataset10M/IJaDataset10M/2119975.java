package com.google.gwt.event.dom.client;

/**
 * This is a convenience interface that includes all touch handlers defined by
 * the core GWT system.
 * <p>
 * WARNING, PLEASE READ: As this interface is intended for developers who wish
 * to handle all touch events in GWT, new touch event handlers will be added to
 * it. Therefore, updates can cause breaking API changes.
 * </p>
 */
public interface HasAllTouchHandlers extends HasTouchStartHandlers, HasTouchMoveHandlers, HasTouchEndHandlers, HasTouchCancelHandlers {
}
