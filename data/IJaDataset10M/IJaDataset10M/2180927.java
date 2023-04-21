package org.dinopolis.timmon.frontend.treetable;

import java.awt.*;
import java.awt.event.*;

/**
 * Simple logic event to tell, that an automatic scroll was requested by
 * dragging the mousecursor outside the container-bounds.
 *
 * @author  Michael Fahrmair
 * @version Prototype 1
 * @see     util.AutoScrollPane
 */
public class AutoScrollPaneEvent extends ComponentEvent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final int AUTOSCROLLPANE_FIRST = AWTEvent.RESERVED_ID_MAX + 1;

    public static final int AUTOSCROLLPANE_SCROLL_RIGHT = AUTOSCROLLPANE_FIRST;

    public static final int AUTOSCROLLPANE_SCROLL_LEFT = AUTOSCROLLPANE_SCROLL_RIGHT + 1;

    public static final int AUTOSCROLLPANE_SCROLL_UP = AUTOSCROLLPANE_SCROLL_LEFT + 1;

    public static final int AUTOSCROLLPANE_SCROLL_DOWN = AUTOSCROLLPANE_SCROLL_UP + 1;

    public static final int AUTOSCROLLPANE_LAST = AUTOSCROLLPANE_SCROLL_DOWN + 1;

    public AutoScrollPaneEvent(Component source, int id) {
        super(source, id);
    }
}
