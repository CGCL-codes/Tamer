package com.rapidminer.operator.performance.test;

import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.rapidminer.operator.performance.EstimatedPerformance;
import com.rapidminer.operator.performance.PerformanceCriterion;

/**
 * Tests {@link EstimatedPerformance}.
 * 
 * @author Simon Fischer, Ingo Mierswa
 *          ingomierswa Exp $
 */
public class EstimatedCriterionTest extends AbstractCriterionTestCase {

    private EstimatedPerformance performance10x08, performance20x04;

    @Before
    public void setUp() throws Exception {
        performance10x08 = new EstimatedPerformance("test_performance", 0.8, 10, false);
        performance20x04 = new EstimatedPerformance("test_performance", 0.4, 20, false);
    }

    @After
    public void tearDown() throws Exception {
        performance10x08 = performance20x04 = null;
    }

    /**
	 * Tests micro and makro average. Since makro average is implemented in
	 * {@link PerformanceCriterion}, this does not have to be tested for
	 * measured performance criteria.
	 */
    @Test
    public void testAverage() {
        performance10x08.buildAverage(performance20x04);
        assertEquals("Wrong weighted average", (10 * 0.8 + 20 * 0.4) / (10 + 20), performance10x08.getMikroAverage(), 0.0000001);
        assertEquals("Wrong makro average", (0.8 + 0.4) / 2, performance10x08.getMakroAverage(), 0.0000001);
    }

    @Test
    public void testClone() {
        cloneTest("Clone of simple criterion", performance10x08);
        performance10x08.buildAverage(performance20x04);
        cloneTest("Clone of averaged criterion", performance10x08);
    }
}
