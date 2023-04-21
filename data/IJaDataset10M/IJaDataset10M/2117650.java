package org.ogre4j;

import org.xbig.base.*;

public interface IViewport extends INativeObject, org.ogre4j.IRenderSysAllocatedObject {

    /** 
    Notifies the viewport of a possible change in dimensions. **/
    public void _updateDimensions();

    /** 
    Instructs the viewport to updates its contents. **/
    public void update();

    /** 
    Retrieves a pointer to the render target for this viewport. **/
    public org.ogre4j.IRenderTarget getTarget();

    /** 
    Retrieves a pointer to the camera for this viewport. **/
    public org.ogre4j.ICamera getCamera();

    /** 
    Sets the camera to use for rendering to this viewport. **/
    public void setCamera(org.ogre4j.ICamera cam);

    /** 
    Gets the Z-Order of this viewport. **/
    public int getZOrder();

    /** 
    Gets one of the relative dimensions of the viewport, a value between 0.0 and 1.0. **/
    public float getLeft();

    /** 
    Gets one of the relative dimensions of the viewport, a value between 0.0 and 1.0. **/
    public float getTop();

    /** 
    Gets one of the relative dimensions of the viewport, a value between 0.0 and 1.0. **/
    public float getWidth();

    /** 
    Gets one of the relative dimensions of the viewport, a value between 0.0 and 1.0. **/
    public float getHeight();

    /** 
    Gets one of the actual dimensions of the viewport, a value in pixels. **/
    public int getActualLeft();

    /** 
    Gets one of the actual dimensions of the viewport, a value in pixels. **/
    public int getActualTop();

    /** 
    Gets one of the actual dimensions of the viewport, a value in pixels. **/
    public int getActualWidth();

    /** 
    Gets one of the actual dimensions of the viewport, a value in pixels. **/
    public int getActualHeight();

    /** 
    Sets the dimensions (after creation). **/
    public void setDimensions(float left, float top, float width, float height);

    /** 
    Sets the initial background colour of the viewport (before rendering). **/
    public void setBackgroundColour(org.ogre4j.IColourValue colour);

    /** 
    Gets the background colour. **/
    public org.ogre4j.IColourValue getBackgroundColour();

    /** 
    Determines whether to clear the viewport before rendering. **/
    public void setClearEveryFrame(boolean clear, long buffers);

    /** 
    Determines if the viewport is cleared before every frame. **/
    public boolean getClearEveryFrame();

    /** 
    Gets which buffers are to be cleared each frame. **/
    public long getClearBuffers();

    /** 
    Set the material scheme which the viewport should use. **/
    public void setMaterialScheme(String schemeName);

    /** 
    Get the material scheme which the viewport should use. **/
    public String getMaterialScheme();

    /** 
    Access to actual dimensions (based on target size). **/
    public void getActualDimensions(IntegerPointer left, IntegerPointer top, IntegerPointer width, IntegerPointer height);

    /** **/
    public boolean _isUpdated();

    /** **/
    public void _clearUpdatedFlag();

    /** 
    Gets the number of rendered faces in the last update. **/
    public long _getNumRenderedFaces();

    /** 
    Gets the number of rendered batches in the last update. **/
    public long _getNumRenderedBatches();

    /** 
    Tells this viewport whether it should display  objects. **/
    public void setOverlaysEnabled(boolean enabled);

    /** 
    Returns whether or not  objects (created in the ) are displayed in this viewport. **/
    public boolean getOverlaysEnabled();

    /** 
    Tells this viewport whether it should display skies. **/
    public void setSkiesEnabled(boolean enabled);

    /** 
    Returns whether or not skies (created in the ) are displayed in this viewport. **/
    public boolean getSkiesEnabled();

    /** 
    Tells this viewport whether it should display shadows. **/
    public void setShadowsEnabled(boolean enabled);

    /** 
    Returns whether or not shadows (defined in the ) are displayed in this viewport. **/
    public boolean getShadowsEnabled();

    /** 
    Sets a per-viewport visibility mask. **/
    public void setVisibilityMask(long mask);

    /** 
    Gets a per-viewport visibility mask. **/
    public long getVisibilityMask();

    /** 
    Sets the use of a custom  for rendering this target. **/
    public void setRenderQueueInvocationSequenceName(String sequenceName);

    /** 
    Gets the name of the render queue invocation sequence for this target. **/
    public String getRenderQueueInvocationSequenceName();

    /** **/
    public org.ogre4j.IRenderQueueInvocationSequence _getRenderQueueInvocationSequence();
}
