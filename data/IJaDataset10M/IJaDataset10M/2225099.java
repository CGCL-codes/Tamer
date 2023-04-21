package uk.ac.shef.wit.simmetrics.similaritymetrics;

import junit.framework.TestCase;

/**
 * Performs a unit test upon the MongeElkan string metric.
 * 
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a
 *         href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 */
public class MongeElkanTest extends TestCase {

    private AbstractStringMetric metric;

    /**
	 * Sets up the test fixture.
	 * 
	 * Called before every test case method.
	 */
    protected void setUp() {
        metric = new MongeElkan();
    }

    /**
	 * Tears down the test fixture.
	 * 
	 * Called after every test case method.
	 */
    protected void tearDown() {
    }

    /**
	 * Tests emptying the cart.
	 */
    public void testGetSimilarity() {
        float result = metric.getSimilarity("Test String1", "Test String2");
        assertEquals(0.92857146f, result);
    }
}
