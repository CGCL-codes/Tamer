package org.xith3d.scenegraph;

import org.xith3d.render.CanvasPeer;

/**
 * Depth component.
 * 
 * @author David Yazel
 */
public abstract class DepthComponent extends NodeComponent {

    protected int width;

    protected int height;

    public final int getWidth() {
        return (width);
    }

    public final int getHeight() {
        return (height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void duplicateNodeComponent(NodeComponent original, boolean forceDuplicate) {
        super.duplicateNodeComponent(original, forceDuplicate);
        DepthComponent o = (DepthComponent) original;
        this.width = o.getWidth();
        this.height = o.getHeight();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void freeOpenGLResources(CanvasPeer canvasPeer) {
    }

    DepthComponent() {
        super(false);
    }
}
