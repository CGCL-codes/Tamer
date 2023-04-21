package org.xvr.cam.native_;

import java.net.URL;
import java.util.ArrayList;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.xvr.cam.Activator;
import org.xvr.cam.views.ICameraEventsListeners;

/**
 * An abstract camera intended to be sublcassed by super classes.
 * It defines a method that
 * @author Raffaello
 */
public abstract class AbstractCamera extends Canvas implements PaintListener, DisposeListener, MouseListener {

    protected boolean active;

    protected static Image inactive_camera;

    protected ArrayList<ICameraEventsListeners> listeners;

    static {
        inactive_camera = getImage("icons/camera.png");
    }

    private static Image getImage(String path) {
        URL url = FileLocator.find(Activator.getDefault().getBundle(), new Path(path), null);
        return ImageDescriptor.createFromURL(url).createImage();
    }

    public AbstractCamera(Composite parent, int style) {
        super(parent, style);
        this.addPaintListener(this);
        this.addDisposeListener(this);
        this.active = false;
        this.listeners = new ArrayList<ICameraEventsListeners>();
    }

    /**
	 * Draws the current camera frame.
	 * @param gc {@link GC} where to draw the frame.
	 */
    protected abstract void draw(GC gc);

    /**
	 * Sets the state of the camera as Running. If the camera has been previously stopped or paused the camera stream will be displayed again. 
	 */
    protected abstract void play();

    /**
	 * Sets the camera state to paused. The widget will not be notified of new incoming frame.
	 */
    protected abstract void pause();

    /**
	 * Sets the camera state to stopped and stop the acquisition. The widget will not be notified of new incoming frame.
	 * If the user wants to start again the camera, this one will have to be reinitialized.
	 */
    protected abstract void stop();

    /**
	 * Close the camera widget.
	 */
    protected abstract void close();

    @Override
    public void paintControl(PaintEvent e) {
        this.draw(e.gc);
    }

    @Override
    public void widgetDisposed(DisposeEvent e) {
        this.close();
    }

    /**
	 * Sets the current camera as active or not
	 * @param active a boolean value to sets/unset the current camera as active
	 */
    public void setActive(boolean active) {
        this.active = true;
    }

    public void addCameraListener(ICameraEventsListeners listener) {
        this.listeners.add(listener);
    }

    public void removeCameraListener(ICameraEventsListeners listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
    }

    @Override
    public void mouseUp(MouseEvent e) {
    }
}
