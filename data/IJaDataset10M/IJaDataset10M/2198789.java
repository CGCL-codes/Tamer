package saf.v3d.render;

import javax.media.opengl.GL;
import javax.vecmath.Matrix4f;
import saf.v3d.scene.Appearance;

/**
 * Struct for keeping the current render state.
 * 
 * @author Nick Collier
 */
public class RenderState {

    public int vboIndex, textureObject;

    public int width, height, canvasId;

    public Matrix4f matrix;

    public Appearance appearance;

    public BorderRenderer border;

    public int lineWidth = 1;

    public int PolyMode = GL.GL_FILL;

    /**
   * Resets for the next iteration through the display loop.
   */
    public void reset() {
        vboIndex = 0;
        textureObject = 0;
    }
}
