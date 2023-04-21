package org.matsim.utils.vis.otfivs.opengl.drawer;

import javax.media.opengl.GL;
import org.matsim.utils.vis.otfivs.caching.SceneGraph;
import org.matsim.utils.vis.otfivs.data.OTFData;

public abstract class OTFGLDrawableImpl implements OTFGLDrawable, OTFData.Receiver {

    boolean isValid = true;

    public static GL gl;

    public final void draw() {
        onDraw(gl);
        isValid = true;
    }

    public void invalidate(SceneGraph graph) {
        isValid = false;
        graph.addItem(this);
    }
}
