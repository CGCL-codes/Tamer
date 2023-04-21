package fuzzy.operators;

import junit.framework.Assert;

/**
 * @author Paolo Costa <paolo.costa@polimi.it>
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SchweizerUnion2Test extends Fixture {

    /**
	 * Constructor for SchweizerUnion2Test.
	 * @param arg0
	 */
    public SchweizerUnion2Test(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SchweizerUnion2Test.class);
    }

    public final void testOperator() {
        try {
            Assert.assertEquals(0.0F, operator.operator(0F, 0F), 0.0001);
            Assert.assertEquals(1.0F, operator.operator(1F, 1F), 0.0001);
            Assert.assertEquals(0.4201F, operator.operator(0.2F, 0.3F), 0.0001);
        } catch (OutOfRangeException e) {
            Assert.fail();
        }
    }

    public final void testParameter() {
        try {
            new SchweizerUnion2(-0.5F);
            Assert.fail("OutOfRangeException not generated");
        } catch (OutOfRangeException e) {
        }
    }

    public void setUpOperator() {
        try {
            operator = new SchweizerUnion2(0.5F);
        } catch (OutOfRangeException e) {
            Assert.fail();
        }
    }
}
