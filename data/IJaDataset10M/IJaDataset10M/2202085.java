package backend.test.state.ships;

import backend.state.ships.Submarine;
import junit.framework.TestCase;

public class SubmarineTest extends TestCase {

    private Submarine sub;

    protected void setUp() throws Exception {
        sub = new Submarine();
    }

    public void testGetName() {
        assertEquals("Wrong ship name", "Submarine", sub.getName());
    }

    public void testGetSize() {
        assertEquals("Wrong ship size", 3, sub.getSize());
    }

    public void testGetSymbol() {
        assertEquals("Wrong ship symbol", 's', sub.getSymbol());
    }
}
