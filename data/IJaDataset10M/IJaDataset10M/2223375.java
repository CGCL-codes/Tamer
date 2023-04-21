package org.nakedobjects.plugins.xml.objectstore.internal.clock;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.nakedobjects.runtime.testsystem.ProxyJunit4TestCase;

public class DefaultClockTest extends ProxyJunit4TestCase {

    DefaultClock clock;

    @Test
    public void testGetTime() {
        clock = new DefaultClock();
        assertTrue(clock.getTime() > 0);
    }
}
