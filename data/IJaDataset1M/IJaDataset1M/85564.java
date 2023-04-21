package test.view.swing;

import junit.framework.TestCase;
import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SButton;
import org.scopemvc.view.swing.SPanel;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.4 $ $Date: 2002/09/12 19:09:37 $
 */
public final class TestSButton extends TestCase {

    private SButton button;

    private SwingDummyController controller;

    /**
     * Constructor for the TestSButton object
     *
     * @param inName Name of the test
     */
    public TestSButton(String inName) {
        super(inName);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testNoControl() throws Exception {
        button.doClick();
        assertNull(controller.lastControl);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testControl() throws Exception {
        button.setControlID("Test");
        assertEquals("Test", button.getText());
        button.doClick();
        assertEquals(new Control("Test"), controller.lastControl);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConstructorControl() throws Exception {
        SButton b = new SButton("Test2");
        button.getParent().add(b);
        assertEquals("Test2", b.getText());
        b.doClick();
        assertEquals(new Control("Test2"), controller.lastControl);
    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        button = new SButton();
        SPanel p = new SPanel();
        p.add(button);
        controller = new SwingDummyController();
        controller.setView(p);
        controller.startup();
    }

    /**
     * The teardown method for JUnit
     */
    protected void tearDown() {
        controller.shutdown();
    }
}
