package de.hpi.eworld.networkview.model;

import de.hpi.eworld.gui.util.Point2DUtils;
import java.awt.Component;
import java.awt.geom.Point2D;
import java.awt.event.MouseEvent;

public class GraphicsSceneMouseEvent extends MouseEvent {

    private static final long serialVersionUID = 4394568837144367673L;

    private final AbstractItem item;

    public GraphicsSceneMouseEvent(final Component source, final AbstractItem item) {
        super(source, 0, 0, 0, -1000, -1000, 0, false, MouseEvent.NOBUTTON);
        this.item = item;
    }

    public GraphicsSceneMouseEvent(final MouseEvent e, final AbstractItem item) {
        super((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
        this.item = item;
    }

    /**
	 * Returns the mouse cursor position in item coordinates.
	 * 
	 * @param e
	 *            MouseEvent
	 * @return Mouse cursor position in item coordinates.
	 */
    public Point2D getLocationInScene() {
        if (item.getScene() != null) {
            return item.getScene().mapToScene(getLocationOnScreen());
        } else {
            return item.getPos();
        }
    }

    /**
	 * Returns the mouse cursor position in item coordinates.
	 * 
	 * @param e
	 *            MouseEvent
	 * @return Mouse cursor position in item coordinates.
	 */
    public Point2D getLocation() {
        if (item.getScene() != null) {
            return Point2DUtils.subtract(getLocationOnScreen(), item.getScene().mapFromScene(item.getPos()));
        } else {
            return item.getPos();
        }
    }
}
