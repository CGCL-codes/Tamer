package org.fenggui.binding.render;

import java.nio.ByteBuffer;

/**
 * Contains a sub set of the OpenGL functions which are required to
 * set up the GUI. This class is an ad hoc class and conceptual not
 * realy convincing. We should find a better way to provide these
 * few OpenGL methods.... 
 * 
 * @author Johannes Schaback ($Author: marcmenghin $)
 * @author Graham Briggs ($Author: marcmenghin $)
 */
public interface IOpenGL {

    public enum Attribute {

        CURRENT_COLOR, LINE_WIDTH, POINT_SIZE
    }

    public boolean isOpenGLThread();

    public void setModelMatrixMode();

    public void setProjectionMatrixMode();

    public void pushMatrix();

    public void popMatrix();

    public void loadIdentity();

    public void pushAllAttribs();

    public void popAllAttribs();

    public int[] getInt(Attribute attrib);

    public float[] getFloat(Attribute attrib);

    public boolean[] getBoolean(Attribute attrib);

    public double[] getDouble(Attribute attrib);

    public String getString(Attribute attrib);

    public void enable(Attribute attrib);

    public void disable(Attribute attrib);

    public void enableTexture2D(boolean b);

    public void setTexEnvModeDecal();

    public void setTexEnvModeModulate();

    public void setViewPort(int x, int y, int width, int height);

    public void setOrtho2D(int left, int right, int bottom, int top);

    public void setDepthFunctionToLEqual();

    public void translateZ(float z);

    public void translateXY(int x, int y);

    public void rotate(float angle);

    public void rotate(float angle, int x, int y, int z);

    public void setScissor(int x, int width, int y, int height);

    public void activateTexture(int i);

    public int genLists(int range);

    public void startList(int list);

    public void endList();

    public void callList(int list);

    public void end();

    public void startQuads();

    public void startLines();

    public void startLineStrip();

    public void startLineLoop();

    public void startTriangles();

    public void startTriangleStrip();

    public void startTriangleFan();

    public void startQuadStrip();

    public void startPoints();

    public void vertex(float x, float y);

    public void rect(float x1, float y1, float x2, float y2);

    public void texCoord(float x, float y);

    public void color(float red, float green, float blue, float alpha);

    public void scale(float scaleX, float scaleY);

    public void enableLighting(boolean b);

    public void setupStateVariables(boolean depthTestEnabled);

    public void enableAlternateBlending(boolean b);

    public void lineWidth(float width);

    public void pointSize(float size);

    public void enableStipple();

    public void disableStipple();

    public void lineStipple(int stretch, short pattern);

    public void enableAlpha(boolean state);

    public void readPixels(int x, int y, int width, int height, ByteBuffer bgr);
}
