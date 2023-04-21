package com.sun.j3d.utils.behaviors.picking;

import com.sun.j3d.utils.behaviors.mouse.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * @deprecated As of Java 3D version 1.2, replaced by
 * <code>com.sun.j3d.utils.picking.behaviors.PickZoomBehavior</code>
 *
 * @see com.sun.j3d.utils.picking.behaviors.PickZoomBehavior
 */
public class PickZoomBehavior extends PickMouseBehavior implements MouseBehaviorCallback {

    MouseZoom zoom;

    int pickMode = PickObject.USE_BOUNDS;

    private PickingCallback callback = null;

    private TransformGroup currentTG;

    /**
   * Creates a pick/zoom behavior that waits for user mouse events for
   * the scene graph. This method has its pickMode set to BOUNDS picking. 
   * @param root   Root of your scene graph.
   * @param canvas Java 3D drawing canvas.
   * @param bounds Bounds of your scene.
   **/
    public PickZoomBehavior(BranchGroup root, Canvas3D canvas, Bounds bounds) {
        super(canvas, root, bounds);
        zoom = new MouseZoom(MouseBehavior.MANUAL_WAKEUP);
        zoom.setTransformGroup(currGrp);
        currGrp.addChild(zoom);
        zoom.setSchedulingBounds(bounds);
        this.setSchedulingBounds(bounds);
    }

    /**
   * Creates a pick/zoom behavior that waits for user mouse events for
   * the scene graph.
   * @param root   Root of your scene graph.
   * @param canvas Java 3D drawing canvas.
   * @param bounds Bounds of your scene.
   * @param pickMode specifys PickObject.USE_BOUNDS or PickObject.USE_GEOMETRY.
   * Note: If pickMode is set to PickObject.USE_GEOMETRY, all geometry object in 
   * the scene graph that allows pickable must have its ALLOW_INTERSECT bit set. 
   **/
    public PickZoomBehavior(BranchGroup root, Canvas3D canvas, Bounds bounds, int pickMode) {
        super(canvas, root, bounds);
        zoom = new MouseZoom(MouseBehavior.MANUAL_WAKEUP);
        zoom.setTransformGroup(currGrp);
        currGrp.addChild(zoom);
        zoom.setSchedulingBounds(bounds);
        this.setSchedulingBounds(bounds);
        this.pickMode = pickMode;
    }

    /**
   * Sets the pickMode component of this PickZoomBehavior to the value of
   * the passed pickMode.
   * @param pickMode the pickMode to be copied.
   **/
    public void setPickMode(int pickMode) {
        this.pickMode = pickMode;
    }

    /**
   * Return the pickMode component of this PickZoomBehavior.
   **/
    public int getPickMode() {
        return pickMode;
    }

    /**
   * Update the scene to manipulate any nodes. This is not meant to be 
   * called by users. Behavior automatically calls this. You can call 
   * this only if you know what you are doing.
   * 
   * @param xpos Current mouse X pos.
   * @param ypos Current mouse Y pos.
   **/
    public void updateScene(int xpos, int ypos) {
        TransformGroup tg = null;
        if (mevent.isAltDown() && !mevent.isMetaDown()) {
            tg = (TransformGroup) pickScene.pickNode(pickScene.pickClosest(xpos, ypos, pickMode), PickObject.TRANSFORM_GROUP);
            if ((tg != null) && (tg.getCapability(TransformGroup.ALLOW_TRANSFORM_READ)) && (tg.getCapability(TransformGroup.ALLOW_TRANSFORM_WRITE))) {
                zoom.setTransformGroup(tg);
                zoom.wakeup();
                currentTG = tg;
            } else if (callback != null) callback.transformChanged(PickingCallback.NO_PICK, null);
        }
    }

    /**
    * Callback method from MouseZoom
    * This is used when the Picking callback is enabled
    */
    public void transformChanged(int type, Transform3D transform) {
        callback.transformChanged(PickingCallback.ZOOM, currentTG);
    }

    /**
    * Register the class @param callback to be called each
    * time the picked object moves
    */
    public void setupCallback(PickingCallback callback) {
        this.callback = callback;
        if (callback == null) zoom.setupCallback(null); else zoom.setupCallback(this);
    }
}
