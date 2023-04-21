package org.xith3d.render;

import org.xith3d.render.Canvas3D;

/**
 * A Canvas3DWrapper is a class, that wrapps a Canvas3D.
 * 
 * @see Canvas3DPanel
 * @see Canvas3DJPanel
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface Canvas3DWrapper {

    /**
     * @return the wrapped Canvas3D
     */
    public Canvas3D getCanvas();
}
