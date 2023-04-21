package CH.ifa.draw.test.figures;

import java.awt.Point;
import CH.ifa.draw.figures.BorderDecorator;
import junit.framework.TestCase;

public class BorderDecoratorTest extends TestCase {

    private BorderDecorator borderdecorator;

    /**
	 * Constructor BorderDecoratorTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public BorderDecoratorTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public CH.ifa.draw.figures.BorderDecorator createInstance() throws Exception {
        return new CH.ifa.draw.figures.BorderDecorator();
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        borderdecorator = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        borderdecorator = null;
        super.tearDown();
    }

    public void testSetGetBorderOffset() throws Exception {
        Point[] tests = { new java.awt.Point() };
        for (int i = 0; i < tests.length; i++) {
            borderdecorator.setBorderOffset(tests[i]);
            assertEquals(tests[i], borderdecorator.getBorderOffset());
        }
    }

    /**
	  * Test a null argument to setBorderOffset.  Expect an Point(0, 0) to come back
	  * 
	  * @see CH.ifa.draw.figures.BorderDecorator#setBorderOffset(java.awt.Point)
	  */
    public void setNullBorderOffset() throws Exception {
        Point original = borderdecorator.getBorderOffset();
        borderdecorator.setBorderOffset(null);
        assertEquals(new Point(0, 0), borderdecorator.getBorderOffset());
    }

    public void testDraw() throws Exception {
    }

    public void testDisplayBox() throws Exception {
    }

    public void testFigureInvalidated() throws Exception {
    }

    public void testConnectionInsets() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
