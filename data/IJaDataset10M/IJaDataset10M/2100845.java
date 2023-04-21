package test.math;

import edu.math.*;
import junit.framework.*;

public class FractionItemTest extends TestCase {

    private double precision = 0.00000001;

    public void testAddFractionItem() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem b = new FractionItem(1, 3);
        FractionItem c = (FractionItem) a.add(b);
        Assert.assertEquals(c, new FractionItem(5, 6));
    }

    public void testAddDouble() {
        FractionItem a = new FractionItem(1, 4);
        FractionItem c = (FractionItem) a.add(0.25);
        Assert.assertEquals(c, new FractionItem(1, 2));
    }

    public void testSubFractionItem() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem b = new FractionItem(1, 4);
        FractionItem c = (FractionItem) a.sub(b);
        Assert.assertEquals(c, new FractionItem(1, 4));
    }

    public void testSubDouble() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem c = (FractionItem) a.sub(0.25);
        Assert.assertEquals(c, new FractionItem(1, 4));
    }

    public void testMulFractionItem() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem b = new FractionItem(1, 3);
        FractionItem c = (FractionItem) a.mul(b);
        Assert.assertEquals(c, new FractionItem(1, 6));
    }

    public void testMulDouble() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem c = (FractionItem) a.mul(0.5);
        Assert.assertEquals(c, new FractionItem(1, 4));
    }

    public void testDivFractionItem() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem b = new FractionItem(1, 3);
        FractionItem c = (FractionItem) a.div(b);
        Assert.assertEquals(c, new FractionItem(3, 2));
    }

    public void testDivDouble() {
        FractionItem a = new FractionItem(1, 4);
        FractionItem c = (FractionItem) a.div(0.5);
        Assert.assertEquals(c, new FractionItem(1, 2));
    }

    public void testPowFractionItem() {
        FractionItem a = new FractionItem(1, 4);
        FractionItem c = (FractionItem) a.pow(new FractionItem(1, 2));
        Assert.assertEquals(c, new FractionItem(1, 2));
    }

    public void testPowLong() {
        FractionItem a = new FractionItem(1, 4);
        FractionItem c = (FractionItem) a.pow(2);
        Assert.assertEquals(c, new FractionItem(1, 16));
    }

    public void testPowDouble() {
        FractionItem a = new FractionItem(1, 4);
        FractionItem c = (FractionItem) a.pow(0.5);
        Assert.assertEquals(c, new FractionItem(1, 2));
    }

    public void testClone() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem b = (FractionItem) a.clone();
        boolean c = (a.equals(b) && a != b);
        Assert.assertEquals(c, true);
    }

    public void testCompareToFractionItem() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem b = new FractionItem(1, 3);
        FractionItem c = new FractionItem(1, 2);
        Assert.assertEquals(b.compareTo(a), -1);
        Assert.assertEquals(a.compareTo(c), 0);
        Assert.assertEquals(a.compareTo(b), 1);
    }

    public void testCompareToDouble() {
        FractionItem a = new FractionItem(1, 2);
        Assert.assertEquals(a.compareTo(0.7), -1);
        Assert.assertEquals(a.compareTo(0.5), 0);
        Assert.assertEquals(a.compareTo(0.3), 1);
    }

    public void testEqualsFractionItem() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem b = new FractionItem(1, 3);
        boolean c = a.equals(b);
        Assert.assertEquals(c, false);
    }

    public void testEqualsDouble() {
        FractionItem a = new FractionItem(1, 2);
        boolean c = a.equals(0.5);
        Assert.assertEquals(c, true);
    }

    public void testEqualsFractionItemAccuracy() {
        FractionItem a = new FractionItem(1, 2);
        FractionItem b = new FractionItem(1, 1);
        boolean c = a.equals(b, 0.6);
        Assert.assertEquals(c, true);
    }

    public void testEqualsDoubleAccuracy() {
        FractionItem a = new FractionItem(1, 3);
        boolean c = a.equals(0.5, 0.1);
        Assert.assertEquals(c, false);
    }

    public void testReduce() {
        FractionItem a = new FractionItem(15, 6);
        Assert.assertEquals(a.reduce(), new FractionItem(5, 2));
    }

    public void testLongValue() {
        FractionItem a = new FractionItem(6, 4);
        Assert.assertEquals(a.longValue(), 1);
    }

    public void testIntegerPart() {
        FractionItem a = new FractionItem(6, 4);
        Assert.assertEquals(a.integerPart(), new FractionItem(1, 1));
    }

    public void testDoubleValue() {
        FractionItem a = new FractionItem(6, 4);
        FractionItem b = new FractionItem(7, 3);
        Assert.assertEquals(a.doubleValue(), 1.5);
        Assert.assertTrue(Math.abs(b.doubleValue() - (double) 7 / 3) <= precision);
    }

    public void testFractionItemalPart() {
        FractionItem a = new FractionItem(6, 4);
        Assert.assertEquals(a.fractionalPart(), new FractionItem(1, 2));
    }
}
