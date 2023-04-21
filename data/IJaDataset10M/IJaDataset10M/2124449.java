package org.alcibiade.asciiart.widget.metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TextMetricsTest {

    @Test
    public void testZero() {
        assertEquals(new TextMetrics(0, 0), new TextMetrics());
        assertEquals(new TextMetrics(0, 0), new TextMetrics(""));
    }

    @Test
    public void testText() {
        assertEquals(new TextMetrics(4, 0), new TextMetrics("abcd"));
        assertEquals(new TextMetrics(11, 0), new TextMetrics("command.com"));
        assertEquals(new TextMetrics(5, 0), new TextMetrics("1.2.3"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(TextMetrics.isNumeric("2"));
        assertTrue(TextMetrics.isNumeric("2."));
        assertTrue(TextMetrics.isNumeric("2.2"));
        assertFalse(TextMetrics.isNumeric("2.2a"));
        assertFalse(TextMetrics.isNumeric("a"));
        assertFalse(TextMetrics.isNumeric(" 2"));
        assertFalse(TextMetrics.isNumeric(" 2."));
        assertFalse(TextMetrics.isNumeric(" 2.2"));
        assertFalse(TextMetrics.isNumeric("2 "));
        assertFalse(TextMetrics.isNumeric("2. "));
        assertFalse(TextMetrics.isNumeric("2.2 "));
    }
}
