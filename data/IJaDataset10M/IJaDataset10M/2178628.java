package org.xnap.commons.io;

import junit.framework.TestCase;

public abstract class GenericProgressMonitorTest<T extends ProgressMonitor> extends TestCase {

    public GenericProgressMonitorTest() {
    }

    protected abstract T createProgressMonitor();

    protected abstract long getValue(T monitor);

    public void testGenericWorkNegative() {
        T monitor = createProgressMonitor();
        try {
            monitor.work(-1);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testGenericWorkDecrease() {
        T monitor = createProgressMonitor();
        monitor.setTotalSteps(10);
        monitor.work(5);
        try {
            monitor.setValue(4);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testGenericWork() {
        T monitor = createProgressMonitor();
        monitor.setTotalSteps(100);
        try {
            monitor.work(101);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
        monitor.work(10);
        assertEquals(10, getValue(monitor));
        monitor.work(1);
        assertEquals(11, getValue(monitor));
        monitor.work(0);
        assertEquals(11, getValue(monitor));
        try {
            monitor.work(-1);
            assertTrue("Expected exception", false);
        } catch (IllegalArgumentException e) {
        }
        monitor.work(89);
        assertEquals(100, getValue(monitor));
        try {
            monitor.work(1);
            assertTrue("Expected exception", false);
        } catch (IllegalArgumentException e) {
        }
    }

    public void testGenericSetValue() {
        T monitor = createProgressMonitor();
        monitor.setTotalSteps(100);
        try {
            monitor.setValue(101);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
        monitor.setValue(0);
        assertEquals(0, getValue(monitor));
        monitor.setValue(10);
        assertEquals(10, getValue(monitor));
        monitor.setValue(20);
        assertEquals(20, getValue(monitor));
        monitor.setValue(20);
        assertEquals(20, getValue(monitor));
        try {
            monitor.setValue(19);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
        try {
            monitor.setValue(-1);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
        monitor.setValue(100);
        assertEquals(100, getValue(monitor));
        try {
            monitor.setValue(101);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testGenericSetTotalSteps() {
        T monitor = createProgressMonitor();
        monitor.setTotalSteps(100);
        try {
            monitor.setTotalSteps(-1);
            fail("Expected exception");
        } catch (IllegalArgumentException e) {
        }
        monitor.setTotalSteps(0);
        monitor.setTotalSteps(Long.MAX_VALUE);
    }
}
