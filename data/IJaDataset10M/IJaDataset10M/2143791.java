package jmathx.model;

import jmathx.model.RealInterval;
import java.math.BigDecimal;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author Nikolay Grozev
 */
public class RealIntervalTest extends TestCase {

    @Test
    public void testInstantiation() {
        RealInterval fromStrinng = new RealInterval("(5; 6)");
        RealInterval fromNumbers = new RealInterval(BigDecimal.valueOf(5), BigDecimal.valueOf(6), false, false);
        assertEquals(fromStrinng, fromNumbers);
        fromStrinng = new RealInterval("[5.789 ; 6.89)");
        fromNumbers = new RealInterval(BigDecimal.valueOf(5.789), BigDecimal.valueOf(6.89), true, false);
        assertEquals(fromStrinng, fromNumbers);
        fromStrinng = new RealInterval("( ; 6.89)");
        fromNumbers = new RealInterval(null, BigDecimal.valueOf(6.89), false, false);
        assertEquals(fromStrinng, fromNumbers);
        fromStrinng = new RealInterval("( ; 6.89789]");
        fromNumbers = new RealInterval(null, BigDecimal.valueOf(6.89789), false, false);
        assertFalse(fromStrinng.equals(fromNumbers));
        fromStrinng = new RealInterval("[ - 78; 6.89)");
        fromNumbers = new RealInterval(null, BigDecimal.valueOf(6.89), false, false);
        assertFalse(fromStrinng.equals(fromNumbers));
        fromStrinng = new RealInterval("( ; 6.89)");
        fromNumbers = new RealInterval(BigDecimal.valueOf(6.89), false, false);
        assertEquals(fromStrinng, fromNumbers);
        fromStrinng = new RealInterval("[ 6.8978 ; )");
        fromNumbers = new RealInterval(BigDecimal.valueOf(6.8978), true, true);
        assertEquals(fromStrinng, fromNumbers);
        fromStrinng = new RealInterval("[ 6.8978 ; 6.8978]");
        fromNumbers = new RealInterval(BigDecimal.valueOf(6.8978), BigDecimal.valueOf(6.8978), true, true);
        assertEquals(fromStrinng, fromNumbers);
        fromStrinng = new RealInterval("( ; )");
        fromNumbers = new RealInterval(null, true, true);
        assertEquals(fromStrinng, fromNumbers);
    }

    @Test
    public void testUnion() {
        RealInterval a = new RealInterval("[5.53;6]");
        RealInterval b = new RealInterval("[3;8]");
        List<RealInterval> unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 1);
        RealInterval union = unionIntervals.get(0);
        assertEquals(union, b);
        a = new RealInterval("[4;5]");
        b = new RealInterval("[3;4)");
        unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 1);
        union = unionIntervals.get(0);
        assertEquals(union, new RealInterval("[3;5]"));
        a = new RealInterval("[4;5]");
        b = new RealInterval("[3;4)");
        unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 1);
        union = unionIntervals.get(0);
        assertEquals(union, new RealInterval("[3;5]"));
        a = new RealInterval("[4;6]");
        b = new RealInterval("[5;8)");
        unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 1);
        union = unionIntervals.get(0);
        assertEquals(union, new RealInterval("[4;8)"));
        a = new RealInterval("(4;5.2]");
        b = new RealInterval("[5.2;8)");
        unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 1);
        union = unionIntervals.get(0);
        assertEquals(union, new RealInterval("(4;8)"));
        a = new RealInterval("(4;4.9]");
        b = new RealInterval("(5;8)");
        unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 2);
        assertTrue(unionIntervals.indexOf(a) == 0);
        assertTrue(unionIntervals.indexOf(b) == 1);
        a = new RealInterval("(7;8)");
        b = new RealInterval("[4;5]");
        unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 2);
        assertTrue(unionIntervals.indexOf(a) == 1);
        assertTrue(unionIntervals.indexOf(b) == 0);
        a = new RealInterval("(;5]");
        b = new RealInterval("[5;8)");
        unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 1);
        union = unionIntervals.get(0);
        assertEquals(union, new RealInterval("(;8)"));
        a = new RealInterval("(;5)");
        b = new RealInterval("(5;)");
        unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 2);
        assertTrue(unionIntervals.indexOf(a) == 0);
        assertTrue(unionIntervals.indexOf(b) == 1);
        a = new RealInterval("(;)");
        b = new RealInterval("(5;6)");
        unionIntervals = a.unite(b);
        assertTrue(unionIntervals.size() == 1);
        union = unionIntervals.get(0);
        assertEquals(union, a);
        a = new RealInterval("(5;7)");
        unionIntervals = a.unite(RealInterval.THE_EMPTY_INTERVAl);
        assertTrue(unionIntervals.size() == 1);
        union = unionIntervals.get(0);
        assertEquals(union, a);
        a = new RealInterval("(5;7)");
        unionIntervals = a.unite(a);
        assertTrue(unionIntervals.size() == 1);
        union = unionIntervals.get(0);
        assertEquals(union, a);
        assertTrue(RealInterval.THE_EMPTY_INTERVAl.unite(RealInterval.THE_EMPTY_INTERVAl).size() == 0);
    }

    @Test
    public void testIntersection() {
        RealInterval a = new RealInterval("[5;9]");
        RealInterval b = new RealInterval("[3;9]");
        RealInterval intersection = a.intersect(b);
        assertEquals(intersection, a);
        a = new RealInterval("[5;9]");
        b = new RealInterval("[3;5]");
        intersection = a.intersect(b);
        assertEquals(intersection, new RealInterval("[5;5]"));
        a = new RealInterval("[5;9]");
        b = new RealInterval("[3;5)");
        intersection = a.intersect(b);
        assertEquals(intersection, RealInterval.THE_EMPTY_INTERVAl);
        a = new RealInterval("[3;4)");
        b = new RealInterval("[7;9]");
        intersection = a.intersect(b);
        assertEquals(intersection, RealInterval.THE_EMPTY_INTERVAl);
        a = new RealInterval("[3;)");
        b = new RealInterval("[7;]");
        intersection = a.intersect(b);
        assertEquals(intersection, b);
        a = new RealInterval("[;4)");
        b = new RealInterval("[2;]");
        intersection = a.intersect(b);
        assertEquals(intersection, new RealInterval("[2;4)"));
        a = new RealInterval("[;)");
        b = new RealInterval("[2;]");
        intersection = a.intersect(b);
        assertEquals(intersection, new RealInterval("[2;)"));
        a = new RealInterval("[3;4)");
        intersection = a.intersect(RealInterval.THE_EMPTY_INTERVAl);
        assertEquals(intersection, RealInterval.THE_EMPTY_INTERVAl);
        a = new RealInterval("[3;4)");
        intersection = a.intersect(a);
        assertEquals(intersection, a);
    }

    @Test
    public void testDiff() {
        RealInterval a = new RealInterval("[;4)");
        RealInterval b = new RealInterval("[1;]");
        List<RealInterval> differences = a.diff(b);
        assertTrue(differences.size() == 1);
        RealInterval difference = differences.get(0);
        assertEquals(difference, new RealInterval("[;1)"));
        a = new RealInterval("[;-5)");
        b = new RealInterval("[1;]");
        differences = a.diff(b);
        assertTrue(differences.size() == 1);
        difference = differences.get(0);
        assertEquals(difference, new RealInterval("[;-5)"));
        a = new RealInterval("[;4]");
        b = new RealInterval("[;2]");
        differences = a.diff(b);
        assertTrue(differences.size() == 1);
        difference = differences.get(0);
        assertEquals(difference, new RealInterval("(2;4]"));
        a = new RealInterval("[;4.23]");
        differences = a.diff(RealInterval.THE_EMPTY_INTERVAl);
        assertTrue(differences.size() == 1);
        difference = differences.get(0);
        assertEquals(difference, a);
        a = new RealInterval("[-5;4]");
        b = new RealInterval("(1;2)");
        differences = a.diff(b);
        assertTrue(differences.size() == 2);
        assertEquals(differences.get(0), new RealInterval("[-5;1]"));
        assertEquals(differences.get(1), new RealInterval("[2;4]"));
        a = RealInterval.INFINITY;
        b = new RealInterval("(1;2)");
        differences = a.diff(b);
        assertTrue(differences.size() == 2);
        assertEquals(differences.get(0), new RealInterval("[;1]"));
        assertEquals(differences.get(1), new RealInterval("[2;)"));
        a = new RealInterval("[0;3]");
        b = new RealInterval("(1;2]");
        differences = a.diff(b);
        assertTrue(differences.size() == 2);
        assertEquals(differences.get(0), new RealInterval("[0;1]"));
        assertEquals(differences.get(1), new RealInterval("(2;3]"));
    }

    @Test
    public void testContainsNumber() {
        RealInterval a = new RealInterval("(5.53;6]");
        assertTrue(a.contains(new BigDecimal("5.531")));
        a = new RealInterval("[-5.53;6]");
        assertTrue(a.contains(new BigDecimal("-5.53")));
        a = new RealInterval("[-5.53;6]");
        assertTrue(a.contains(new BigDecimal("-5.53")));
        a = new RealInterval("[;6]");
        assertTrue(a.contains(new BigDecimal("-5.53")));
        a = new RealInterval("[;6)");
        assertFalse(a.contains(new BigDecimal("6")));
        a = new RealInterval("[;]");
        assertTrue(a.contains(new BigDecimal("1000")));
        a = new RealInterval("[1;2]");
        assertFalse(a.contains(new BigDecimal("1000")));
        assertFalse(RealInterval.THE_EMPTY_INTERVAl.contains(new BigDecimal("-5.53")));
    }

    @Test
    public void testContainsInterval() {
        RealInterval a = RealInterval.valueOf("[1;2]");
        RealInterval b = RealInterval.valueOf("(1;2)");
        assertTrue(a.contains(b));
        a = RealInterval.valueOf("[1;]");
        b = RealInterval.valueOf("[1;2)");
        assertTrue(a.contains(b));
        a = RealInterval.valueOf("[;2]");
        b = RealInterval.valueOf("(1;2]");
        assertTrue(a.contains(b));
        a = RealInterval.INFINITY;
        b = RealInterval.valueOf("(1;2)");
        assertTrue(a.contains(b));
        a = RealInterval.INFINITY;
        b = RealInterval.THE_EMPTY_INTERVAl;
        assertTrue(a.contains(b));
        a = RealInterval.THE_EMPTY_INTERVAl;
        b = RealInterval.valueOf("(1;2)");
        assertFalse(a.contains(b));
        a = RealInterval.THE_EMPTY_INTERVAl;
        b = RealInterval.THE_EMPTY_INTERVAl;
        assertTrue(a.contains(b));
        a = RealInterval.valueOf("(1;2)");
        b = RealInterval.THE_EMPTY_INTERVAl;
        assertTrue(a.contains(b));
        a = RealInterval.valueOf("(;2)");
        b = RealInterval.valueOf("(1;)");
        assertFalse(a.contains(b));
        a = RealInterval.valueOf("(;0)");
        b = RealInterval.valueOf("[1;)");
        assertFalse(a.contains(b));
    }

    @Test
    public void testComplement() {
        RealInterval a = RealInterval.valueOf("[;]");
        List<RealInterval> complement = a.complement();
        assertTrue(complement.size() == 1);
        assertEquals(complement.get(0), RealInterval.THE_EMPTY_INTERVAl);
        a = RealInterval.valueOf("(1;]");
        complement = a.complement();
        assertTrue(complement.size() == 1);
        assertEquals(complement.get(0), RealInterval.valueOf("[;1]"));
        a = RealInterval.valueOf("[2;3)");
        complement = a.complement();
        assertTrue(complement.size() == 2);
        assertEquals(complement.get(0), RealInterval.valueOf("(;2)"));
        assertEquals(complement.get(1), RealInterval.valueOf("[3;]"));
        a = RealInterval.THE_EMPTY_INTERVAl;
        complement = a.complement();
        assertTrue(complement.size() == 1);
        assertEquals(complement.get(0), RealInterval.INFINITY);
    }

    @Test
    public void testPrecedes() {
        RealInterval a = RealInterval.valueOf("[1;2]");
        RealInterval b = RealInterval.valueOf("[0;0.5]");
        assertTrue(b.precedes(a));
        a = RealInterval.valueOf("[1;2]");
        b = RealInterval.valueOf("[2;3]");
        assertFalse(a.precedes(b));
        a = RealInterval.valueOf("[1;2)");
        b = RealInterval.valueOf("[2;3]");
        assertTrue(a.precedes(b));
        a = RealInterval.valueOf("[1;2]");
        b = RealInterval.valueOf("(2;3]");
        assertTrue(a.precedes(b));
        a = RealInterval.valueOf("[;2)");
        b = RealInterval.valueOf("[3;3]");
        assertTrue(a.precedes(b));
        a = RealInterval.valueOf("[2;2]");
        b = RealInterval.valueOf("[3;3]");
        assertTrue(a.precedes(b));
        a = RealInterval.valueOf("[2;2]");
        b = RealInterval.valueOf("[;3]");
        assertFalse(a.precedes(b));
        a = RealInterval.valueOf("[2;2]");
        b = RealInterval.valueOf("(1;3]");
        assertFalse(a.precedes(b));
        a = RealInterval.INFINITY;
        b = RealInterval.valueOf("[3;3]");
        assertFalse(a.precedes(b));
        a = RealInterval.THE_EMPTY_INTERVAl;
        b = RealInterval.THE_EMPTY_INTERVAl;
        assertTrue(a.precedes(b));
        a = RealInterval.THE_EMPTY_INTERVAl;
        b = RealInterval.valueOf("[5;6)");
        assertTrue(a.precedes(b));
    }

    @Test
    public void testEquals() {
        assertFalse(RealInterval.THE_EMPTY_INTERVAl.equals(RealInterval.INFINITY));
        assertEquals(new RealInterval("[;)"), RealInterval.INFINITY);
    }
}
