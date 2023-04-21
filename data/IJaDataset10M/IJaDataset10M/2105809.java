package com.jogamp.opengl.test.junit.jogl.swt;

import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLProfile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.Test;
import com.jogamp.opengl.test.junit.util.UITestCase;

/**
 * Tests that a basic SWT app can open without crashing under different GL profiles. Uses the SWT GL canvas.
 * @author Wade Walker
 */
public class TestSWT01GLn extends UITestCase {

    static int duration = 250;

    static final int iwidth = 640;

    static final int iheight = 480;

    Display display = null;

    Shell shell = null;

    Composite composite = null;

    @BeforeClass
    public static void startup() {
        GLProfile.initSingleton(true);
        System.out.println("GLProfile " + GLProfile.glAvailabilityToString());
    }

    @Before
    public void init() {
        display = new Display();
        Assert.assertNotNull(display);
        shell = new Shell(display);
        Assert.assertNotNull(shell);
        shell.setLayout(new FillLayout());
        composite = new Composite(shell, SWT.NONE);
        composite.setLayout(new FillLayout());
        Assert.assertNotNull(composite);
    }

    @After
    public void release() {
        Assert.assertNotNull(display);
        Assert.assertNotNull(shell);
        Assert.assertNotNull(composite);
        try {
            composite.dispose();
            shell.dispose();
            display.dispose();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Assume.assumeNoException(throwable);
        }
        display = null;
        shell = null;
        composite = null;
    }

    protected void runTestAGL(GLProfile glprofile) throws InterruptedException {
        GLData gldata = new GLData();
        gldata.doubleBuffer = true;
        final GLCanvas glcanvas = new GLCanvas(composite, SWT.NO_BACKGROUND, gldata);
        Assert.assertNotNull(glcanvas);
        glcanvas.setCurrent();
        final GLContext glcontext = GLDrawableFactory.getFactory(glprofile).createExternalGLContext();
        Assert.assertNotNull(glcontext);
        glcanvas.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event event) {
                Rectangle rectangle = glcanvas.getClientArea();
                glcanvas.setCurrent();
                glcontext.makeCurrent();
                GL2 gl = glcontext.getGL().getGL2();
                OneTriangle.setup(gl, rectangle);
                glcontext.release();
                System.err.println("resize");
            }
        });
        glcanvas.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent paintevent) {
                Rectangle rectangle = glcanvas.getClientArea();
                glcanvas.setCurrent();
                glcontext.makeCurrent();
                GL2 gl = glcontext.getGL().getGL2();
                OneTriangle.render(gl, rectangle);
                glcanvas.swapBuffers();
                glcontext.release();
                System.err.println("paint");
            }
        });
        shell.setText(getClass().getName());
        shell.setSize(640, 480);
        shell.open();
        long lStartTime = System.currentTimeMillis();
        long lEndTime = lStartTime + duration;
        try {
            while ((System.currentTimeMillis() < lEndTime) && !glcanvas.isDisposed()) {
                if (!display.readAndDispatch()) {
                    Thread.sleep(10);
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            Assume.assumeNoException(throwable);
        }
        glcanvas.dispose();
    }

    @Test
    public void testA01GLDefault() throws InterruptedException {
        GLProfile glprofile = GLProfile.getDefault();
        System.out.println("GLProfile Default: " + glprofile);
        runTestAGL(glprofile);
    }

    @Test
    public void test02GL2() throws InterruptedException {
        GLProfile glprofile = GLProfile.get(GLProfile.GL2);
        System.out.println("GLProfile GL2: " + glprofile);
        runTestAGL(glprofile);
    }

    static int atoi(String a) {
        int i = 0;
        try {
            i = Integer.parseInt(a);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return i;
    }

    public static void main(String args[]) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-time")) {
                duration = atoi(args[++i]);
            }
        }
        System.out.println("durationPerTest: " + duration);
        org.junit.runner.JUnitCore.main(TestSWT01GLn.class.getName());
    }
}
