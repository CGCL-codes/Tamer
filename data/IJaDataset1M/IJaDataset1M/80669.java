package edu.udo.scaffoldhunter.view.scaffoldtree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import edu.udo.scaffoldhunter.view.RenderingQuality;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;

/**
 * Base class for components providing an additional view on a VCanvas.
 * This class provides mechanisms to connect to a VCanvas and preserves
 * consistency with the canvas, such that modifications like adding or
 * removing layers are reflected by this view. 
 * 
 * @author Kriege
 *
 */
public class DependentCanvasMap extends PCanvas {

    /**
     * Canvas viewed by this component. 
     */
    protected VCanvas viewedCanvas;

    /**
     * Change listeners to know when to update.
     */
    private PropertyChangeListener layerListener;

    private PropertyChangeListener interactionListener;

    /**
     * Creates a new instance.
     */
    public DependentCanvasMap() {
        interactionListener = new InteractionListener();
        layerListener = new LayerListener();
        removeInputEventListener(getPanEventHandler());
        removeInputEventListener(getZoomEventHandler());
    }

    /**
     * Creates a new instance and connects a canvas to it.
     * @param canvas the VCanvas this component depends on
     */
    public DependentCanvasMap(VCanvas canvas) {
        this();
        connect(canvas);
    }

    /**
     * @return true iff the component is connected to a canvas
     */
    public boolean isConnected() {
        return viewedCanvas != null;
    }

    /**
     * Connects a new canvas to this component. If it is already
     * connected to a canvas, it will be disconnected first.
     * 
     * @param canvas the <b>VCanvas</b> that should be viewed
     */
    public void connect(VCanvas canvas) {
        if (isConnected()) disconnect();
        viewedCanvas = canvas;
        synchronizeLayers();
        viewedCanvas.getCamera().addPropertyChangeListener(PCamera.PROPERTY_LAYERS, layerListener);
        viewedCanvas.addPropertyChangeListener(VCanvas.PROPERTY_INTERACTING, interactionListener);
    }

    /**
     * Stop this component from receiving events from the viewed canvas
     * and remove all layers.
     */
    public void disconnect() {
        if (!isConnected()) return;
        while (!getCamera().getLayersReference().isEmpty()) {
            getCamera().removeLayer(0);
        }
        viewedCanvas.getCamera().removePropertyChangeListener(PCamera.PROPERTY_LAYERS, layerListener);
        viewedCanvas.removePropertyChangeListener(VCanvas.PROPERTY_INTERACTING, interactionListener);
        viewedCanvas = null;
    }

    /**
     * Removes all references to this object.
     */
    @SuppressWarnings("deprecation")
    public void dispose() {
        disconnect();
        getRoot().getActivityScheduler().removeAllActivities();
        if (PCanvas.CURRENT_ZCANVAS == this) PCanvas.CURRENT_ZCANVAS = null;
        removeInputSources();
    }

    /**
     * Synchronizes the layers of the two canvases.
     */
    public void synchronizeLayers() {
        while (!getCamera().getLayersReference().isEmpty()) {
            getCamera().removeLayer(0);
        }
        for (int i = 0; i < viewedCanvas.getCamera().getLayerCount(); i++) {
            getCamera().addLayer(i, viewedCanvas.getCamera().getLayer(i));
        }
    }

    /**
     * Synchronizes rendering quality changes because of animation.
     */
    @Override
    public boolean getAnimating() {
        if (viewedCanvas == null) return false;
        return (getRoot().getActivityScheduler().getAnimating() || viewedCanvas.getRoot().getActivityScheduler().getAnimating());
    }

    /**
     * Change the rendering quality.
     * 
     * @param newquality quality to be set
     */
    public void setRenderingQuality(RenderingQuality newquality) {
        newquality.setQuality(this);
    }

    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {
        if ((width <= 0) || (height <= 0)) return;
        super.setBounds(x, y, width, height);
    }

    /**
     * Synchronizes the interaction status. The rendering quality of this
     * component may depend on the interaction status.
     */
    private class InteractionListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if ((Boolean) event.getNewValue()) {
                setInteracting(true);
            } else {
                setInteracting(false);
            }
        }
    }

    /**
     * Triggers synchronization of layers.
     */
    private class LayerListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            synchronizeLayers();
        }
    }
}
