package emulator.unittest.hardware.nmos6502.commands;

import org.junit.Before;
import emulator.hardware.nmos6502.Flags6502;
import emulator.hardware.nmos6502.commands.BPL;

public class BPLTest extends BranchTestBase {

    @Before
    public void setUp() {
        branch_command = new BPL();
        bit = Flags6502.BIT_NEGATIVE;
        value = false;
    }
}
