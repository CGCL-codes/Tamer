package com.traxel.lumbermill.event;

import java.util.prefs.Preferences;
import com.traxel.Context;
import com.traxel.LumbermillTest;
import ucar.util.prefs.PreferencesExt;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class ColumnSetTest extends TestCase {

    private ColumnSet _set;

    private Column _column;

    public void setUp() {
        _set = new ColumnSet(Context.getColumnsPreferences());
    }

    public void tearDown() {
        _set = null;
        _column = null;
    }

    public void testTest() {
        assertTrue(getClass() + " is running.", true);
    }

    public void testGet() {
        assertEquals(_set.SEVERITY, _set.getColumn(0));
        assertEquals(_set.ELAPSED_TIME, _set.getColumn(1));
        assertEquals(_set.HAS_THROWN, _set.getColumn(2));
        assertEquals(_set.SOURCE, _set.getColumn(3));
        assertEquals(_set.MESSAGE, _set.getColumn(4));
    }

    public void testSeverity() {
        _column = _set.SEVERITY;
        assertEquals(Accessor.SEVERITY, _column.getAccessor());
    }

    public void testTimestamp() {
        _column = _set.TIMESTAMP;
        assertEquals(Accessor.TIMESTAMP, _column.getAccessor());
    }

    public void testElapsedTime() {
        _column = _set.ELAPSED_TIME;
        assertEquals(Accessor.ELAPSED_TIME, _column.getAccessor());
    }

    public void testHasThrown() {
        _column = _set.HAS_THROWN;
        assertEquals(Accessor.HAS_THROWN, _column.getAccessor());
    }

    public void testShortSource() {
        _column = _set.SHORT_SOURCE;
        assertEquals(Accessor.SHORT_SOURCE, _column.getAccessor());
    }

    public void testSource() {
        _column = _set.SOURCE;
        assertEquals(Accessor.SOURCE, _column.getAccessor());
    }

    public void testMessage() {
        _column = _set.MESSAGE;
        assertEquals(Accessor.MESSAGE, _column.getAccessor());
    }

    public void testNDC() {
        _column = _set.NDC;
        assertEquals(Accessor.NDC, _column.getAccessor());
    }

    public static Test suite() {
        return new TestSuite(ColumnSetTest.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
}
