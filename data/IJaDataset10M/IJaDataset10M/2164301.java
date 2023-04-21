package de.hpi.eworld.networkview.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import de.hpi.eworld.core.ui.MainWindow;
import de.hpi.eworld.gui.util.Point2DUtils;
import de.hpi.eworld.model.db.data.ModelElement;
import de.hpi.eworld.networkview.GraphController;
import de.hpi.eworld.networkview.objects.GraphicsView;
import de.hpi.eworld.networkview.objects.ViewTransferable;

/**
 * is an abstract class for dragging and dropping Views onto and on the GraphView <br/>
 * two methods have to be implemented by the using components because of the different <br/>
 * ways of determiniation of the dragged view and reaction on the drop 
 * @author Christian.Kieschnick
 *
 */
public abstract class ViewDragAndDropHandler implements DragSourceListener, DragGestureListener, DropTargetListener {

    private static final transient Logger logger = Logger.getLogger(ViewDragAndDropHandler.class);

    private static final transient Color TRANSPARENT = new Color(255, 255, 255, 0);

    protected GraphController controller;

    private GhostGlassPane glassPane;

    /**
	 * offset of the mouse cursor to the center of the dragged item in <b>global pixels</b>
	 */
    private Point2D mousePointerOffset = new Point2D.Double();

    private Component dragComponent;

    private Point prevLocation;

    public void setMousePointerOffset(Point2D mousePointerOffset) {
        this.mousePointerOffset = mousePointerOffset;
    }

    public ViewDragAndDropHandler(Component component, GraphController controller, int DnDAction) {
        this.controller = controller;
        this.glassPane = new GhostGlassPane();
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(component, DnDAction, this);
        new DropTarget(glassPane, DnDAction, this, true);
    }

    /**
	 * First checks if the mouse is dragged over the networkView. If so it
	 * checks if the annotationItem is dropable at this point and accepts the
	 * drag (might draw new mouse icon [OS dependent]). If not dropable the
	 * event is rejected.
	 * 
	 * @param event
	 *            given mouseEvent
	 * @author "Gary Yao", "Martin Horst Boissier"
	 */
    private void checkForDragAcceptance(final DropTargetDragEvent event) {
        if (isDroppable(event)) {
            try {
                Point2D globalPoint = controller.getGraphCoordinateConverter().windowToGlobal(event.getLocation());
                GraphicsView<ModelElement> draggedView = getDraggedItem(event);
                if (draggedView.isDroppableAt(globalPoint, controller)) {
                    event.acceptDrag(event.getDropAction());
                } else {
                    event.rejectDrag();
                }
            } catch (Exception e) {
                logger.debug(e);
            }
        }
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent event) {
        if (event.getDropSuccess()) {
            int action = event.getDropAction();
            if (action == DnDConstants.ACTION_COPY) {
            } else if (action == DnDConstants.ACTION_MOVE) {
            } else {
                logger.error("Not support Drag and Drop action");
            }
        }
        glassPane.setVisible(false);
        glassPane.setImage(null);
    }

    @Override
    public void dragEnter(DragSourceDragEvent event) {
    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
        checkForDragAcceptance(event);
    }

    @Override
    public void dragExit(DragSourceEvent event) {
        glassPane.setPoint(new Point(-1000, -1000));
        glassPane.repaint();
    }

    @Override
    public void dragExit(DropTargetEvent event) {
    }

    /**
	 * is responsible to determine the dragged element
	 * @param dragOrigin
	 * @return the view or if no view could determined null
	 */
    protected abstract GraphicsView<ModelElement> getDraggedView(Point dragOrigin);

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
        MainWindow.getInstance().setGlassPane(glassPane);
        GraphicsView<ModelElement> view = getDraggedView(event.getDragOrigin());
        if (view == null) {
            return;
        }
        createGlassPaneImage(view);
        dragComponent = event.getComponent();
        Point windowPoint = SwingUtilities.convertPoint(dragComponent, event.getDragOrigin(), glassPane);
        updateGlassPanePosition(windowPoint);
        glassPane.setVisible(true);
        Transferable transferable = new ViewTransferable<ModelElement>(view);
        try {
            event.startDrag(null, transferable, this);
        } catch (InvalidDnDOperationException idoe) {
            idoe.printStackTrace();
        }
    }

    private Image createImage(double imageWidth, double imageHeight) {
        int width = (int) Math.ceil(imageWidth);
        int height = (int) Math.ceil(imageHeight);
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setBackground(TRANSPARENT);
        g2.clearRect(0, 0, width, height);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return image;
    }

    private void createGlassPaneImage(GraphicsView<ModelElement> view) {
        Image image = createImage(view.getWidth(), view.getHeight());
        Graphics2D canvas = (Graphics2D) image.getGraphics();
        view.paintImage(canvas, new Rectangle2D.Double(0, 0, view.getWidth(), view.getHeight()));
        canvas.dispose();
        glassPane.setImage(image);
    }

    @Override
    public void dragOver(DragSourceDragEvent event) {
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
        Point newLocation = event.getLocation();
        logger.debug("new DragEvent, old: " + newLocation + ", " + prevLocation);
        if (!newLocation.equals(prevLocation)) {
            prevLocation = newLocation;
            updateGlassPanePosition(newLocation);
            glassPane.repaint();
            checkForDragAcceptance(event);
        }
    }

    /**
	 * Computes the correct center point, according to the offset of the mouse cursor, the location of the drag event and the scroll status of the drag component
	 * @param eventLocation the location where in the drag component the event occurred
	 */
    private void updateGlassPanePosition(Point eventLocation) {
        Point2D newPositionWithOffset = Point2DUtils.add(eventLocation, mousePointerOffset);
        glassPane.setPoint(newPositionWithOffset);
    }

    /**
	 * the subclasses implements the drop method for the real drop operation<br/>
	 * i.e. the drop on the canvas
	 * @param dropOrigin
	 * @param view
	 */
    protected abstract void drop(Point dropOrigin, GraphicsView<ModelElement> view);

    @Override
    public void drop(DropTargetDropEvent event) {
        if (isDroppable(event)) {
            try {
                GraphicsView<ModelElement> item = getDroppedItem(event);
                Point dropPoint = getDropPoint(event.getLocation());
                drop(dropPoint, item);
            } catch (Exception e) {
                logger.error("AnnotationItem could not be dropped: " + e.toString());
                e.printStackTrace();
            }
        }
        event.dropComplete(true);
        mousePointerOffset = new Point2D.Double();
        controller.repaint();
    }

    /**
	 * adds <code>mousePointerOffset</code> and converts to <code>Point</code>
	 * @param dropLocation the position where the drop event occurred
	 * @return the converted position where to drop the item
	 */
    private Point getDropPoint(Point2D dropLocation) {
        Point2D globalDropPoint = controller.getGraphCoordinateConverter().windowToGlobal(Point2DUtils.convertPoint(dropLocation));
        globalDropPoint = Point2DUtils.add(globalDropPoint, mousePointerOffset);
        Point dropPoint = controller.getGraphCoordinateConverter().globalToWindow(Point2DUtils.convertPoint(globalDropPoint));
        logger.debug("dropLocation: " + dropLocation);
        logger.debug("mousePointerOffset: " + mousePointerOffset);
        logger.debug("globalDropPoint: " + globalDropPoint);
        logger.debug("dropPoint: " + dropPoint);
        return dropPoint;
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent event) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
    }

    /**
	 * extract the transfered item from the event object
	 * 
	 * @param event
	 * @return
	 */
    @SuppressWarnings("unchecked")
    private GraphicsView<ModelElement> getDraggedItem(DropTargetDragEvent drop) throws UnsupportedFlavorException, IOException {
        Transferable contentWrapper = drop.getTransferable();
        return (GraphicsView<ModelElement>) contentWrapper.getTransferData(ViewTransferable.EWorldAnnotationItemFlavor);
    }

    /**
	 * extract the transfered item from the event object
	 * 
	 * @param event
	 * @return
	 */
    @SuppressWarnings("unchecked")
    private GraphicsView<ModelElement> getDroppedItem(DropTargetDropEvent drop) throws UnsupportedFlavorException, IOException {
        Transferable contentWrapper = drop.getTransferable();
        return (GraphicsView<ModelElement>) contentWrapper.getTransferData(ViewTransferable.EWorldAnnotationItemFlavor);
    }

    /**
	 * is dragged element accepted by this drop target
	 */
    private boolean isDataFlavorDroppable(Transferable contentWrapper) {
        return (contentWrapper != null) && contentWrapper.isDataFlavorSupported(ViewTransferable.EWorldAnnotationItemFlavor);
    }

    /**
	 * this method checks if the drag occurred inside the network view and if
	 * the dragged item is droppable
	 * 
	 * @param drag
	 * @return
	 */
    private boolean isDroppable(DropTargetDragEvent drag) {
        return isDataFlavorDroppable(drag.getTransferable()) && (controller.getViewport().contains(controller.getGraphCoordinateConverter().windowToViewport(drag.getLocation())));
    }

    /**
	 * this method checks if the drop occurred inside the network view and if
	 * the dragged/dropped item is droppable
	 * 
	 * @param event
	 * @return
	 */
    private boolean isDroppable(DropTargetDropEvent drop) {
        return isDataFlavorDroppable(drop.getTransferable()) && (controller.getViewport().contains(controller.getGraphCoordinateConverter().windowToViewport(drop.getLocation())));
    }
}
