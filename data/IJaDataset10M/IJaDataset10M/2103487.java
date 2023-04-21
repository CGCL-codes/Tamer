package com.obsidiandynamics.needle;

import junit.framework.*;
import org.junit.Test;
import com.obsidiandynamics.needle.type.*;

/**
 *  Unit tests for a <code>TimeInterval</code>.
 *  
 *  @author Emil Koutanov, Obsidian Dynamics.
 */
public final class TimeIntervalTester {

    @Test
    public void testOne() {
        assertInterval("1m", "1m");
    }

    @Test
    public void testMany() {
        assertInterval("1m 2h 3s 4ms 55d", "1m2h3s 4ms55d ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartWithUnit() {
        assertInterval("", "d 5s");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateUnit() {
        assertInterval("", "1d d 5s");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTrailingDuration() {
        assertInterval("", "1d 5s 3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUnit() {
        assertInterval("", "1x 5s");
    }

    private void assertInterval(String expected, String source) {
        final TimeInterval interval = TimeInterval.parseInterval(source);
        TestCase.assertEquals(expected, interval.toString());
    }
}
