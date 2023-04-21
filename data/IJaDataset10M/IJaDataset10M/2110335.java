package org.databene.commons.math;

import static org.junit.Assert.*;
import java.text.ParsePosition;
import org.databene.commons.ComparableComparator;
import org.databene.commons.comparator.IntComparator;
import org.junit.Test;

/**
 * Tests the {@link IntervalParser}.<br/><br/>
 * Created: 10.03.2011 16:04:54
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class IntervalParserTest {

    @Test
    public void testClosedInterval() {
        Interval<Integer> parsedInterval = parseInterval("[1,2]");
        assertEquals(new Interval<Integer>(1, true, 2, true, new ComparableComparator<Integer>()), parsedInterval);
    }

    @Test
    public void testOpenInterval() {
        Interval<Integer> parsedInterval = parseInterval("]1,2[");
        assertEquals(new Interval<Integer>(1, false, 2, false, new ComparableComparator<Integer>()), parsedInterval);
    }

    @Test
    public void testWhitespace() {
        Interval<Integer> parsedInterval = parseInterval(" [ 1 ,	2 ] ");
        assertEquals(new Interval<Integer>(1, true, 2, true, new ComparableComparator<Integer>()), parsedInterval);
    }

    private Interval<Integer> parseInterval(String text) {
        IntervalParser<Integer> parser = new IntervalParser<Integer>(new IntParser(), new IntComparator());
        return parser.parseObject(text, new ParsePosition(0));
    }
}
