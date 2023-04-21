package vehikel.protocol.serial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import vehikel.protocol.serial.LibRxTxConsistencyCheck;

public class LibRxTxConsistencyCheckTest {

    @Test
    public void testCheck() {
        assertTrue(new LibRxTxConsistencyCheck().check());
    }

    @Test
    public void testGetMessage() {
        LibRxTxConsistencyCheck checker = new LibRxTxConsistencyCheck();
        checker.check();
        String msg1 = checker.getMessage();
        assertNotNull(msg1);
        assertTrue(msg1.endsWith(": ok"));
        checker.check();
        String msg2 = checker.getMessage();
        assertNotNull(msg2);
        assertEquals(msg1, msg2);
    }

    @Test
    public void testState2() {
    }

    @Test
    public void testState3() {
    }
}
