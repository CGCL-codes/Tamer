package org.xith3d.scenegraph;

import org.xith3d.scenegraph.View.CameraMode;
import org.xith3d.scenegraph.traversal.DetailedTraversalCallback;

/**
 * Foreground defines group which is always rendered last.
 * Additionally, the View can be fixed so it is always rendered with
 * the camera in the same location.
 * 
 * @author William Denniss
 * @author Marvin Froehlich (aka Qudus)
 * @author Amos Wenger (aka BlueSky)
 */
public class Foreground extends LayeredNode {

    /**
     * Initialises Foreground with the camera mode View.VIEW_NORMAL.
     *
     */
    public Foreground() {
        super(CameraMode.VIEW_NORMAL);
    }

    /**
     * Constructs a new Foreground object with the given group 
     * setting the view initially to View.VIEW_NORMAL
     * 
     * @param group the Group to display
     */
    public Foreground(Group group) {
        super(group, CameraMode.VIEW_NORMAL);
    }

    /**
     * Initialises Foreground with given camera mode.
     * 
     * @param cameraMode
     */
    public Foreground(View.CameraMode cameraMode) {
        super(cameraMode);
    }

    /**
     * Constructs a new Foreground object with the given group
     * that will be rendered with the given camera mode
     * 
     * @param group the Geometry to display
     * @param cameraMode the camera mode used to display the group
     */
    public Foreground(Group group, View.CameraMode cameraMode) {
        super(group, cameraMode);
    }

    /**
     * @return the region.
     * 
     * @deprecated use getApplicationBounds() instead.
     */
    public Bounds getRegion() {
        return getApplicationBounds();
    }

    /**
     * @param region The region to set.
     * 
     * @deprecated use setApplicationBounds() instead.
     */
    public void setRegion(Bounds region) {
        setApplicationBounds(region);
    }

    /**
     * Traverses the scenegraph from this node on.
     * If this Node is a Group it will recusively run through each child.
     * 
     * @param callback the listener is notified of any traversed Node on the way
     * @return if false, the whole traversal will stop
     */
    public boolean traverse(DetailedTraversalCallback callback) {
        if (!callback.traversalOperationCommon(this)) return (false);
        if (!callback.traversalOperation(this)) return (false);
        if (getGroup() != null) {
            if (!getGroup().traverse(callback)) return (false);
        }
        return (callback.traversalOperationAfter(this) && callback.traversalOperationCommonAfter(this));
    }
}
