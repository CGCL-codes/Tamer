package emulator.unittest.hardware.nmos6502.commands;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import emulator.EmulatorException;
import emulator.hardware.HwByte;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Flags6502;
import emulator.hardware.nmos6502.commands.DEX;
import emulator.hardware.nmos6502.operands.NoOperand;

public class DEXTest {

    @Test
    public void testDecPositive() throws EmulatorException {
        executeAndCheckDEX(0x3F, 0);
    }

    @Test
    public void testDecNegative() throws EmulatorException {
        executeAndCheckDEX(0x00, Flags6502.FLAG_NEGATIVE);
    }

    @Test
    public void testDecZero() throws EmulatorException {
        executeAndCheckDEX(0x01, Flags6502.FLAG_ZERO);
    }

    private void executeAndCheckDEX(int val, int expected_flags) throws EmulatorException {
        Cpu6502 cpu = new Cpu6502();
        cpu.setX(new HwByte(val));
        new DEX().execute(cpu, new NoOperand());
        assertEquals(new HwByte(val - 1), cpu.getX());
        assertEquals(new Flags6502(expected_flags), cpu.getFlags());
    }
}
