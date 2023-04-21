package com.riseOfPeople.handler;

import java.awt.Font;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.riseOfPeople.view.OpenGLPanel;

/**
 * @author Cor Attema
 * @date 03-02-2011
 */
public class RiseOfPeopleGLEventListener implements GLEventListener {

    private OpenGLPanel openGLPanel;

    /**
	 * function setOpenGLPanel
	 * @param openGLPanel, {@link OpenGLPanel}
	 */
    public void setOpenGLPanel(OpenGLPanel openGLPanel) {
        this.openGLPanel = openGLPanel;
    }

    /**
	 * function init
	 * inits the openGL view settings
	 */
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        GLU glu = new GLU();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glDisable(GL.GL_DEPTH_TEST);
        glu.gluOrtho2D(-1, 1, -1, 1);
        openGLPanel.setTextRenderer(new TextRenderer(new Font(Font.SERIF, 30, 30)));
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(500 * 10);
        byteBuffer.order(ByteOrder.nativeOrder());
        openGLPanel.setSelectionBuffer(byteBuffer.asIntBuffer());
        if (openGLPanel.getFPSAnimator() == null) {
            openGLPanel.setFPSanimator(new FPSAnimator(glAutoDrawable, 60, true));
            openGLPanel.getFPSAnimator().start();
        }
    }

    /**
	 * function display
	 * handles all display work of openGL
	 */
    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        openGLPanel.draw(glAutoDrawable.getGL().getGL2(), new GLU());
    }

    /**
	 * function dispose
	 * doesn't do a lot
	 */
    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
    }

    /**
	 * function reshape
	 * reshapes the view port of the canvas
	 */
    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
    }
}
