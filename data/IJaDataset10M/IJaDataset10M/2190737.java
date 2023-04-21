package fuzzy.operators;

import junit.framework.Assert;

/**
 * @author Paolo Costa <paolo.costa@polimi.it>
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BoundedDifferenceTest extends Fixture {

    /**
	 * Constructor for BoundedDifferenceTest.
	 * @param arg0
	 */
    public BoundedDifferenceTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(BoundedDifferenceTest.class);
    }

    public final void testOperator() {
        try {
            Assert.assertEquals(0.0F, operator.operator(0.0F, 0.0F), 0.0001);
            Assert.assertEquals(1.0F, operator.operator(1.0F, 1.0F), 0.0001);
            Assert.assertEquals(0.0F, operator.operator(0.2F, 0.3F), 0.0001);
            Assert.assertEquals(0.3F, operator.operator(0.6F, 0.7F), 0.0001);
        } catch (OutOfRangeException e) {
            Assert.fail();
        }
    }

    public void setUpOperator() {
        operator = new BoundedDifference();
    }
}
