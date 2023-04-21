package org.jhotdraw.test.standard;

import org.jhotdraw.standard.DuplicateCommand;
import org.jhotdraw.test.JHDTestCase;

public class DuplicateCommandTest extends JHDTestCase {

    private DuplicateCommand duplicatecommand;

    /**
	 * Constructor DuplicateCommandTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public DuplicateCommandTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public DuplicateCommand createInstance() throws Exception {
        return new DuplicateCommand("TestDuplicate", getDrawingEditor());
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        duplicatecommand = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        duplicatecommand = null;
        super.tearDown();
    }

    public void testExecute() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
