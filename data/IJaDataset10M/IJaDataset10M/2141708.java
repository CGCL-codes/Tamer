package de.fhkl.vr.particlespace.view;

import de.fhkl.vr.particlespace.model.IPlaceableSpaceObject;
import javax.media.opengl.GL;

/**
 *
 * @author stefan
 */
public abstract class AbstractParticleSpaceObjectView {

    protected IPlaceableSpaceObject obj = null;

    public AbstractParticleSpaceObjectView(IPlaceableSpaceObject obj) {
        this.obj = obj;
    }

    public abstract void draw(GL gl);
}
