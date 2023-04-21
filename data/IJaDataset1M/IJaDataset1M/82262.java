package org.jdesktop.jxlayer.plaf.item;

import java.util.EventObject;
import java.awt.*;
import org.jdesktop.jxlayer.JXLayer;

/**
 * {@code LayerItemChangeEvent} provides notification of changes
 * to a {@code LayerItem}.
 * 
 * @see LayerItem
 * @see LayerItemListener
 */
public class LayerItemChangeEvent extends EventObject {

    /**
     * Creates a new {@code LayerItemEvent} object.
     *
     * @param source the object on which the event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public LayerItemChangeEvent(LayerItem source) {
        super(source);
    }

    /**
     * Returns the {@link LayerItem} on which the event initially occurred.
     *
     * @return the {@link LayerItem} on which the event initially occurred.
     */
    public LayerItem getSource() {
        return (LayerItem) super.getSource();
    }

    /**
     * Since one {@code LayerItemChangeEvent} can be sent to multiple
     * {@link org.jdesktop.jxlayer.JXLayer}s, this method gives an opportunity
     * for the subclasses of {@code LayerItemChangeEvent} to define
     * the precise part of a {@link JXLayer} to be repainted.
     *
     * @param width the current width of a {@link JXLayer}
     * @param height the current height of a {@link JXLayer}
     * @return the part ofa {@link JXLayer} to be repainted  
     */
    public Shape getClip(int width, int height) {
        return null;
    }
}
