package objectDraw.canvas;

import java.awt.*;
import java.util.*;

/**
 * The interface responsible for providing a graphics context to draw on as well as the user interface.
 *
 */
public interface CanvasLook {

    /**
	 * Gets the canvas that is being displayed.
	 * @return The canvas.
	 */
    public Canvas getCanvas();

    /**
	 * Change the canvas that is used to render the view.
	 * @param c The new canvas.
	 */
    public void setCanvas(Canvas c);

    /**
	 * Find a figure at a given point.
	 * @param point The point to inspect
	 * @return A figure at the point.
	 */
    public Figure findFigure(Point point);

    /**
	 * Add a new listener to handle events
	 * @param l The listener to add.
	 */
    public void addCanvasListener(EventListener l);
}
