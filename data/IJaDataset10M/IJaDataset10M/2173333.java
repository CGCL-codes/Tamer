package jmathx.model.interpolation;

import jmathx.model.interpolation.EqualIntervalFactory;
import java.math.BigDecimal;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import jmathx.model.RealInterval;
import jmathx.model.RealSet;

/**
 *
 * @author Nikolay Grozev
 */
public class EqualInterpolationIntervalCreatorTest extends TestCase {

    @Test
    public void testBreakIntervals() {
        RealSet domain = new RealSet("[-1;5]");
        BigDecimal start = BigDecimal.valueOf(-5.7);
        BigDecimal end = BigDecimal.valueOf(4.7);
        BigDecimal intervalSize = BigDecimal.valueOf(0.7);
        EqualIntervalFactory breaker = new EqualIntervalFactory(intervalSize);
        List<RealInterval> intervals = breaker.breakIntervals(domain, start, end);
        assertTrue(intervals.size() == 9);
        assertEquals(intervals.get(0), RealInterval.valueOf("[-1;-0.3]"));
        assertEquals(intervals.get(1), RealInterval.valueOf("[-0.3;0.4]"));
        assertEquals(intervals.get(2), RealInterval.valueOf("[0.4;1.1]"));
        assertEquals(intervals.get(3), RealInterval.valueOf("[1.1;1.8]"));
        assertEquals(intervals.get(4), RealInterval.valueOf("[1.8;2.5]"));
        assertEquals(intervals.get(5), RealInterval.valueOf("[2.5;3.2]"));
        assertEquals(intervals.get(6), RealInterval.valueOf("[3.2;3.9]"));
        assertEquals(intervals.get(7), RealInterval.valueOf("[3.9;4.6]"));
        assertEquals(intervals.get(8), RealInterval.valueOf("[4.6;4.7]"));
        domain = new RealSet("[;1)", "[2;6)");
        start = BigDecimal.valueOf(-5.7);
        end = BigDecimal.valueOf(6);
        intervalSize = BigDecimal.valueOf(1.5);
        breaker = new EqualIntervalFactory(intervalSize);
        intervals = breaker.breakIntervals(domain, start, end);
        assertTrue(intervals.size() == 8);
        assertEquals(intervals.get(0), RealInterval.valueOf("[-5.7;-4.2]"));
        assertEquals(intervals.get(1), RealInterval.valueOf("[-4.2;-2.7]"));
        assertEquals(intervals.get(2), RealInterval.valueOf("[-2.7;-1.2]"));
        assertEquals(intervals.get(3), RealInterval.valueOf("[-1.2;0.3]"));
        assertEquals(intervals.get(4), RealInterval.valueOf("[0.3;1)"));
        assertEquals(intervals.get(5), RealInterval.valueOf("[2;3.5]"));
        assertEquals(intervals.get(6), RealInterval.valueOf("[3.5;5]"));
        assertEquals(intervals.get(7), RealInterval.valueOf("[5;6)"));
        domain = new RealSet(RealInterval.THE_EMPTY_INTERVAl);
        start = BigDecimal.valueOf(-5.7);
        end = BigDecimal.valueOf(6);
        intervalSize = BigDecimal.valueOf(1.5);
        breaker = new EqualIntervalFactory(intervalSize);
        intervals = breaker.breakIntervals(domain, start, end);
        assertTrue(intervals.size() == 0);
    }
}
