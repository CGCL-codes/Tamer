package org.simbrain.network.gui;

import java.awt.event.InputEvent;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolo.event.PPanEventHandler;

/**
 * Pan event handler.
 */
final class PanEventHandler extends PPanEventHandler {

    /** Network Panel. */
    private final NetworkPanel networkPanel;

    /**
     * Create a new pan event handler.
     */
    public PanEventHandler(NetworkPanel networkPanel) {
        super();
        setEventFilter(new PanEventFilter());
        this.networkPanel = networkPanel;
    }

    /**
     * Pan event filter, accepts left mouse clicks, but only when the network
     * panel's edit mode is <code>EditMode.PAN</code>.
     */
    private class PanEventFilter extends PInputEventFilter {

        /**
         * Create a new pan event filter.
         */
        public PanEventFilter() {
            super(InputEvent.BUTTON1_MASK);
        }

        /** @see PInputEventFilter */
        public boolean acceptsEvent(final PInputEvent event, final int type) {
            EditMode editMode = networkPanel.getEditMode();
            if (editMode.isPan() && super.acceptsEvent(event, type)) {
                networkPanel.getTextHandle().stopEditing();
                return true;
            } else {
                return false;
            }
        }
    }
}
