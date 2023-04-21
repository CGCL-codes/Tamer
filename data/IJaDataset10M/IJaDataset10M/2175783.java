package org.ascape.test.model.space;

import junit.framework.TestCase;
import org.ascape.model.Cell;
import org.ascape.model.Scape;
import org.ascape.model.space.Array1D;
import org.ascape.util.Conditional;

public class Array1DBaseTest extends TestCase {

    public Array1DBaseTest(String name) {
        super(name);
    }

    class TestCell extends Cell {

        public boolean testState;

        public void initialize() {
            testState = false;
        }
    }

    Scape vn = new Scape(new Array1D());

    static final Conditional TEST_STATE = new Conditional() {

        public boolean meetsCondition(Object o) {
            return ((TestCell) o).testState;
        }
    };

    public void testfindNearest() {
        vn = new Scape(new Array1D());
        vn.setExtent(101);
        vn.setPrototypeAgent(new TestCell());
        vn.createScape();
        vn.initialize();
        ((TestCell) vn.get(10)).testState = true;
        ((TestCell) vn.get(11)).testState = true;
        assertTrue(((Cell) vn.get(13)).findNearest(TEST_STATE, false, Double.MAX_VALUE) == vn.get(11));
        assertTrue(((Cell) vn.get(1)).findNearest(TEST_STATE, false, Double.MAX_VALUE) == vn.get(10));
        assertTrue(((Cell) vn.get(60)).findNearest(TEST_STATE, false, Double.MAX_VALUE) == vn.get(11));
        assertTrue(((Cell) vn.get(62)).findNearest(TEST_STATE, false, Double.MAX_VALUE) == vn.get(10));
        vn.setPeriodic(false);
        assertTrue(((Cell) vn.get(13)).findNearest(TEST_STATE, false, Double.MAX_VALUE) == vn.get(11));
        assertTrue(((Cell) vn.get(1)).findNearest(TEST_STATE, false, Double.MAX_VALUE) == vn.get(10));
        assertTrue(((Cell) vn.get(60)).findNearest(TEST_STATE, false, Double.MAX_VALUE) == vn.get(11));
        assertTrue(((Cell) vn.get(62)).findNearest(TEST_STATE, false, Double.MAX_VALUE) == vn.get(11));
        assertTrue(((Cell) vn.get(99)).findNearest(TEST_STATE, false, Double.MAX_VALUE) == vn.get(11));
    }
}
