package org.ogre4j;

import org.xbig.base.*;

public interface IMovablePlane extends INativeObject, org.ogre4j.IPlane, org.ogre4j.IMovableObject {

    /** **/
    public void _notifyCurrentCamera(org.ogre4j.ICamera a1);

    /** **/
    public org.ogre4j.IAxisAlignedBox getBoundingBox();

    /** **/
    public float getBoundingRadius();

    /** **/
    public void _updateRenderQueue(org.ogre4j.IRenderQueue a1);

    /** **/
    public String getMovableType();

    /** **/
    public org.ogre4j.IPlane _getDerivedPlane();

    /** **/
    public void visitRenderables(org.ogre4j.IRenderable.IVisitor visitor, boolean debugRenderables);
}
