package net.sourceforge.jaulp.comparators;

import junit.framework.TestCase;

/**
 * Test class for the class HashCodeComparator.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public class HashCodeComparatorTest extends TestCase {

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     * {@inheritDoc}
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     * {@inheritDoc}
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for.
     *
     * {@link net.sourceforge.jaulp.comparators.HashCodeComparator#compare(java.lang.Object, java.lang.Object)}
     * .
     */
    public void testCompare() {
        final String testString = "Albert";
        final String notExpected = "Asterios";
        final String expected = "Albert";
        final HashCodeComparator<String> comparator = new HashCodeComparator<String>();
        int i = comparator.compare(testString, notExpected);
        System.out.println("comparator.compare(testString, notExpected)=i:" + i);
        System.out.println("testString.hashCode():" + testString.hashCode());
        System.out.println("notExpected.hashCode():" + notExpected.hashCode());
        System.out.println(testString.hashCode() + ">" + notExpected.hashCode());
        assertTrue("Hashcodes should be " + testString.hashCode() + ">" + notExpected.hashCode() + ".", comparator.compare(testString, notExpected) == 1);
        i = comparator.compare(notExpected, testString);
        System.out.println("comparator.compare(notExpected, testString)=i:" + i);
        System.out.println("notExpected.hashCode():" + notExpected.hashCode());
        System.out.println("testString.hashCode():" + testString.hashCode());
        System.out.println(notExpected.hashCode() + "<" + testString.hashCode());
        assertTrue("Hashcodes should be " + notExpected.hashCode() + "<" + testString.hashCode() + ".", comparator.compare(notExpected, testString) == -1);
        i = comparator.compare(testString, expected);
        System.out.println("comparator.compare(testString, expected)=i:" + i);
        System.out.println("testString.hashCode():" + testString.hashCode());
        System.out.println("expected.hashCode():" + expected.hashCode());
        System.out.println(testString.hashCode() + "=" + expected.hashCode());
        assertTrue("Hashcodes should be " + testString.hashCode() + "=" + expected.hashCode() + ".", comparator.compare(testString, expected) == 0);
    }
}
