package org.jdesktop.animation.timing.triggers;

import static org.junit.Assert.*;
import org.junit.*;

public final class FocusTriggerEventTest {

    @Test
    public void testGetOppositeEvent() {
        assertSame(FocusTriggerEvent.OUT, FocusTriggerEvent.IN.getOppositeEvent());
        assertSame(FocusTriggerEvent.IN, FocusTriggerEvent.OUT.getOppositeEvent());
    }
}
