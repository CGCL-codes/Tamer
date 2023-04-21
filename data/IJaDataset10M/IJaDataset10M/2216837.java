package com.jme.renderer.lwjgl;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import com.jme.renderer.AbstractCamera;
import com.jme.scene.state.lwjgl.records.RendererRecord;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;

/**
 * <code>LWJGLCamera</code> defines a concrete implementation of a
 * <code>AbstractCamera</code> using the LWJGL library for view port setting.
 * Most functionality is provided by the <code>AbstractCamera</code> class with
 * this class handling the OpenGL specific calls to set the frustum and
 * viewport.
 * 
 * @author Mark Powell
 * @author Joshua Slack
 */
public class LWJGLCamera extends AbstractCamera {

    private static final long serialVersionUID = 1L;

    private static final FloatBuffer tmp_FloatBuffer = BufferUtils.createFloatBuffer(16);

    public LWJGLCamera() {
    }

    /**
     * Constructor instantiates a new <code>LWJGLCamera</code> object. The
     * width and height are provided, which corresponds to either the
     * width and height of the rendering window, or the resolution of the
     * fullscreen display.
     * @param width the width/resolution of the display.
     * @param height the height/resolution of the display.
     */
    public LWJGLCamera(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        update();
    }

    /**
     * Constructor instantiates a new <code>LWJGLCamera</code> object. The
     * width and height are provided, which corresponds to either the
     * width and height of the rendering window, or the resolution of the
     * fullscreen display.
     * @param width the width/resolution of the display.
     * @param height the height/resolution of the display.
     */
    public LWJGLCamera(int width, int height, boolean dataOnly) {
        super(dataOnly);
        this.width = width;
        this.height = height;
        setDataOnly(dataOnly);
        update();
    }

    /**
     * @return the width/resolution of the display.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the height/resolution of the display.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Resizes this camera's view with the given width and height. This is
     * similar to constructing a new camera, but reusing the same Object. This
     * method is called by an associated renderer to notify the camera of
     * changes in the display dimensions.
     * 
     * @param width
     *            the view width
     * @param height
     *            the view height
     */
    public void resize(final int width, final int height) {
        this.width = width;
        this.height = height;
        onViewPortChange();
    }

    /**
     * Resizes this camera's view with the given width and height. This is
     * similar to constructing a new camera, but reusing the same Object. This
     * method is called by an associated renderer to notify the camera of
     * changes in the display dimensions. A renderer can use the forceDirty
     * parameter for a newly associated camera to ensure that the settings for a
     * previously used camera will be part of the next rendering phase.
     * 
     * @param width
     *            the view width
     * @param height
     *            the view height
     * @param forceDirty
     *            <code>true</code> if camera settings should be treated as
     *            changed
     */
    void resize(final int width, final int height, final boolean forceDirty) {
        if (forceDirty) {
            frustumDirty = true;
            viewPortDirty = true;
            frameDirty = true;
        }
        resize(width, height);
    }

    private boolean frustumDirty;

    private boolean viewPortDirty;

    private boolean frameDirty;

    public void apply() {
        if (frustumDirty) {
            doFrustumChange();
            frustumDirty = false;
        }
        if (viewPortDirty) {
            doViewPortChange();
            viewPortDirty = false;
        }
        if (frameDirty) {
            doFrameChange();
            frameDirty = false;
        }
    }

    @Override
    public void onFrustumChange() {
        super.onFrustumChange();
        frustumDirty = true;
    }

    public void onViewPortChange() {
        viewPortDirty = true;
    }

    @Override
    public void onFrameChange() {
        super.onFrameChange();
        frameDirty = true;
    }

    /**
     * Sets the OpenGL frustum.
     * @see com.jme.renderer.Camera#onFrustumChange()
     */
    protected void doFrustumChange() {
        if (!isDataOnly()) {
            RendererRecord matRecord = (RendererRecord) DisplaySystem.getDisplaySystem().getCurrentContext().getRendererRecord();
            matRecord.switchMode(GL11.GL_PROJECTION);
            tmp_FloatBuffer.rewind();
            getProjectionMatrix().fillFloatBuffer(tmp_FloatBuffer);
            tmp_FloatBuffer.rewind();
            GL11.glLoadMatrix(tmp_FloatBuffer);
        }
    }

    /**
     * Sets OpenGL's viewport.
     * @see com.jme.renderer.Camera#onViewPortChange()
     */
    protected void doViewPortChange() {
        if (!isDataOnly()) {
            int x = (int) (viewPortLeft * width);
            int y = (int) (viewPortBottom * height);
            int w = (int) ((viewPortRight - viewPortLeft) * width);
            int h = (int) ((viewPortTop - viewPortBottom) * height);
            GL11.glViewport(x, y, w, h);
        }
    }

    /**
     * Uses GLU's lookat function to set the OpenGL frame.
     * @see com.jme.renderer.Camera#onFrameChange()
     */
    protected void doFrameChange() {
        if (!isDataOnly()) {
            RendererRecord matRecord = (RendererRecord) DisplaySystem.getDisplaySystem().getCurrentContext().getRendererRecord();
            matRecord.switchMode(GL11.GL_MODELVIEW);
            tmp_FloatBuffer.rewind();
            getModelViewMatrix().fillFloatBuffer(tmp_FloatBuffer);
            tmp_FloatBuffer.rewind();
            GL11.glLoadMatrix(tmp_FloatBuffer);
        }
    }
}
