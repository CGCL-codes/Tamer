package TestCases;

import Tracer.RTColor;
import XMLScene.RTSurface;
import junit.framework.TestCase;

/**
 * @author forrestzhao
 * 
 */
public class RTSurfaceTest extends TestCase {

    private RTSurface colorSurface = null;

    private RTSurface imageSurface = null;

    /**
	 * @param name
	 */
    public RTSurfaceTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        colorSurface = new RTSurface(new RTColor(0.0f, 0.5f, 1.0f));
        colorSurface.setDiffuse(0.4f);
        colorSurface.setReflect(0.6f);
        imageSurface = new RTSurface("earthmap.ppm");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test method for setDiffuse Test it works well after performing setDiffuse
	 */
    public final void testSetDiffuse() {
        imageSurface.setDiffuse(0.3f);
        assertEquals("Diffuse error", 0.3f, imageSurface.getDiffuse());
    }

    /**
	 * Test method for setReflect Test it works well after performing setReflect
	 */
    public final void testSetReflect() {
        imageSurface.setReflect(0.7f);
        assertEquals("Reflect error", 0.7f, imageSurface.getReflect());
    }

    /**
	 * Test method for getDiffuse() Test it works well after performing
	 * getDiffuse
	 */
    public final void testGetDiffuse() {
        assertEquals("Diffuse error", 0.4f, colorSurface.getDiffuse());
    }

    /**
	 * Test method for getReflect Test it works well after performing getReflect
	 */
    public final void testGetReflect() {
        assertEquals("Reflect error", 0.6f, colorSurface.getReflect());
    }

    /**
	 * Test method for getColor Test it works well after performing getColor
	 */
    public final void testGetColor() {
        assertTrue("Color error", colorSurface.getPigment().getColor().isEqual(new RTColor(0.0f, 0.5f, 1.0f)));
    }

    /**
	 * Test method for getImagePath Test it works well after performing
	 * getImagePath
	 */
    public final void testGetImagePath() {
        assertEquals("Image path error", 640, imageSurface.getPigment().getBitmap().width());
    }
}
