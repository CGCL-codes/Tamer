package org.tockit.canvas.events;

import org.tockit.canvas.CanvasItem;
import org.tockit.events.Event;

/**
 * A generic class for all canvas item related events.
 */
public abstract class CanvasItemEvent implements Event {

    private CanvasItem item;

    private int modifiers;

    /**
     * Creates a new event for an item with a specific set of keyboard and mouse modifiers
     * used.
     */
    public CanvasItemEvent(CanvasItem item, int modifiers) {
        this.item = item;
        this.modifiers = modifiers;
    }

    /**
     * Returns the item attached to the event.
     */
    public CanvasItem getItem() {
        return item;
    }

    /**
     * Implements Event.getSource() by returning the item attached to this event.
     */
    public Object getSubject() {
        return item;
    }

    /**
     * Returns the keyboard and mouse modifiers used at the time the event was caused.
     */
    public int getModifiers() {
        return modifiers;
    }
}
