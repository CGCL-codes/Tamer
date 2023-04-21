package net.sourceforge.jfl.core;

import java.util.Set;
import net.sourceforge.jfl.core.interval.Interval;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import junit.framework.TestCase;

/**
 * 
 * @author arons777@users.sourceforge.net
 *
 */
public class DiscreteFuzzySetTest extends TestCase {

    private static Log log = LogFactory.getLog(DiscreteFuzzySetTest.class);

    private static double precision = 1E-16;

    /**
     * Test an <code>DiscreteFuzzySetTest<Integer></code>.
     *
     */
    @Test
    public void testIntegerInstance() {
        log.info("testIntegerInstance");
        DiscreteFuzzySet<Integer> fuzzySet = new DiscreteFuzzySet<Integer>();
        fuzzySet.addElement(null, 1.0);
        fuzzySet.addElement(-2, 0.0);
        fuzzySet.addElement(-1, 0.5);
        fuzzySet.addElement(0, 1.0);
        fuzzySet.addElement(1, 0.5);
        assertEquals("wrong membership values", 0.0, fuzzySet.getMembership(-2), precision);
        assertEquals("wrong membership values", 0.5, fuzzySet.getMembership(-1), precision);
        assertEquals("wrong membership values", 1.0, fuzzySet.getMembership(0), precision);
        assertEquals("wrong membership values", 0.5, fuzzySet.getMembership(1), precision);
        Set<Integer> support = fuzzySet.getSupport();
        assertNotNull("returned null support", support);
        assertEquals("wrong support size", 3, support.size());
        assertEquals("wrong membership values", 1.0, fuzzySet.removeElement(0), precision);
        assertEquals("wrong membership values", 0.0, fuzzySet.getMembership(0), precision);
        fuzzySet.addElement(10, 1.5);
        assertEquals("wrong membership values", 0.0, fuzzySet.removeElement(10), precision);
        fuzzySet.addElement(11, -0.5);
        assertEquals("wrong membership values", 0.0, fuzzySet.removeElement(11), precision);
    }

    /**
     * Test correct values for support
     *
     */
    @Test
    public void testSupport() {
        log.info("testSupport");
        DiscreteFuzzySet<Integer> fuzzySet = new DiscreteFuzzySet<Integer>();
        fuzzySet.addElement(null, 1.0);
        fuzzySet.addElement(0, 0.0);
        fuzzySet.addElement(1, 0.5);
        fuzzySet.addElement(2, 1.0);
        fuzzySet.addElement(3, 0.5);
        fuzzySet.addElement(4, 0.5);
        fuzzySet.addElement(5, 0.5);
        fuzzySet.addElement(6, 0.0);
        fuzzySet.addElement(7, 0.0);
        fuzzySet.addElement(8, 0.5);
        fuzzySet.addElement(9, 0.5);
        fuzzySet.addElement(10, 1.0);
        Set<Integer> support = fuzzySet.getSupport();
        assertNotNull("support is ", support);
        assertEquals("wrong support size", 8, support.size());
        assertTrue("error in support set", support.contains(1));
        assertTrue("error in support set", support.contains(2));
        assertTrue("error in support set", support.contains(3));
        assertTrue("error in support set", support.contains(4));
        assertTrue("error in support set", support.contains(5));
        assertTrue("error in support set", support.contains(8));
        assertTrue("error in support set", support.contains(9));
        assertTrue("error in support set", support.contains(10));
        assertFalse("error in support set", support.contains(0));
        assertFalse("error in support set", support.contains(6));
        assertFalse("error in support set", support.contains(7));
        assertFalse("error in support set", support.contains(11));
        Set<Interval> supportIntervalSet = fuzzySet.getSupportIntervals();
        assertNull("support interval set must be null", supportIntervalSet);
        DiscreteFuzzySet<Integer> complementary = fuzzySet.complement();
        assertNotNull("complementary is null", complementary);
        Set<Integer> supportC = complementary.getSupport();
        assertNull("support is not null", supportC);
    }

    @Test
    public void testComplementary() {
        log.info("testComplementary");
        DiscreteFuzzySet<Integer> fuzzy = new DiscreteFuzzySet<Integer>();
        fuzzy.addElement(-2, 0.0);
        fuzzy.addElement(-1, 0.5);
        fuzzy.addElement(0, 1.0);
        fuzzy.addElement(1, 0.5);
        fuzzy.addElement(2, 0.0);
        DiscreteFuzzySet<Integer> complementary = (DiscreteFuzzySet<Integer>) fuzzy.complement();
        assertNotNull("complementary is", complementary);
        assertEquals("wrong membership values", 0.0, complementary.getMembership(null), precision);
        assertEquals("wrong membership values", 1.0, complementary.getMembership(-3), precision);
        assertEquals("wrong membership values", 1.0, complementary.getMembership(-2), precision);
        assertEquals("wrong membership values", 0.5, complementary.getMembership(-1), precision);
        assertEquals("wrong membership values", 0.0, complementary.getMembership(0), precision);
        assertEquals("wrong membership values", 0.5, complementary.getMembership(1), precision);
        assertEquals("wrong membership values", 1.0, complementary.getMembership(2), precision);
        Set<Integer> support = complementary.getSupport();
        assertNull("complemantary supoprt is", support);
        complementary.addElement(10, 0.7);
        assertEquals("wrong membership values", 0.7, complementary.getMembership(10));
        fuzzy = complementary.complement();
        assertEquals("wrong membership values", 0.0, fuzzy.getMembership(-2), precision);
        assertEquals("wrong membership values", 0.5, fuzzy.getMembership(-1), precision);
        assertEquals("wrong membership values", 1.0, fuzzy.getMembership(0), precision);
        assertEquals("wrong membership values", 0.5, fuzzy.getMembership(1), precision);
        assertEquals("wrong membership values", 0.0, fuzzy.getMembership(2), precision);
        assertEquals("wrong membership values", 0.3, fuzzy.getMembership(10), precision);
        support = fuzzy.getSupport();
        assertNotNull("support is", support);
        assertEquals("wrong supoprt size", 4, support.size());
        assertEquals("wrong membership values", 1.0, complementary.removeElement(2), precision);
        assertEquals("wrong membership values", 0.0, complementary.getMembership(2), precision);
        complementary = complementary.complement();
        assertNotNull("complementary is", complementary);
        assertEquals("wrong membership values", 0.0, complementary.getMembership(-2), precision);
        assertEquals("wrong membership values", 0.5, complementary.getMembership(-1), precision);
        assertEquals("wrong membership values", 1.0, complementary.getMembership(0), precision);
        assertEquals("wrong membership values", 0.5, complementary.getMembership(1), precision);
        assertEquals("wrong membership values", 1.0, complementary.getMembership(2), precision);
        assertEquals("wrong membership values", 0.3, complementary.getMembership(10), precision);
    }

    @Test
    public void testDefaultOperator_AND() {
        log.info("testDefaultOperator_AND");
        DiscreteFuzzySet<Integer> fuzzySetA = new DiscreteFuzzySet<Integer>();
        fuzzySetA.addElement(null, 1.0);
        fuzzySetA.addElement(-2, 0.0);
        fuzzySetA.addElement(-1, 0.5);
        fuzzySetA.addElement(0, 1.0);
        fuzzySetA.addElement(1, 0.5);
        DiscreteFuzzySet<Integer> fuzzySetB = new DiscreteFuzzySet<Integer>();
        fuzzySetB.addElement(-3, 0.2);
        fuzzySetB.addElement(-2, 1.0);
        fuzzySetB.addElement(-1, 1.0);
        fuzzySetB.addElement(0, 0.2);
        DiscreteFuzzySet<Integer> result = fuzzySetA.and(fuzzySetB);
        assertNotNull(result);
        assertEquals(0.0, result.getMembership(-3), precision);
        assertEquals(0.0, result.getMembership(-2), precision);
        assertEquals(0.5, result.getMembership(-1), precision);
        assertEquals(0.2, result.getMembership(0), precision);
        assertEquals(0.0, result.getMembership(1), precision);
        assertEquals(2, result.getSupport().size());
        result = fuzzySetB.and(fuzzySetA);
        assertNotNull(result);
        assertEquals(0.0, result.getMembership(-3), precision);
        assertEquals(0.0, result.getMembership(-2), precision);
        assertEquals(0.5, result.getMembership(-1), precision);
        assertEquals(0.2, result.getMembership(0), precision);
        assertEquals(0.0, result.getMembership(1), precision);
        assertEquals(2, result.getSupport().size());
        DiscreteFuzzySet<Integer> fuzzySetAComplement = fuzzySetA.complement();
        assertNotNull(fuzzySetAComplement);
        result = fuzzySetAComplement.and(fuzzySetA);
        assertNotNull(result);
        assertEquals(0.0, result.getMembership(-10), precision);
        assertEquals(0.0, result.getMembership(-3), precision);
        assertEquals(0.0, result.getMembership(-2), precision);
        assertEquals(0.5, result.getMembership(-1), precision);
        assertEquals(0.0, result.getMembership(0), precision);
        assertEquals(0.5, result.getMembership(1), precision);
        assertEquals(0.0, result.getMembership(2), precision);
        assertEquals(0.0, result.getMembership(10), precision);
    }

    @Test
    public void testDefaultOperator_OR() {
        log.info("testDefaultOperator_OR");
        DiscreteFuzzySet<Integer> fuzzySetA = new DiscreteFuzzySet<Integer>();
        fuzzySetA.addElement(null, 1.0);
        fuzzySetA.addElement(-2, 0.0);
        fuzzySetA.addElement(-1, 0.5);
        fuzzySetA.addElement(0, 1.0);
        fuzzySetA.addElement(1, 0.5);
        DiscreteFuzzySet<Integer> fuzzySetB = new DiscreteFuzzySet<Integer>();
        fuzzySetB.addElement(-3, 0.2);
        fuzzySetB.addElement(-2, 1.0);
        fuzzySetB.addElement(-1, 1.0);
        fuzzySetB.addElement(0, 0.2);
        DiscreteFuzzySet<Integer> result = fuzzySetA.or(fuzzySetB);
        assertNotNull(result);
        assertEquals(0.2, result.getMembership(-3), precision);
        assertEquals(1.0, result.getMembership(-2), precision);
        assertEquals(1.0, result.getMembership(-1), precision);
        assertEquals(1.0, result.getMembership(0), precision);
        assertEquals(0.5, result.getMembership(1), precision);
        assertEquals(0.0, result.getMembership(2), precision);
        assertEquals(5, result.getSupport().size());
        result = fuzzySetB.or(fuzzySetA);
        assertNotNull(result);
        assertEquals(0.2, result.getMembership(-3), precision);
        assertEquals(1.0, result.getMembership(-2), precision);
        assertEquals(1.0, result.getMembership(-1), precision);
        assertEquals(1.0, result.getMembership(0), precision);
        assertEquals(0.5, result.getMembership(1), precision);
        assertEquals(0.0, result.getMembership(2), precision);
        assertEquals(5, result.getSupport().size());
        DiscreteFuzzySet<Integer> fuzzySetAComplement = fuzzySetA.complement();
        assertNotNull(fuzzySetAComplement);
        result = fuzzySetAComplement.or(fuzzySetA);
        assertNotNull(result);
        assertEquals(1.0, result.getMembership(-10), precision);
        assertEquals(1.0, result.getMembership(-3), precision);
        assertEquals(1.0, result.getMembership(-2), precision);
        assertEquals(0.5, result.getMembership(-1), precision);
        assertEquals(1.0, result.getMembership(0), precision);
        assertEquals(0.5, result.getMembership(1), precision);
        assertEquals(1.0, result.getMembership(2), precision);
        assertEquals(1.0, result.getMembership(10), precision);
    }
}
