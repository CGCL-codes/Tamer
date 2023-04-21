package org.megadix.jfcm.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleDelayWeightedConnectionTest {

    @Test
    public void test_calculateOutput() {
        SimpleDelayWeightedConnection delay_0 = new SimpleDelayWeightedConnection("delay_0", null, 1.0, 0);
        SimpleDelayWeightedConnection delay_1 = new SimpleDelayWeightedConnection("delay_1", null, 1.0, 1);
        SimpleDelayWeightedConnection delay_10 = new SimpleDelayWeightedConnection("delay_10", null, 1.0, 10);
        DefaultConcept c1 = new DefaultConcept("c1", null, new LinearConceptActivator(), null, 1.0, true);
        LinearConceptActivator act = new LinearConceptActivator();
        DefaultConcept c2 = new DefaultConcept("c2", null, act, null, null, false);
        DefaultConcept c3 = new DefaultConcept("c3", null, act, null, null, false);
        DefaultConcept c4 = new DefaultConcept("c4", null, act, null, null, false);
        c1.connectOutputTo(delay_0);
        delay_0.connectOutputTo(c2);
        c1.connectOutputTo(delay_1);
        delay_1.connectOutputTo(c3);
        c1.connectOutputTo(delay_10);
        delay_10.connectOutputTo(c4);
        assertNull(delay_0.getOutput());
        assertNull(delay_1.getOutput());
        assertNull(delay_10.getOutput());
        c2.update();
        c3.update();
        c4.update();
        assertNotNull(delay_0.getOutput());
        assertNull(delay_1.getOutput());
        assertNull(delay_10.getOutput());
        c2.update();
        c3.update();
        c4.update();
        assertNotNull(delay_0.getOutput());
        assertNotNull(delay_1.getOutput());
        assertNull(delay_10.getOutput());
        for (int i = 0; i < 8; i++) {
            c2.update();
            c3.update();
            c4.update();
            assertNotNull(delay_0.getOutput());
            assertNotNull(delay_1.getOutput());
            assertNull(delay_10.getOutput());
        }
        c2.update();
        c3.update();
        c4.update();
        assertNotNull(delay_0.getOutput());
        assertNotNull(delay_1.getOutput());
        assertNotNull(delay_10.getOutput());
    }
}
