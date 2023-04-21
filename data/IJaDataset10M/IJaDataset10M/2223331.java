package org.jhotdraw.test.contrib;

import java.awt.Point;
import junit.framework.TestCase;
import org.jhotdraw.contrib.GraphicalCompositeFigure;
import org.jhotdraw.contrib.Layoutable;
import org.jhotdraw.contrib.SimpleLayouter;
import org.jhotdraw.figures.RectangleFigure;

public class SimpleLayouterTest extends TestCase {

    private SimpleLayouter simplelayouter;

    /**
	 * Constructor SimpleLayouterTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public SimpleLayouterTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public SimpleLayouter createInstance() throws Exception {
        Layoutable fig = new GraphicalCompositeFigure(new RectangleFigure(new Point(10, 10), new Point(100, 100)));
        return new SimpleLayouter(fig);
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        simplelayouter = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        simplelayouter = null;
        super.tearDown();
    }

    public void testSetGetLayoutable() throws Exception {
        org.jhotdraw.contrib.Layoutable[] tests = { new GraphicalCompositeFigure(), null };
        for (int i = 0; i < tests.length; i++) {
            simplelayouter.setLayoutable(tests[i]);
            assertEquals(tests[i], simplelayouter.getLayoutable());
        }
    }

    public void testSetGetInsets() throws Exception {
        java.awt.Insets[] tests = { new java.awt.Insets(5, 5, 5, 5), null };
        for (int i = 0; i < tests.length; i++) {
            simplelayouter.setInsets(tests[i]);
            assertEquals(tests[i], simplelayouter.getInsets());
        }
    }

    public void testCreate() throws Exception {
    }

    public void testCalculateLayout() throws Exception {
    }

    public void testLayout() throws Exception {
    }

    public void testRead() throws Exception {
    }

    public void testWrite() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
