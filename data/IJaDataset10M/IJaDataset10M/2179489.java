package emulator.unittest.hardware.nmos6502.commands;

import org.junit.Before;
import org.junit.Test;
import emulator.EmulatorException;
import emulator.hardware.nmos6502.Flags6502;
import emulator.hardware.nmos6502.commands.LDA;

public class LDATest extends CommandTestBase {

    @Before
    public void setUp() throws Exception {
        command = new LDA();
    }

    @Test
    public void testLoad() throws EmulatorException {
        executeAndCheckCommandOnAccu(command, 0, 0, 0x3f, 0, 0x3F);
    }

    @Test
    public void testLoadZero() throws EmulatorException {
        executeAndCheckCommandOnAccu(command, 0, 0x3F, 0, Flags6502.FLAG_ZERO, 0);
    }

    @Test
    public void testLoadNegative() throws EmulatorException {
        executeAndCheckCommandOnAccu(command, 0, 0x3F, 0x9F, Flags6502.FLAG_NEGATIVE, 0x9F);
    }
}
