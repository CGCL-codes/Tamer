package org.openscience.cdk.test.qsar.result;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.qsar.result.IntegerResultType;
import org.openscience.cdk.test.NewCDKTestCase;

/**
 * @cdk.module test-standard
 */
public class IntegerResultTypeTest extends NewCDKTestCase {

    public IntegerResultTypeTest() {
        super();
    }

    @Test
    public void testIntegerResultType() {
        IDescriptorResult type = new IntegerResultType();
        Assert.assertNotNull(type);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("IntegerResultType", new IntegerResultType().toString());
    }

    @Test
    public void testLength() {
        Assert.assertEquals(1, new IntegerResultType().length());
    }
}
