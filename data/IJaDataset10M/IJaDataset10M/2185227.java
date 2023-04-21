package org.openscience.cdk.qsar.result;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.qsar.result.DoubleArrayResultType;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.CDKTestCase;

/**
 * @cdk.module test-standard
 */
public class DoubleArrayResultTypeTest extends CDKTestCase {

    public DoubleArrayResultTypeTest() {
        super();
    }

    @Test
    public void testDoubleArrayResultType() {
        IDescriptorResult type = new DoubleArrayResultType(6);
        Assert.assertNotNull(type);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("DoubleArrayResultType", new DoubleArrayResultType(7).toString());
    }

    @Test
    public void testLength() {
        Assert.assertEquals(7, new DoubleArrayResultType(7).length());
    }
}
