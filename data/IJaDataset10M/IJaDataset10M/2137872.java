package test.view.awt;

import java.awt.*;
import junit.framework.TestCase;
import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.core.*;
import org.scopemvc.view.awt.*;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:59 $
 */
public final class TestAWTUtilities extends TestCase {

    private Dimension screenDimension;

    private Frame dummyFrame;

    /**
     * Constructor for the TestAWTUtilities object
     *
     * @param inName Name of the test
     */
    public TestAWTUtilities(String inName) {
        super(inName);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFindControlIssuer() throws Exception {
        assertNull(AWTUtilities.findControlIssuer(null));
        org.scopemvc.view.swing.SPanel v = new org.scopemvc.view.swing.SPanel();
        assertNull(AWTUtilities.findControlIssuer(v));
        Controller c = new DummyController();
        v.setController(c);
        assertSame(v, AWTUtilities.findControlIssuer(v));
        org.scopemvc.view.swing.SPanel v2 = new org.scopemvc.view.swing.SPanel();
        v.add(v2);
        assertSame(v, AWTUtilities.findControlIssuer(v2));
        v.setController(null);
        assertNull(AWTUtilities.findControlIssuer(v));
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCentreOnScreen() throws Exception {
        Window w = new Window(dummyFrame);
        w.setBounds(0, 0, 100, 50);
        AWTUtilities.centreOnScreen(w);
        Rectangle r = w.getBounds();
        assertTrue(Math.abs(screenDimension.width - (r.x + r.width) - r.x) <= 1);
        assertTrue(Math.abs(screenDimension.height - (r.y + r.height) - r.y) <= 1);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCentreOnWindow() throws Exception {
        Window w1 = new Window(dummyFrame);
        w1.setBounds(10, 10, 100, 60);
        Window w2 = new Window(dummyFrame);
        w2.setBounds(10, 10, 50, 30);
        AWTUtilities.centreOnWindow(w1, w2);
        Rectangle r1 = w1.getBounds();
        Rectangle r2 = w2.getBounds();
        assertTrue(r1.x == 10);
        assertTrue(r1.y == 10);
        assertTrue(r1.width == 100);
        assertTrue(r1.height == 60);
        assertTrue(r2.x == 25);
        assertTrue(r2.y == 15);
        assertTrue(r2.width == 50);
        assertTrue(r2.height == 30);
    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        dummyFrame = new Frame("TestAWTUtilities");
        dummyFrame.setVisible(true);
        screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * The teardown method for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    protected void tearDown() throws Exception {
        dummyFrame.setVisible(false);
    }

    static class DummyController extends BasicController {
    }
}
