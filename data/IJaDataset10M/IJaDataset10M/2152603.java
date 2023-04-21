package com.google.gwt.event.dom.client;

import com.google.gwt.dom.client.BrowserEvents;

/**
 * Represents a native double click event.
 */
public class DoubleClickEvent extends MouseEvent<DoubleClickHandler> {

    /**
   * Event type for double click events. Represents the meta-data associated
   * with this event.
   */
    private static final Type<DoubleClickHandler> TYPE = new Type<DoubleClickHandler>(BrowserEvents.DBLCLICK, new DoubleClickEvent());

    /**
   * Gets the event type associated with double click events.
   * 
   * @return the handler type
   */
    public static Type<DoubleClickHandler> getType() {
        return TYPE;
    }

    /**
   * Protected constructor, use
   * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers)}
   * to fire double click events.
   */
    protected DoubleClickEvent() {
    }

    @Override
    public final Type<DoubleClickHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DoubleClickHandler handler) {
        handler.onDoubleClick(this);
    }
}
