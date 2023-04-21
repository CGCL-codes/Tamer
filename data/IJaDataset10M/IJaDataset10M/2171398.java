package com.jogamp.opengl.test.junit.jogl.drawable;

import com.jogamp.opengl.test.junit.util.UITestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import javax.media.opengl.*;
import com.jogamp.newt.*;
import java.io.IOException;

public class TestDrawable01NEWT extends UITestCase {

    static GLProfile glp;

    static GLDrawableFactory factory;

    static int width, height;

    GLCapabilities caps;

    Window window;

    GLDrawable drawable;

    GLContext context;

    @BeforeClass
    public static void initClass() {
        GLProfile.initSingleton(true);
        glp = GLProfile.getDefault();
        Assert.assertNotNull(glp);
        factory = GLDrawableFactory.getFactory(glp);
        Assert.assertNotNull(factory);
        width = 640;
        height = 480;
    }

    @AfterClass
    public static void releaseClass() {
        Assert.assertNotNull(factory);
        factory = null;
    }

    @Before
    public void initTest() {
        caps = new GLCapabilities(glp);
        Assert.assertNotNull(caps);
    }

    void createWindow(boolean onscreen, boolean pbuffer, boolean undecorated) {
        caps.setOnscreen(onscreen);
        caps.setPBuffer(!onscreen && pbuffer);
        caps.setDoubleBuffered(onscreen);
        Display display = NewtFactory.createDisplay(null);
        Assert.assertNotNull(display);
        Screen screen = NewtFactory.createScreen(display, 0);
        Assert.assertNotNull(screen);
        window = NewtFactory.createWindow(screen, caps);
        Assert.assertNotNull(window);
        window.setUndecorated(onscreen && undecorated);
        window.setSize(width, height);
        window.setVisible(true);
        GLCapabilities glCaps = (GLCapabilities) window.getGraphicsConfiguration().getNativeGraphicsConfiguration().getChosenCapabilities();
        Assert.assertNotNull(glCaps);
        Assert.assertTrue(glCaps.getGreenBits() > 5);
        Assert.assertTrue(glCaps.getBlueBits() > 5);
        Assert.assertTrue(glCaps.getRedBits() > 5);
        Assert.assertEquals(glCaps.isOnscreen(), onscreen);
        Assert.assertTrue(onscreen || !pbuffer || glCaps.isPBuffer());
        Assert.assertEquals(glCaps.getDoubleBuffered(), onscreen);
        Assert.assertTrue(glCaps.getDepthBits() > 4);
        drawable = factory.createGLDrawable(window);
        Assert.assertNotNull(drawable);
        drawable.setRealized(true);
        Assert.assertEquals(window, drawable.getNativeSurface());
        context = drawable.createContext(null);
        Assert.assertNotNull(context);
        int res = context.makeCurrent();
        Assert.assertTrue(GLContext.CONTEXT_CURRENT_NEW == res || GLContext.CONTEXT_CURRENT == res);
        drawable.swapBuffers();
        context.release();
    }

    void destroyWindow() {
        Assert.assertNotNull(context);
        context.destroy();
        Assert.assertNotNull(drawable);
        drawable.setRealized(false);
        Assert.assertNotNull(window);
        window.destroy();
        drawable = null;
        context = null;
        window = null;
    }

    @Test
    public void testOnScreenDecorated() throws InterruptedException {
        createWindow(true, false, false);
        Thread.sleep(1000);
        destroyWindow();
    }

    @Test
    public void testOnScreenUndecorated() throws InterruptedException {
        createWindow(true, false, true);
        Thread.sleep(1000);
        destroyWindow();
    }

    public static void main(String args[]) throws IOException {
        String tstname = TestDrawable01NEWT.class.getName();
        org.apache.tools.ant.taskdefs.optional.junit.JUnitTestRunner.main(new String[] { tstname, "filtertrace=true", "haltOnError=false", "haltOnFailure=false", "showoutput=true", "outputtoformatters=true", "logfailedtests=true", "logtestlistenerevents=true", "formatter=org.apache.tools.ant.taskdefs.optional.junit.PlainJUnitResultFormatter", "formatter=org.apache.tools.ant.taskdefs.optional.junit.XMLJUnitResultFormatter,TEST-" + tstname + ".xml" });
    }
}
