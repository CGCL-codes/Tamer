package com.mgensystems.util;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * <b>Title:</b> <br />
 * <b>Description:</b> <br />
 * <b>Changes:</b><li></li>
 * @author raykroeker@gmail.com
 * @version 2009.1
 */
public final class AutoTimeFormatTest {

    /** The format to test. */
    private TimeFormat format;

    /**
	 * Create AutoTimeFormatTest.
	 * 
	 */
    public AutoTimeFormatTest() {
        super();
    }

    /**
	 * Setup the test.
	 * 
	 */
    @Before
    public void before() {
        format = new TimeFormat();
    }

    /**
	 * Test days.
	 * 
	 */
    @Test
    public void days() {
        assertEquals("Time format does not match expectation.", "1.0 " + TimeFormat.Unit.DAYS.name(), format.format(TimeFormat.Unit.AUTO, 1000L * 60L * 60L * 24L + 1L));
        assertEquals("Time format does not match expectation.", "6.0 " + TimeFormat.Unit.DAYS.name(), format.format(TimeFormat.Unit.AUTO, 1000L * 60L * 60L * 24L * 6L));
    }

    /**
	 * Test hours.
	 * 
	 */
    @Test
    public void hours() {
        assertEquals("Time format does not match expectation.", "1.0 " + TimeFormat.Unit.HOURS.name(), format.format(TimeFormat.Unit.AUTO, 1000L * 60L * 60L + 1L));
        assertEquals("Time format does not match expectation.", "23.0 " + TimeFormat.Unit.HOURS.name(), format.format(TimeFormat.Unit.AUTO, 1000L * 60L * 60L * 23L));
    }

    /**
	 * Test milliseconds.
	 * 
	 */
    @Test
    public void milliseconds() {
        assertEquals("Time format does not match expectation.", "1 " + TimeFormat.Unit.MILLISECONDS.name(), format.format(TimeFormat.Unit.AUTO, 1L));
        assertEquals("Time format does not match expectation.", "999 " + TimeFormat.Unit.MILLISECONDS.name(), format.format(TimeFormat.Unit.AUTO, 999L));
    }

    /**
	 * Test minutes.
	 * 
	 */
    @Test
    public void minutes() {
        assertEquals("Time format does not match expectation.", "1.0 " + TimeFormat.Unit.MINUTES.name(), format.format(TimeFormat.Unit.AUTO, 1000L * 60L + 1L));
        assertEquals("Time format does not match expectation.", "59.0 " + TimeFormat.Unit.MINUTES.name(), format.format(TimeFormat.Unit.AUTO, 1000L * 60L * 59L));
    }

    /**
	 * Test seconds.
	 * 
	 */
    @Test
    public void seconds() {
        assertEquals("Time format does not match expectation.", "1.0 " + TimeFormat.Unit.SECONDS.name(), format.format(TimeFormat.Unit.AUTO, 1000L + 1L));
        assertEquals("Time format does not match expectation.", "59.0 " + TimeFormat.Unit.SECONDS.name(), format.format(TimeFormat.Unit.AUTO, 1000L * 59L));
    }

    /**
	 * Test weeks.
	 * 
	 */
    @Test
    public void weeks() {
        assertEquals("Time format does not match expectation.", "1.0 " + TimeFormat.Unit.WEEKS.name(), format.format(TimeFormat.Unit.AUTO, 1000L * 60L * 60L * 24L * 7L + 1L));
        assertEquals("Time format does not match expectation.", "52.0 " + TimeFormat.Unit.WEEKS.name(), format.format(TimeFormat.Unit.AUTO, 1000L * 60L * 60L * 24L * 7L * 52L));
        assertEquals("Time format does not match expectation.", "15250284544.0 " + TimeFormat.Unit.WEEKS.name(), format.format(TimeFormat.Unit.AUTO, Long.MAX_VALUE));
    }
}
