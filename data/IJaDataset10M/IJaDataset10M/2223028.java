package com.jogamp.opengl.test.junit.jogl.demos.gl2.gears;

import javax.media.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.test.junit.util.UITestCase;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * Test for bug 450, which causes the right part of the frame to be black
 * for all x >= height.
 * 
 * Draws the Gears demo in a window that's twice as wide than it is tall,
 * and checks to see if a particular pixel in the right half of the frame
 * is colored.
 *
 * @author Wade Walker (adapted from TestGearsGLJPanelAWT)
 */
public class TestGearsGLJPanelAWTBug450 extends UITestCase {

    static GLProfile glp;

    static int width, height;

    /** Set this if test fails. Needed because we can't throw an exception
     * all the way up the stack from where we test the pixel. */
    static boolean failed;

    @BeforeClass
    public static void initClass() {
        GLProfile.initSingleton(false);
        glp = GLProfile.getDefault();
        Assert.assertNotNull(glp);
        width = 512;
        height = 256;
    }

    @AfterClass
    public static void releaseClass() {
    }

    protected void runTestGL(GLCapabilities caps) throws AWTException, InterruptedException, InvocationTargetException {
        JFrame frame = new JFrame("Swing GLJPanel");
        Assert.assertNotNull(frame);
        GLJPanel glJPanel = new GLJPanel(caps);
        Assert.assertNotNull(glJPanel);
        glJPanel.addGLEventListener(new Gears() {

            @Override
            public void display(GLAutoDrawable drawable) {
                super.display(drawable);
                GL2 gl = GLUgl2.getCurrentGL2();
                ByteBuffer bytebuffer = ByteBuffer.allocateDirect(3);
                gl.glReadPixels(260, 10, 1, 1, GL2.GL_BGR, GL2.GL_UNSIGNED_BYTE, bytebuffer);
                byte byte0 = bytebuffer.get(0);
                byte byte1 = bytebuffer.get(1);
                byte byte2 = bytebuffer.get(2);
                if ((byte0 == 0) && (byte1 == 0) && (byte2 == 0)) failed = true;
            }
        });
        FPSAnimator animator = new FPSAnimator(glJPanel, 60);
        final JFrame _frame = frame;
        final GLJPanel _glJPanel = glJPanel;
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _frame.getContentPane().add(_glJPanel, BorderLayout.CENTER);
                _frame.setSize(width, height);
                _frame.setVisible(true);
            }
        });
        animator.setUpdateFPSFrames(1, null);
        animator.start();
        Assert.assertEquals(true, animator.isAnimating());
        while (animator.isAnimating() && animator.getTotalFPSDuration() < duration) {
            Thread.sleep(100);
        }
        Assert.assertNotNull(frame);
        Assert.assertNotNull(glJPanel);
        Assert.assertNotNull(animator);
        animator.stop();
        Assert.assertEquals(false, animator.isAnimating());
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                _frame.setVisible(false);
                _frame.getContentPane().remove(_glJPanel);
                _frame.remove(_glJPanel);
                _glJPanel.destroy();
                _frame.dispose();
            }
        });
        Assert.assertFalse(failed);
    }

    @Test
    public void test01() throws AWTException, InterruptedException, InvocationTargetException {
        GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
        runTestGL(caps);
    }

    static long duration = 500;

    public static void main(String args[]) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-time")) {
                i++;
                try {
                    duration = Integer.parseInt(args[i]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        org.junit.runner.JUnitCore.main(TestGearsGLJPanelAWTBug450.class.getName());
    }
}
