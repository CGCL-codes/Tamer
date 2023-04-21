package org.piccolo2d.extras.swt;

import org.piccolo2d.PCamera;
import org.piccolo2d.PNode;
import org.piccolo2d.util.PBounds;
import org.piccolo2d.util.PPickPath;

/**
 * A class for managing the position of a sticky handle.
 */
public class PSWTStickyHandleManager extends PNode {

    private static final long serialVersionUID = 1L;

    private PNode target;

    private PCamera camera;

    /**
     * Creates a sticky handle that will be displayed on the given camera and
     * will update the provided target.
     * 
     * @param camera camera on which to display the sticky handle
     * @param target target being controlled by the handle
     */
    public PSWTStickyHandleManager(final PCamera camera, final PNode target) {
        setCameraTarget(camera, target);
        PSWTBoundsHandle.addBoundsHandlesTo(this);
    }

    /**
     * Changes the associated camera and target for this sticky handle.
     * 
     * @param newCamera new camera onto which this handle should appear
     * @param newTarget new target which this handle will control
     */
    public void setCameraTarget(final PCamera newCamera, final PNode newTarget) {
        camera = newCamera;
        camera.addChild(this);
        target = newTarget;
    }

    /** {@inheritDoc} */
    public boolean setBounds(final double x, final double y, final double width, final double height) {
        final PBounds b = new PBounds(x, y, width, height);
        camera.localToGlobal(b);
        camera.localToView(b);
        target.globalToLocal(b);
        target.setBounds(b);
        return super.setBounds(x, y, width, height);
    }

    /**
     * Always returns true to ensure that they will always be displayed
     * appropriately.
     * 
     * @return true
     */
    protected boolean getBoundsVolatile() {
        return true;
    }

    /** {@inheritDoc} */
    public PBounds getBoundsReference() {
        final PBounds targetBounds = target.getFullBounds();
        camera.viewToLocal(targetBounds);
        camera.globalToLocal(targetBounds);
        final PBounds bounds = super.getBoundsReference();
        bounds.setRect(targetBounds);
        return super.getBoundsReference();
    }

    /** {@inheritDoc} */
    public void startResizeBounds() {
        super.startResizeBounds();
        target.startResizeBounds();
    }

    /** {@inheritDoc} */
    public void endResizeBounds() {
        super.endResizeBounds();
        target.endResizeBounds();
    }

    /**
     * Since PSWTStickyHandle manager is not visible on screen, it just returns
     * false when it is asked to be repainted.
     * 
     * @param pickPath path of this node in which the interaction occurred that
     *            required the repaint
     * 
     * @return always false
     */
    public boolean pickAfterChildren(final PPickPath pickPath) {
        return false;
    }
}
