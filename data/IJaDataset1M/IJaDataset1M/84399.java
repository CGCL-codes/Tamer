package org.ogre4j;

import org.xbig.base.*;

public interface ISimpleRenderable extends INativeObject, org.ogre4j.IMovableObject, org.ogre4j.IRenderable {

    /** **/
    public void setMaterial(String matName);

    /** **/
    public org.ogre4j.IMaterialPtr getMaterial();

    /** **/
    public void setRenderOperation(org.ogre4j.IRenderOperation rend);

    /** **/
    public void getRenderOperation(org.ogre4j.IRenderOperation op);

    /** **/
    public void setWorldTransform(org.ogre4j.IMatrix4 xform);

    /** **/
    public void getWorldTransforms(org.ogre4j.IMatrix4 xform);

    /** **/
    public void _notifyCurrentCamera(org.ogre4j.ICamera cam);

    /** **/
    public void setBoundingBox(org.ogre4j.IAxisAlignedBox box);

    /** **/
    public org.ogre4j.IAxisAlignedBox getBoundingBox();

    /** **/
    public void _updateRenderQueue(org.ogre4j.IRenderQueue queue);

    /** **/
    public void visitRenderables(org.ogre4j.IRenderable.IVisitor visitor, boolean debugRenderables);

    /** 
    Overridden from **/
    public String getMovableType();

    /** 
    **/
    public org.ogre4j.ILightList getLights();
}
