package org.jhotdraw.framework;

import java.awt.Rectangle;
import java.util.EventObject;

/**
 * The event passed to DrawingChangeListeners.
 *
 * @version <$CURRENT_VERSION$>
 */
public class DrawingChangeEvent extends EventObject {

    private Rectangle myRectangle;

    /**
	 *  Constructs a drawing change event.
	 */
    public DrawingChangeEvent(Drawing newSource, Rectangle newRect) {
        super(newSource);
        myRectangle = newRect;
    }

    /**
	 *  Gets the changed drawing
	 */
    public Drawing getDrawing() {
        return (Drawing) getSource();
    }

    /**
	 *  Gets the changed rectangle
	 */
    public Rectangle getInvalidatedRectangle() {
        return myRectangle;
    }
}
