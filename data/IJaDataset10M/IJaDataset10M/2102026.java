package net.frede.gui.gui.explorer.table;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.frede.gui.action.Actions;
import net.frede.gui.gui.GuiTestBase;
import net.frede.gui.gui.explorer.TreeNodeDummy;
import net.frede.gui.gui.explorer.table.TableNode;
import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class TableNodeTests extends GuiTestBase {

    /**
	 * the logger that will log any abnormal outputs out of this instance.
	 */
    static Logger logger = Logger.getLogger(TableNodeTests.class);

    /**
	 * Constructs a <code>TableNodeTests</code> with the specified name.
	 * 
	 * @param name
	 *            Test name.
	 */
    public TableNodeTests(String name) {
        super(name, "table-node-test.xml");
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
	 * @return A non-null <code>TableNodeTests</code> instance.
	 */
    public static Test suite() {
        return new TestSuite(TableNodeTests.class);
    }

    /**
	 * DOCUMENT ME!
	 */
    public void testRoot() {
        TreeNodeDummy tree = (TreeNodeDummy) getElement("tree-node-dummy.xml");
        Actions a = (Actions) getElement("actions-filter-test.xml");
        TableNode tn = (TableNode) element;
        System.out.println(tree.toXML());
        tn.setActions(a);
        tn.setRoot(tree);
        see(tn.getDisplay());
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
