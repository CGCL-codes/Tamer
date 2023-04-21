package org.sf.spring3d.scene.test.utils;

import javax.media.opengl.GL2;
import org.sf.spring3d.scene.node.impl.DefaultSceneNode;
import org.sf.spring3d.scene.view.engine.impl.DrawContext;

/**
 * Draws X, Y, and Z axises as lines.  Usually used for testing a view.
 * 
 * @since 0.0.1
 */
public class Axises extends DefaultSceneNode<DrawContext> {

    /**
    * The size of the axises. Defaults to 10 in each direction, not the total
    * size.
    */
    private int _size = 10;

    @SuppressWarnings("static-access")
    @Override
    public void draw(DrawContext drawContext) {
        GL2 gl = drawContext.getDefault();
        gl.glBegin(gl.GL_LINES);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3i(0, getSize(), 0);
        gl.glVertex3i(0, -getSize(), 0);
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3i(getSize(), 0, 0);
        gl.glVertex3i(-getSize(), 0, 0);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3i(0, 0, getSize());
        gl.glVertex3i(0, 0, -getSize());
        gl.glEnd();
    }

    /**
    * @return the size
    */
    public int getSize() {
        return _size;
    }

    /**
    * @param size the size to set
    */
    public void setSize(int size) {
        _size = size;
    }
}
