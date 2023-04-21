package org.apache.harmony.luni.tests.java.lang;

import junit.framework.TestCase;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(Thread.State.class)
public class ThreadStateTest extends TestCase {

    Thread.State[] exStates = { Thread.State.NEW, Thread.State.RUNNABLE, Thread.State.BLOCKED, Thread.State.WAITING, Thread.State.TIMED_WAITING, Thread.State.TERMINATED };

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "valueOf", args = { java.lang.String.class })
    public void test_valueOfLString() {
        String[] spNames = { "NEW", "RUNNABLE", "BLOCKED", "WAITING", "TIMED_WAITING", "TERMINATED" };
        for (int i = 0; i < exStates.length; i++) {
            assertEquals(exStates[i], Thread.State.valueOf(spNames[i]));
        }
        String[] illegalNames = { "New", "new", "", "NAME", "TIME" };
        for (String s : illegalNames) {
            try {
                Thread.State.valueOf(s);
                fail("IllegalArgumentException was not thrown for string: " + s);
            } catch (IllegalArgumentException iae) {
            }
        }
    }

    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "values", args = {  })
    public void test_values() {
        Thread.State[] thStates = Thread.State.values();
        assertEquals(exStates.length, thStates.length);
        for (Thread.State ts : thStates) {
            assertTrue(isContain(ts));
        }
    }

    boolean isContain(Thread.State state) {
        for (Thread.State ts : exStates) {
            if (ts.equals(state)) return true;
        }
        return false;
    }
}
