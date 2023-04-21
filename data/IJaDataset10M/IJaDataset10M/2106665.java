package fr.itris.glips.svgeditor.display.selection;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.selection.*;
import fr.itris.glips.svgeditor.shape.*;

/**
 * the class handling the mouse events on a svg canvas
 * @author ITRIS, Jordi SUC
 */
public class SelectionListener extends MouseAdapter implements MouseMotionListener {

    /**
	 * the zone for the double click action
	 */
    protected static final Point2D zoneForDoubleClick = new Point2D.Double(6, 6);

    /**
	 * the time between two clicks in a mouse released event
	 */
    protected static final long doubleClickThreshold = 300;

    /**
	 * the global selection manager
	 */
    private SelectionInfoManager selectionManager = Editor.getEditor().getSelectionManager();

    /**
	 * the initial point for the drag action
	 */
    private Point2D initialDragPoint = null;

    /**
	 * the last point onto which a mouse action (but mouse move) has been done
	 */
    private Point2D currentPoint = null;

    /**
	 * whether the drag action has begun or not
	 */
    private boolean dragStarted = false;

    /**
	 * the selection manager
	 */
    private Selection selection;

    /**
	 * the last time a released mouse action happened
	 */
    private long lastMouseReleasedActionTime = 0;

    /**
	 * the point that was clicked when a released mouse action happened
	 */
    private Point2D lastMouseReleasedActionPoint = null;

    /**
	 * the constructor of the class
	 * @param selection the selection manager
	 */
    public SelectionListener(Selection selection) {
        this.selection = selection;
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        evt = convertEvent(evt);
        int selectionMode = selectionManager.getSelectionMode();
        if (selectionMode != SelectionInfoManager.NONE_MODE && !isPopUp(evt)) {
            Point2D point = selection.getSVGHandle().getTransformsManager().getAlignedWithRulersPoint(evt.getPoint(), false);
            currentPoint = point;
            initialDragPoint = point;
            dragStarted = false;
            if (selectionMode == SelectionInfoManager.DRAWING_MODE) {
                selection.drawingAction(point, evt.getModifiers(), AbstractShape.DRAWING_MOUSE_PRESSED);
            } else if (selectionMode == SelectionInfoManager.ITEMS_ACTION_MODE) {
                selection.itemsAction(point);
            } else if (selectionMode == SelectionInfoManager.ZONE_MODE) {
                boolean isMultiSelectionEnabled = isMultiSelectionEnabled(evt);
                selection.handleSelectionZone(point, Selection.SELECTION_ZONE_MOUSE_PRESSED, isMultiSelectionEnabled);
            } else if (selectionMode == SelectionInfoManager.ZOOM_MODE) {
                selection.handleZoomZone(point, Selection.SELECTION_ZONE_MOUSE_PRESSED);
            }
        } else if (isPopUp(evt)) {
            Editor.getEditor().getSVGModuleLoader().getPopupManager().showPopup(selection.getSVGHandle(), evt.getPoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        evt = convertEvent(evt);
        int selectionMode = selectionManager.getSelectionMode();
        if (selectionMode != SelectionInfoManager.NONE_MODE && selectionMode != SelectionInfoManager.ITEMS_ACTION_MODE && !isPopUp(evt)) {
            Point2D point = currentPoint;
            boolean isMultiSelectionEnabled = isMultiSelectionEnabled(evt);
            int selectionSubMode = selection.getSelectionSubMode();
            switch(selectionMode) {
                case SelectionInfoManager.REGULAR_MODE:
                    if (!evt.isShiftDown() && selectionSubMode != Selection.REGULAR_SUB_MODE) {
                        selection.validateAction(point);
                    } else {
                        selection.setSelection(point, isMultiSelectionEnabled);
                    }
                    break;
                case SelectionInfoManager.ZONE_MODE:
                    selection.handleSelectionZone(point, Selection.SELECTION_ZONE_MOUSE_RELEASED, isMultiSelectionEnabled);
                    break;
                case SelectionInfoManager.ZOOM_MODE:
                    selection.handleZoomZone(point, Selection.SELECTION_ZONE_MOUSE_RELEASED);
                    break;
                case SelectionInfoManager.DRAWING_MODE:
                    boolean isDoubleClick = false;
                    if (lastMouseReleasedActionTime != 0 && lastMouseReleasedActionPoint != null) {
                        long deltaTime = evt.getWhen() - lastMouseReleasedActionTime;
                        Rectangle2D oldZone = new Rectangle2D.Double(point.getX() - zoneForDoubleClick.getX() / 2, point.getY() - zoneForDoubleClick.getY() / 2, zoneForDoubleClick.getX(), zoneForDoubleClick.getY());
                        if (deltaTime < doubleClickThreshold && oldZone.contains(lastMouseReleasedActionPoint)) {
                            isDoubleClick = true;
                        }
                    }
                    lastMouseReleasedActionPoint = point;
                    lastMouseReleasedActionTime = evt.getWhen();
                    int type = isDoubleClick ? AbstractShape.DRAWING_MOUSE_DOUBLE_CLICK : AbstractShape.DRAWING_MOUSE_RELEASED;
                    selection.drawingAction(point, evt.getModifiers(), type);
                    break;
            }
            initialDragPoint = null;
            dragStarted = false;
            currentPoint = null;
        }
    }

    @SuppressWarnings("all")
    public void mouseDragged(MouseEvent evt) {
        evt = convertEvent(evt);
        int selectionMode = selectionManager.getSelectionMode();
        if (selectionMode != SelectionInfoManager.NONE_MODE && selectionMode != SelectionInfoManager.ITEMS_ACTION_MODE && !isPopUp(evt)) {
            Point2D point = selection.getSVGHandle().getTransformsManager().getAlignedWithRulersPoint(evt.getPoint(), false);
            currentPoint = point;
            selection.refreshSVGFrame(evt.getPoint());
            switch(selectionMode) {
                case SelectionInfoManager.REGULAR_MODE:
                    if (!evt.isShiftDown()) {
                        selection.doAction(dragStarted ? point : initialDragPoint);
                    }
                    break;
                case SelectionInfoManager.ZONE_MODE:
                    boolean isMultiSelectionEnabled = isMultiSelectionEnabled(evt);
                    selection.handleSelectionZone(dragStarted ? point : initialDragPoint, Selection.SELECTION_ZONE_MOUSE_DRAGGED, isMultiSelectionEnabled);
                    break;
                case SelectionInfoManager.ZOOM_MODE:
                    selection.handleZoomZone(dragStarted ? point : initialDragPoint, Selection.SELECTION_ZONE_MOUSE_DRAGGED);
                    break;
                case SelectionInfoManager.DRAWING_MODE:
                    selection.drawingAction(dragStarted ? point : initialDragPoint, evt.getModifiers(), AbstractShape.DRAWING_MOUSE_DRAGGED);
                    break;
            }
            dragStarted = true;
        }
    }

    @SuppressWarnings("all")
    public void mouseMoved(MouseEvent evt) {
        evt = convertEvent(evt);
        if (!isPopUp(evt)) {
            int selectionMode = selectionManager.getSelectionMode();
            if (selectionMode != SelectionInfoManager.NONE_MODE) {
                selection.handleCursor(evt.getPoint());
            }
            if (selectionMode == SelectionInfoManager.DRAWING_MODE) {
                Point2D point = selection.getSVGHandle().getTransformsManager().getAlignedWithRulersPoint(evt.getPoint(), false);
                selection.drawingAction(point, evt.getModifiers(), AbstractShape.DRAWING_MOUSE_MOVED);
            }
        }
    }

    /**
	 * checks whether this mouse event denotes a popup trigger event or not
	 * @param evt an event
	 * @return whether this mouse event denotes a popup trigger event or not
	 */
    protected boolean isPopUp(MouseEvent evt) {
        return evt.isPopupTrigger() || SwingUtilities.isRightMouseButton(evt);
    }

    /**
	 * checks whether this mouse event denotes a multi selection action
	 * @param evt an event
	 * @return whether this mouse event denotes a multi selection action
	 */
    protected boolean isMultiSelectionEnabled(MouseEvent evt) {
        return evt.isShiftDown();
    }

    /**
	 * converts the provided event to a svg canvas event
	 * @param evt an event
	 * @return the converted event
	 */
    protected MouseEvent convertEvent(MouseEvent evt) {
        if (!evt.getSource().equals(selection.getSVGHandle().getCanvas())) {
            evt = SwingUtilities.convertMouseEvent((Component) evt.getSource(), evt, selection.getSVGHandle().getCanvas());
        }
        return evt;
    }
}
