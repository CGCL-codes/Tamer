package org.identifylife.descriptlet.store.model;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author dbarnier
 *
 */
public class ValueEnumTests {

    @Test
    public void testPresentValues() {
        Value present = Value.PRESENT;
        Assert.assertTrue(present.getValue() == Value.PRESENT_VALUE);
    }

    @Test
    public void testAbsentValues() {
        Value absent = Value.ABSENT;
        Assert.assertTrue(absent.getValue() == Value.ABSENT_VALUE);
    }

    @Test
    public void testUncertainValue() {
        Value uncertain = Value.UNCERTAIN;
        Assert.assertTrue(uncertain.getValue() == Value.UNCERTAIN_VALUE);
    }
}
