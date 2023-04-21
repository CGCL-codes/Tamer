package com.uberrated;

import com.es.components.generic.ValueChange;
import com.es.components.generic.ValueComponent;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA. User: snorre Date: 16.07.11 Time: 08.12 To change this template use File | Settings | File Templates.
 */
public class ValueChangeTest {

    private static final int DELTA = 20;

    private static final double error = 0.001;

    private ValueComponent value;

    private ValueChange changer;

    @Before
    public void setUp() throws Exception {
        value = new ValueComponent() {
        };
        changer = new ValueChange() {
        };
        changer.setChange(1);
        changer.setPeriod(DELTA);
    }

    @Test
    public void testTickOneStep() throws Exception {
        tick();
        assertValue(1.0f);
    }

    @Test
    public void testTickOneStepRandom() throws Exception {
        changer.setChangeSpread(1);
        tick();
        assertTrue(value.getValue() != 0.0f && value.getValue() != 1.0f);
    }

    @Test
    public void testTick3Step() throws Exception {
        tick(3);
        assertValue(3.0f);
    }

    @Test
    public void testTickDuration() throws Exception {
        changer.setDuration(100);
        tick(10);
        assertValue(5.0f);
    }

    @Test
    public void testTickDelay() throws Exception {
        changer.setDuration(100);
        changer.setDelay(40);
        tick(10);
        assertValue(3.0f);
    }

    @Test
    public void testTickLimit() throws Exception {
        changer.setLimit(1);
        tick(10);
        assertValue(1.0f);
    }

    @Test
    public void testTickRepeat() throws Exception {
        changer.setLimit(1);
        changer.setRepeats(3);
        tick(10);
        assertValue(4.0f);
    }

    @Test
    public void testTickNegativeRepeats() throws Exception {
        changer.setLimit(1);
        changer.setRepeats(-1);
        tick(10);
        assertValue(10.0f);
    }

    private void assertValue(float expected) {
        assertEquals(expected, value.getValue(), error);
    }

    private void tick() {
        tick(1);
    }

    private void tick(int ticks) {
        for (int i = 0; i < ticks; i++) {
            changer.tick(value, DELTA);
        }
    }
}
