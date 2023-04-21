package net.dzzd.extension.jogl;

import net.dzzd.access.*;
import net.dzzd.core.*;
import net.dzzd.DzzD;
import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

public final class Render2DJOGL extends Render2D {

    private GLContext context;

    private GLCanvas canvas;

    private GL gl;

    private GLU glu;

    private IScene2D scene;

    public Canvas getCanvas() {
        return this.canvas;
    }

    public Render2DJOGL() throws Throwable {
        super();
        GLCapabilities glCaps = new GLCapabilities();
        glCaps.setRedBits(8);
        glCaps.setBlueBits(8);
        glCaps.setGreenBits(8);
        glCaps.setAlphaBits(8);
        glCaps.setDoubleBuffered(true);
        glCaps.setHardwareAccelerated(true);
        this.canvas = new GLCanvas(glCaps);
        this.canvas.setAutoSwapBufferMode(false);
        this.glu = new GLU();
        this.context = this.canvas.getContext();
        this.gl = this.context.getGL();
    }

    public void setSize(int viewPixelWidth, int viewPixelHeight) {
        this.canvas.setSize(viewPixelWidth, viewPixelHeight);
    }

    /**
	 * Called when user request Scene2D to be render
	 * <br>
	 * must draw all Scene2DObject within Scene2D as all Shape2D, and other 2D visible object
	 * <br>
	 * build revision between jogl & Scene2D object should be checked to see if recompilation is needed.
	 * <br>
	 * refer to Render3D & Render3DJOGL for compilation & build revision
	 */
    public void renderScene2D(IScene2D scene) {
        this.scene = scene;
        this.makeContentCurrent();
        this.gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glVertex3f(0, 0, 0.0f);
        gl.glVertex3f(1, 0, 0.0f);
        gl.glVertex3f(0, 1, 0.0f);
        gl.glEnd();
        {
            gl.glColor3f(0.0f, 1.0f, 0.0f);
        }
        this.context.release();
        if (this.isScreenUpdateEnabled) this.canvas.swapBuffers();
    }

    private void makeContentCurrent() {
        try {
            while (context.makeCurrent() == GLContext.CONTEXT_NOT_CURRENT) {
                System.out.println("Context not yet current...");
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void init(GLAutoDrawable drawable) {
        this.gl = drawable.getGL();
    }

    public void display(GLAutoDrawable drawable) {
        if (this.scene == null) return;
        super.renderScene2D(this.scene);
    }

    public void reshape(GLAutoDrawable drawable, int i, int i1, int i2, int i3) {
        this.gl = drawable.getGL();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean b, boolean b1) {
        this.gl = drawable.getGL();
    }
}
