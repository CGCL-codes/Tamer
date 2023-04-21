package com.google.gwt.event.dom.client;

import com.google.gwt.dom.client.BrowserEvents;

/**
 * Represents a native drop event.
 */
public class DropEvent extends DragDropEventBase<DropHandler> {

    /**
   * Event type for drop events. Represents the meta-data associated
   * with this event.
   */
    private static final Type<DropHandler> TYPE = new Type<DropHandler>(BrowserEvents.DROP, new DropEvent());

    /**
   * Gets the event type associated with drop events.
   * 
   * @return the handler type
   */
    public static Type<DropHandler> getType() {
        return TYPE;
    }

    /**
   * Protected constructor, use
   * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers)}
   * or
   * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers, com.google.gwt.dom.client.Element)}
   * to fire drop events.
   */
    protected DropEvent() {
    }

    @Override
    public final Type<DropHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DropHandler handler) {
        handler.onDrop(this);
    }
}
