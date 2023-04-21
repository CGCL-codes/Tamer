package org.norecess.nolatte.primitives.arithmetic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class GreaterThanCalculatorTest {

    private GreaterThanCalculator myCalculator;

    @Before
    public void setUp() {
        myCalculator = new GreaterThanCalculator();
    }

    @Test
    public void should_return_true_when_appropriate() {
        assertTrue(myCalculator.doLogic(10, 2));
        assertTrue(myCalculator.doLogic(44, 12));
    }

    @Test
    public void should_false_when_appropriate() {
        assertFalse(myCalculator.doLogic(2, 12));
        assertFalse(myCalculator.doLogic(22, 9876));
        assertFalse(myCalculator.doLogic(9876, 9876));
    }
}
