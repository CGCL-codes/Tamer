package test.core;

import junit.framework.TestCase;
import org.scopemvc.core.ControlException;
import org.scopemvc.core.Selector;
import org.scopemvc.util.UIStrings;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/09/12 19:09:34 $
 */
public final class TestExceptions extends TestCase {

    private static final String MESSAGE1 = "MESSAGE1";

    private static final String MESSAGE2 = "MESSAGE2";

    private static final String CONTROL_ID = "TEST_CONTROL_ID";

    private Object model;

    private Selector selector;

    /**
     * Constructor for the TestExceptions object
     *
     * @param inName Name of the test
     */
    public TestExceptions(String inName) {
        super(inName);
    }

    /**
     * A unit test for JUnit
     */
    public void testControlException1() {
        ControlException cex = new ControlException(MESSAGE1);
        cex.setSourceControlID(CONTROL_ID);
        assertSame(MESSAGE1, cex.getMessage());
        assertEquals("Test Control", cex.getLocalizedSourceControlName());
        assertEquals("Test Message 1", cex.getLocalizedMessage());
    }

    /**
     * A unit test for JUnit
     */
    public void testControlException2() {
        ControlException cex = new ControlException(MESSAGE2, new Object[] { "Insert" });
        cex.setSourceControlID(CONTROL_ID);
        assertSame(MESSAGE2, cex.getMessage());
        assertEquals("Test Control", cex.getLocalizedSourceControlName());
        assertEquals("Test Message 2 Insert", cex.getLocalizedMessage());
    }

    /**
     * The JUnit setup method
     */
    protected void setUp() {
        model = new Object();
        selector = Selector.fromInt(0);
        UIStrings.setPropertiesName("test.util.DummyUIResources");
    }
}
