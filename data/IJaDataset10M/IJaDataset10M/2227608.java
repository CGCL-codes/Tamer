package com.ingenico.piccolo.nodes;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragSequenceEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PDimension;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.nodes.P3DRect;

public class PBirdsEyeView extends PCanvas {

    /**
	 * Generated Serial Version UID
	 */
    private static final long serialVersionUID = -301211516099750184L;

    /**
	 * This is the node that shows the viewed area.
	 */
    PNode areaVisiblePNode;

    /**
	 * This is the canvas that is being viewed
	 */
    PCanvas viewedCanvas;

    /**
	 * The change listener to know when to update the birds eye view.
	 */
    PropertyChangeListener changeListener;

    int layerCount;

    /**
	 * Creates a new instance of a BirdsEyeView
	 */
    public PBirdsEyeView() {
        changeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                updateFromViewed();
            }
        };
        areaVisiblePNode = new P3DRect();
        areaVisiblePNode.setPaint(new Color(128, 128, 255));
        areaVisiblePNode.setTransparency(.8f);
        areaVisiblePNode.setBounds(0, 0, 100, 100);
        getCamera().addChild(areaVisiblePNode);
        getCamera().addInputEventListener(new PDragSequenceEventHandler() {

            protected void startDrag(PInputEvent e) {
                if (e.getPickedNode() == areaVisiblePNode) super.startDrag(e);
            }

            protected void drag(PInputEvent e) {
                PDimension dim = e.getDelta();
                viewedCanvas.getCamera().translateView(0 - dim.getWidth(), 0 - dim.getHeight());
            }
        });
        removeInputEventListener(getPanEventHandler());
        removeInputEventListener(getZoomEventHandler());
        setDefaultRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
    }

    public void connect(PCanvas canvas, PLayer viewed_layer) {
        this.viewedCanvas = canvas;
        layerCount = 0;
        getCamera().addLayer(layerCount, viewed_layer);
        System.out.println("Adding " + viewed_layer.getBounds());
        getCamera().setBounds(0, 0, 10, 10);
        updateFromViewed();
        viewedCanvas.getCamera().addPropertyChangeListener(changeListener);
    }

    /**
	 * Add a layer to list of viewed layers
	 */
    public void addLayer(PLayer new_layer) {
        getCamera().addLayer(new_layer);
        layerCount++;
    }

    /**
	 * Remove the layer from the viewed layers
	 */
    public void removeLayer(PLayer old_layer) {
        getCamera().removeLayer(old_layer);
        layerCount--;
    }

    /**
	 * Stop the birds eye view from receiving events from the viewed canvas
	 * and remove all layers
	 */
    public void disconnect() {
        viewedCanvas.getCamera().removePropertyChangeListener(changeListener);
        for (int i = 0; i < getCamera().getLayerCount(); ++i) {
            getCamera().removeLayer(i);
        }
    }

    /**
	 * This method will get called when the viewed canvas changes
	 */
    public void propertyChange(PropertyChangeEvent event) {
        updateFromViewed();
    }

    /**
	 * This method gets the state of the viewed canvas and updates the
	 * BirdsEyeViewer This can be called from outside code
	 */
    public void updateFromViewed() {
        final Rectangle2D curvesBounds = getCamera().getUnionOfLayerFullBounds();
        final PBounds viewBounds = getCamera().getViewBounds();
        final PDimension delta = viewBounds.deltaRequiredToCenter(curvesBounds);
        final PAffineTransform newTransform = getCamera().getViewTransform();
        final double sx = viewBounds.getWidth() / curvesBounds.getWidth();
        final double sy = viewBounds.getHeight() / curvesBounds.getHeight();
        newTransform.translate(delta.width, delta.height);
        if (sx != Double.POSITIVE_INFINITY && sx != 0 && sy != Double.POSITIVE_INFINITY && sy != 0) {
            newTransform.scaleAboutPoint(sx, sy, curvesBounds.getCenterX(), curvesBounds.getCenterY());
        }
        getCamera().animateViewToTransform(newTransform, 0);
        final Rectangle2D cameraBounds = viewedCanvas.getCamera().getViewBounds();
        getCamera().viewToLocal(cameraBounds);
        areaVisiblePNode.setBounds(cameraBounds);
    }

    public void zz_updateFromViewed() {
        double viewedX;
        double viewedY;
        double viewedHeight;
        double viewedWidth;
        double ul_camera_x = viewedCanvas.getCamera().getViewBounds().getX();
        double ul_camera_y = viewedCanvas.getCamera().getViewBounds().getY();
        double lr_camera_x = ul_camera_x + viewedCanvas.getCamera().getViewBounds().getWidth();
        double lr_camera_y = ul_camera_y + viewedCanvas.getCamera().getViewBounds().getHeight();
        Rectangle2D drag_bounds = getCamera().getUnionOfLayerFullBounds();
        System.out.println("" + drag_bounds);
        double ul_layer_x = drag_bounds.getX();
        double ul_layer_y = drag_bounds.getY();
        double lr_layer_x = drag_bounds.getX() + drag_bounds.getWidth();
        double lr_layer_y = drag_bounds.getY() + drag_bounds.getHeight();
        if (ul_camera_x < ul_layer_x) viewedX = ul_layer_x; else viewedX = ul_camera_x;
        if (ul_camera_y < ul_layer_y) viewedY = ul_layer_y; else viewedY = ul_camera_y;
        if (lr_camera_x < lr_layer_x) viewedWidth = lr_camera_x - viewedX; else viewedWidth = lr_layer_x - viewedX;
        if (lr_camera_y < lr_layer_y) viewedHeight = lr_camera_y - viewedY; else viewedHeight = lr_layer_y - viewedY;
        Rectangle2D bounds = new Rectangle2D.Double(viewedX, viewedY, viewedWidth, viewedHeight);
        System.out.println("visible bounds = " + bounds);
        bounds = getCamera().viewToLocal(bounds);
        areaVisiblePNode.setBounds(bounds);
        System.out.println("visible bounds = " + bounds);
        getCamera().animateViewToCenterBounds(drag_bounds, true, 0);
        System.out.println("		" + getCamera().getViewBounds());
    }
}
