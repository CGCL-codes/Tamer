package emulator.hardware.nmos6502.commands;

import emulator.EmulatorException;
import emulator.hardware.nmos6502.Command;
import emulator.hardware.nmos6502.CommandSet;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Flags6502;
import emulator.hardware.nmos6502.Operand;
import emulator.hardware.nmos6502.operands.Displacement;

/** BNE (Branch on Not Equal) */
public class BNE implements Command {

    @Override
    public void execute(Cpu6502 cpu, Operand operand) throws EmulatorException {
        if (!cpu.getFlags().getZero()) cpu.branch(operand.getByte());
    }

    @Override
    public String getName() {
        return "BNE";
    }

    @Override
    public void register(CommandSet cset) {
        cset.defineCommand(0xD0, this, new Displacement(Flags6502.BIT_ZERO, false));
    }
}
