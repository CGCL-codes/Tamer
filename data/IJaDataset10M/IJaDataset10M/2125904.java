package org.jhotdraw.test.contrib;

import java.awt.Point;
import junit.framework.TestCase;
import org.jhotdraw.contrib.GraphicalCompositeFigure;
import org.jhotdraw.contrib.SimpleLayouter;
import org.jhotdraw.figures.RectangleFigure;

public class GraphicalCompositeFigureTest extends TestCase {

    private GraphicalCompositeFigure graphicalcompositefigure;

    /**
	 * Constructor GraphicalCompositeFigureTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public GraphicalCompositeFigureTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public GraphicalCompositeFigure createInstance() throws Exception {
        return new GraphicalCompositeFigure();
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        graphicalcompositefigure = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        graphicalcompositefigure = null;
        super.tearDown();
    }

    public void testClone() throws Exception {
    }

    public void testDisplayBox() throws Exception {
    }

    public void testBasicDisplayBox() throws Exception {
    }

    public void testUpdate() throws Exception {
    }

    public void testDraw() throws Exception {
    }

    public void testHandles() throws Exception {
    }

    public void testGetAttribute() throws Exception {
    }

    public void testSetAttribute() throws Exception {
    }

    public void testSetGetPresentationFigure() throws Exception {
        org.jhotdraw.framework.Figure[] tests = { new RectangleFigure(new Point(10, 10), new Point(100, 100)), null };
        for (int i = 0; i < tests.length; i++) {
            graphicalcompositefigure.setPresentationFigure(tests[i]);
            assertEquals(tests[i], graphicalcompositefigure.getPresentationFigure());
        }
    }

    public void testLayout() throws Exception {
    }

    public void testSetGetLayouter() throws Exception {
        org.jhotdraw.contrib.Layouter[] tests = { new SimpleLayouter(graphicalcompositefigure), null };
        for (int i = 0; i < tests.length; i++) {
            graphicalcompositefigure.setLayouter(tests[i]);
            assertEquals(tests[i], graphicalcompositefigure.getLayouter());
        }
    }

    public void testFigureRequestRemove() throws Exception {
    }

    public void testRead() throws Exception {
    }

    public void testWrite() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
