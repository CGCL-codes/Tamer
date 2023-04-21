package net.openchrom.chromatogram.msd.model.xic;

import junit.framework.TestCase;

/**
 * Tests the class BackgroundAbundanceRange concerning equals, hashCode and
 * toString.
 * 
 * @author eselmeister
 */
public class BackgroundAbundanceRange_3_Test extends TestCase {

    private IBackgroundAbundanceRange backgroundAbundanceRange1;

    private IBackgroundAbundanceRange backgroundAbundanceRange2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        backgroundAbundanceRange1 = new BackgroundAbundanceRange(3, 5);
        backgroundAbundanceRange2 = new BackgroundAbundanceRange(1, 5);
    }

    @Override
    protected void tearDown() throws Exception {
        backgroundAbundanceRange1 = null;
        backgroundAbundanceRange2 = null;
        super.tearDown();
    }

    public void testEquals_1() {
        assertFalse(backgroundAbundanceRange1.equals(backgroundAbundanceRange2));
    }

    public void testEquals_2() {
        assertFalse(backgroundAbundanceRange2.equals(backgroundAbundanceRange1));
    }

    public void testHashCode_1() {
        assertFalse(backgroundAbundanceRange1.hashCode() == backgroundAbundanceRange2.hashCode());
    }

    public void testHashCode_2() {
        assertFalse(backgroundAbundanceRange2.hashCode() == backgroundAbundanceRange1.hashCode());
    }

    public void testToString_1() {
        assertFalse(backgroundAbundanceRange1.toString().equals(backgroundAbundanceRange2.toString()));
    }

    public void testToString_2() {
        assertFalse(backgroundAbundanceRange2.toString().equals(backgroundAbundanceRange1.toString()));
    }
}
