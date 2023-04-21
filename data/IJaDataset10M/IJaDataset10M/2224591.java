package net.frede.gui.gui.explorer;

import junit.framework.Test;
import junit.framework.TestCase;
import net.frede.gui.gui.explorer.table.TestSuitetable;
import net.frede.gui.gui.explorer.tree.TestSuitetree;

public class TestSuiteexplorer extends TestCase {

    /**
	 * Constructs a <code>TestSuiteexplorer</code> with the specified name.
	 * 
	 * @param name
	 *            Test name.
	 */
    public TestSuiteexplorer(String name) {
        super(name);
    }

    /**
	 * Main.
	 * 
	 * @param args !
	 */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
	 * Assembles and returns a test suite containing all JDepend tests.
	 * 
	 * @return A non-null <code>TestSuiteexplorer</code> instance.
	 */
    public static Test suite() {
        junit.framework.TestSuite suite = new junit.framework.TestSuite("net.frede.gui.gui.explorer Test Suite");
        suite.addTest(TestSuitetable.suite());
        suite.addTest(TestSuitetree.suite());
        suite.addTest(ExplorerTests.suite());
        return suite;
    }
}
