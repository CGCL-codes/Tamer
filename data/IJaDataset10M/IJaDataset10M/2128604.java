package emulator.hardware.nmos6502.commands;

import emulator.EmulatorException;
import emulator.hardware.HwByte;
import emulator.hardware.nmos6502.Command;
import emulator.hardware.nmos6502.CommandSet;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Flags6502;
import emulator.hardware.nmos6502.Operand;
import emulator.hardware.nmos6502.operands.NoOperand;

/** PLA (PuLl Accumulator) */
public class PLA implements Command {

    @Override
    public void execute(Cpu6502 cpu, Operand operand) throws EmulatorException {
        HwByte value = new HwByte(cpu.popByte());
        Flags6502 flags = cpu.getFlags();
        flags.setNZFromValue(value);
        cpu.setAccu(value);
        cpu.setFlags(flags);
    }

    @Override
    public String getName() {
        return "PLA";
    }

    @Override
    public void register(CommandSet cset) {
        cset.defineCommand(0x68, this, new NoOperand(), 4);
    }
}
