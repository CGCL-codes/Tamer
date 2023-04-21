package com.gempukku.animator.function.timeline;

import static org.junit.Assert.*;
import org.junit.*;

public class SimpleTimelineFunctionTest {

    @Test
    public void timeNotPasses() throws Exception {
        SimpleTimelineFunction simpleTimelineFunction = new SimpleTimelineFunction();
        assertEquals(0, simpleTimelineFunction.getLocalTime(0));
        assertEquals(0, simpleTimelineFunction.getLocalTime(10));
        assertEquals(0, simpleTimelineFunction.getLocalTime(1000));
    }

    @Test
    public void timePassesWhenStarted() throws Exception {
        SimpleTimelineFunction simpleTimelineFunction = new SimpleTimelineFunction();
        assertEquals(0, simpleTimelineFunction.getLocalTime(0));
        assertEquals(0, simpleTimelineFunction.getLocalTime(10));
        assertEquals(0, simpleTimelineFunction.getLocalTime(999));
        simpleTimelineFunction.setSpeed(1);
        assertEquals(0, simpleTimelineFunction.getLocalTime(1000));
        assertEquals(10, simpleTimelineFunction.getLocalTime(1010));
        assertEquals(100, simpleTimelineFunction.getLocalTime(1100));
        assertEquals(1235, simpleTimelineFunction.getLocalTime(2235));
    }

    @Test
    public void speed() throws Exception {
        SimpleTimelineFunction simpleTimelineFunction = new SimpleTimelineFunction();
        assertEquals(0, simpleTimelineFunction.getLocalTime(0));
        assertEquals(0, simpleTimelineFunction.getLocalTime(10));
        assertEquals(0, simpleTimelineFunction.getLocalTime(999));
        simpleTimelineFunction.setSpeed(2);
        assertEquals(0, simpleTimelineFunction.getLocalTime(1000));
        assertEquals(20, simpleTimelineFunction.getLocalTime(1010));
        assertEquals(200, simpleTimelineFunction.getLocalTime(1100));
        assertEquals(2470, simpleTimelineFunction.getLocalTime(2235));
        simpleTimelineFunction.setSpeed(-1);
        assertEquals(3000, simpleTimelineFunction.getLocalTime(2500));
        assertEquals(2800, simpleTimelineFunction.getLocalTime(2700));
        assertEquals(2200, simpleTimelineFunction.getLocalTime(3300));
        assertEquals(0, simpleTimelineFunction.getLocalTime(5500));
    }

    @Test
    public void setTime() throws Exception {
        SimpleTimelineFunction simpleTimelineFunction = new SimpleTimelineFunction();
        assertEquals(0, simpleTimelineFunction.getLocalTime(0));
        assertEquals(0, simpleTimelineFunction.getLocalTime(10));
        assertEquals(0, simpleTimelineFunction.getLocalTime(999));
        simpleTimelineFunction.setTime(1000);
        assertEquals(1000, simpleTimelineFunction.getLocalTime(1000));
        assertEquals(1000, simpleTimelineFunction.getLocalTime(1001));
        assertEquals(1000, simpleTimelineFunction.getLocalTime(1050));
        assertEquals(1000, simpleTimelineFunction.getLocalTime(1999));
    }
}
