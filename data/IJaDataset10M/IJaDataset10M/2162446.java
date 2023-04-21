package emulator.unittest.hardware.nmos6502.timing;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import emulator.EmulatorException;

public class IndirectXTest {

    TimingTestBase test_base = null;

    @Before
    public void setUp() throws Exception {
        test_base = new TimingTestBase();
    }

    @Test
    public void testADC() throws EmulatorException {
        assertEquals(6, getCycles(0x61));
    }

    @Test
    public void testAND() throws EmulatorException {
        assertEquals(6, getCycles(0x21));
    }

    @Test
    public void testCMP() throws EmulatorException {
        assertEquals(6, getCycles(0xC1));
    }

    @Test
    public void testEOR() throws EmulatorException {
        assertEquals(6, getCycles(0x41));
    }

    @Test
    public void testLDA() throws EmulatorException {
        assertEquals(6, getCycles(0xA1));
    }

    @Test
    public void testORA() throws EmulatorException {
        assertEquals(6, getCycles(0x01));
    }

    @Test
    public void testSBC() throws EmulatorException {
        assertEquals(6, getCycles(0xE1));
    }

    @Test
    public void testSTA() throws EmulatorException {
        assertEquals(6, getCycles(0x81));
    }

    private int getCycles(int opcode) throws EmulatorException {
        test_base.setCommand(0, opcode, 0x44, 0);
        test_base.execute();
        return test_base.getElapsedCycles();
    }
}
