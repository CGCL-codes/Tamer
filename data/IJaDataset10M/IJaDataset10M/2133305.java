package DE.FhG.IGD.earth.view;

import DE.FhG.IGD.earth.utils.*;
import DE.FhG.IGD.earth.model.graph.*;
import DE.FhG.IGD.earth.view.behavior.*;
import javax.media.j3d.*;
import javax.swing.JSlider;
import com.sun.j3d.utils.universe.*;

/**
 * This is a special JavaProjector for a cube projection.
 * It shows the model as it is. No extra transformation will be done.
 *
 * Title        : Earth
 * Copyright    : Copyright (c) 2001
 * Organisation : IGD FhG
 * @author      : Werner Beutel
 * @version     : 1.0
 */
public class CubeProjector extends JavaProjector {

    private CubeBehavior behavior_;

    /*************************************************************************
     * Creates an instance of this class
     * @param JComponent container (GUI)
     ************************************************************************/
    public CubeProjector(javax.swing.JComponent container) {
        super(container);
        zoomSlider_ = null;
    }

    /*************************************************************************
     * Returns the ident name
     * @return ident name
     ************************************************************************/
    public String getIdentName() {
        return "CubeProjector";
    }

    /*************************************************************************
     * sets the zoom slider (GUI)
     ************************************************************************/
    public void setZoomSlider(JSlider slider) {
        zoomSlider_ = slider;
    }

    /*************************************************************************
     * returns the zoom slider (GUI)
     ************************************************************************/
    public JSlider getZoomSlider() {
        return zoomSlider_;
    }

    /*************************************************************************
     * Check if this projector is allowed to view the node
     * @param node
     ************************************************************************/
    protected boolean checkLatchGroup(DE.FhG.IGD.earth.model.graph.Node node) {
        if (node.getSubType() != node.SUBTYPE_LatchGroup) return true;
        return ((LatchGroup) node).checkProjector(((LatchGroup) node).CUBE_PROJECTOR);
    }

    /*************************************************************************
     * update MODEL GRAPH
     * @param model
     ************************************************************************/
    protected void updateModelGraph(DE.FhG.IGD.earth.model.graph.ModelGraph model) {
        if (model.getChanges(ModelGraphObject.CHANGE_Create) || firstTime_) {
            canvas3D_ = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
            SimpleUniverse universe;
            universe = new SimpleUniverse(canvas3D_);
            ViewingPlatform viewingPlatform = universe.getViewingPlatform();
            view_ = universe.getViewer().getView();
            view_.setBackClipDistance(300);
            behavior_ = new CubeBehavior(canvas3D_, this);
            behavior_.setSchedulingBounds(mainBounds_);
            viewingPlatform.setViewPlatformBehavior(behavior_);
            newUniverse = true;
            model.setProjectorObject(this, universe);
        }
        if (model.getChanges(ModelGraphObject.CHANGE_RemoveChild)) {
            checkErasedChildren(model);
        }
        if (model.getChanges(ModelGraphObject.CHANGE_Delete)) {
            model.resetProjectorObject(this);
            container_.removeAll();
            canvas3D_ = null;
        }
    }

    /*************************************************************************
     * sets the view position
     * @param latitude
     * @param longitude
     ************************************************************************/
    public void setViewPosition(double longitude, double latitude) {
        if (behavior_ != null) behavior_.setTranslation(longitude, latitude, 0);
    }

    /*************************************************************************
     * nothing to do here (no transformation)
     ************************************************************************/
    protected cMatrix4d PixelTransformer(cMatrix4d x) {
        return x;
    }

    /*************************************************************************
     * nothing to do here (no transformation)
     ************************************************************************/
    protected cPoint3d[] PixelTransformer(cPoint3d x[]) {
        return x;
    }

    /*************************************************************************
     * nothing to do here (no transformation)
     ************************************************************************/
    protected cVector3f[] PixelTransformer(cVector3f x[]) {
        return x;
    }
}
