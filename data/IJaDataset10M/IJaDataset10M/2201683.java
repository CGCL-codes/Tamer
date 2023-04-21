package net.frede.gui.gui.toolbar;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.frede.gui.LoaderTests;
import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class ToolBarTests extends LoaderTests {

    /**
	 * the logger that will log any abnormal outputs out of this instance.
	 */
    static Logger logger = Logger.getLogger(ToolBarTests.class);

    /**
	 * Constructs a <code>ToolBarTests</code> with the specified name.
	 * 
	 * @param name
	 *            Test name.
	 */
    public ToolBarTests(String name) {
        super(name, "toolbar-test.xml");
    }

    /**
	 * Main. used to run tests in console mode
	 * 
	 * @param args !
	 */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
	 * Assembles and returns a test suite for all the test methods of this test
	 * case.
	 * 
	 * @return A non-null <code>ToolBarTests</code> instance.
	 */
    public static Test suite() {
        return new TestSuite(ToolBarTests.class);
    }

    /**
	 * Sets up the test fixture. Called before every test case method.
	 */
    protected void setUp() {
    }

    /**
	 * Tears down the test fixture. Called after every test case method.
	 */
    protected void tearDown() {
    }
}
