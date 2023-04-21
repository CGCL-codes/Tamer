package org.happy.commons.ver1x1.patterns.observer.event;

import static org.junit.Assert.*;
import org.happy.commons.patterns.observer.event.ActionEventBefore_1x0;
import org.junit.Test;

public class ActionEventBefore_1x0Test {

    @Test
    public void testActionEventBefore_1x0ObjectIntStringT() {
        ActionEventBefore_1x0<Boolean> a = new ActionEventBefore_1x0<Boolean>(this, 12, "test", true);
        assertEquals(this, a.getSource());
        assertEquals(12, a.getID());
        assertEquals("test", a.getActionCommand());
        assertTrue(a.getData());
        a.setData(false);
        assertFalse(a.getData());
        assertFalse(a.isCanceled());
        a.setCanceled(true);
        assertTrue(a.isCanceled());
    }
}
