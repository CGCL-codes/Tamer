package com.google.devtools.depan.eclipse.visualization.ogl;

import java.awt.Shape;
import javax.media.opengl.GL;

/**
 * Abstract class for any shape, that can be rendered on an openGL canvas.
 *
 * @author Yohann Coppel
 *
 */
public abstract class GLEntity implements Shape {

    protected float translateX = 0f, translateY = 0f, translateZ = 0f;

    protected float scaleX = 1f, scaleY = 1f, scaleZ = 1f;

    public void setTranslation(float x, float y, float z) {
        this.translateX = x;
        this.translateY = y;
        this.translateZ = z;
    }

    public void setScale(float x, float y, float z) {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    public abstract void draw(GL gl);

    public abstract void fill(GL gl);
}
