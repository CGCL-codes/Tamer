package test.view.swing;

import javax.swing.*;
import junit.framework.TestCase;
import org.scopemvc.view.swing.*;

/**
 * <P>
 *
 * Tests ValidationHelper via STextField. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:14:01 $
 * @created 05 September 2002
 */
public final class TestValidationHelper extends TestCase {

    private STextField parent;

    private STextField ref;

    private JFrame f;

    /**
     * Constructor for the TestValidationHelper object
     *
     * @param inName Name of the test
     */
    public TestValidationHelper(String inName) {
        super(inName);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConstructor() throws Exception {
        JToolTip t = parent.createToolTip();
        assertEquals("original", parent.getToolTipText());
        assertEquals(ref.createToolTip().getBackground(), t.getBackground());
        assertEquals(ref.getBackground(), parent.getBackground());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSuccess() throws Exception {
        parent.validationSuccess();
        JToolTip t = parent.createToolTip();
        assertEquals("original", parent.getToolTipText());
        assertEquals(ref.createToolTip().getBackground(), t.getBackground());
        assertEquals(ref.getBackground(), parent.getBackground());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFailed() throws Exception {
        parent.validationFailed(new Exception("test"));
        JToolTip t = parent.createToolTip();
        assertEquals("test", parent.getToolTipText());
        assertEquals(ValidationHelper.DEFAULT_VALIDATION_FAILED_COLOR, t.getBackground());
        assertEquals(ValidationHelper.DEFAULT_VALIDATION_FAILED_COLOR, parent.getBackground());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFailedSuccess() throws Exception {
        parent.validationFailed(new Exception("test1"));
        parent.validationSuccess();
        JToolTip t = parent.createToolTip();
        assertEquals("original", parent.getToolTipText());
        assertEquals(ref.createToolTip().getBackground(), t.getBackground());
        assertEquals(ref.getBackground(), parent.getBackground());
    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        parent = new STextField();
        parent.setToolTipText("original");
        ref = new STextField();
        ref.setToolTipText("reference");
        f = new JFrame();
        f.getContentPane().add(parent);
        f.getContentPane().add(ref);
        f.pack();
        f.setVisible(true);
    }

    /**
     * The teardown method for JUnit
     */
    protected void tearDown() {
        f.setVisible(false);
        f.dispose();
    }
}
