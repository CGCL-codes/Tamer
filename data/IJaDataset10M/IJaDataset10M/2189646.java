package com.jogamp.opengl.test.junit.jogl.awt;

import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.test.junit.util.UITestCase;
import com.jogamp.opengl.test.junit.jogl.demos.gl2.gears.Gears;
import com.jogamp.opengl.test.junit.util.MiscUtils;
import java.awt.Frame;
import java.awt.Label;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Test;

public class TestAWT03GLCanvasRecreate01 extends UITestCase {

    static long durationPerTest = 1000;

    Frame frame1 = null;

    Frame frame2 = null;

    GLCanvas glCanvas = null;

    Label label = null;

    Animator animator = null;

    @BeforeClass
    public static void startup() {
        GLProfile.initSingleton(true);
        System.out.println("GLProfile " + GLProfile.glAvailabilityToString());
    }

    @Before
    public void init() {
        glCanvas = new GLCanvas();
        Assert.assertNotNull(glCanvas);
        glCanvas.addGLEventListener(new Gears());
        animator = new Animator(glCanvas);
        animator.start();
        label = new Label("No GLCanvas");
        frame1 = new Frame("Frame 1");
        Assert.assertNotNull(frame1);
        frame1.add(label);
        frame1.setSize(512, 512);
        frame1.setLocation(0, 0);
        frame2 = new Frame("Frame 2");
        Assert.assertNotNull(frame2);
        frame2.add(label);
        frame2.setSize(512, 512);
        frame2.setLocation(512, 0);
    }

    @After
    public void release() {
        Assert.assertNotNull(frame1);
        Assert.assertNotNull(frame2);
        Assert.assertNotNull(glCanvas);
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    glCanvas.destroy();
                    frame1.dispose();
                    frame2.dispose();
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
            Assume.assumeNoException(t);
        }
        frame1 = null;
        frame2 = null;
        glCanvas = null;
        animator.stop();
        animator = null;
    }

    private void addCanvas(final Frame frame) {
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    frame.remove(label);
                    frame.add(glCanvas);
                    frame.validate();
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
            Assume.assumeNoException(t);
        }
    }

    private void removeCanvas(final Frame frame) {
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    frame.remove(glCanvas);
                    frame.add(label);
                    frame.validate();
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
            Assume.assumeNoException(t);
        }
    }

    private void setVisible(final Frame frame, final boolean v) {
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    frame.setVisible(v);
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
            Assume.assumeNoException(t);
        }
    }

    @Test
    public void testAddRemove3Times() throws InterruptedException {
        setVisible(frame1, true);
        setVisible(frame2, true);
        addCanvas(frame1);
        Thread.sleep(durationPerTest / 4);
        removeCanvas(frame1);
        addCanvas(frame2);
        Thread.sleep(durationPerTest / 4);
        removeCanvas(frame2);
        addCanvas(frame1);
        Thread.sleep(durationPerTest / 4);
        removeCanvas(frame1);
        addCanvas(frame2);
        Thread.sleep(durationPerTest / 4);
    }

    public static void main(String args[]) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-time")) {
                durationPerTest = MiscUtils.atoi(args[++i], (int) durationPerTest);
            }
        }
        org.junit.runner.JUnitCore.main(TestAWT03GLCanvasRecreate01.class.getName());
    }
}
