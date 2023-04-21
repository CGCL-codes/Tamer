package org.ufba.dp.j3d;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.vecmath.Point3d;

/**
 *
 * @author Rafael
 */
class InnerView {

    Canvas3D canvas;

    Viewer viewer;

    ViewingPlatform viewingPlatform;

    InnerModel model;

    public InnerView() {
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        viewer = new Viewer(canvas);
        viewingPlatform = new ViewingPlatform();
        viewer.setViewingPlatform(viewingPlatform);
        viewingPlatform.setNominalViewingTransform();
    }

    public Viewer getViewer() {
        return viewer;
    }

    public Canvas3D getCanvas3D() {
        return canvas;
    }

    public ViewingPlatform getViewingPlatform() {
        return viewingPlatform;
    }

    public void setModel(InnerModel model) {
        this.model = model;
        model.addViewingPlatform(viewingPlatform);
    }

    private void addOrbitBehaviour(Canvas3D canvas, ViewingPlatform viewingPlatform) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);
        viewingPlatform.setViewPlatformBehavior(orbit);
    }
}
