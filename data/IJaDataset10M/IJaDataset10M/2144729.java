package emulator.hardware.nmos6502.commands;

import emulator.EmulatorException;
import emulator.hardware.nmos6502.Command;
import emulator.hardware.nmos6502.CommandSet;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Flags6502;
import emulator.hardware.nmos6502.Operand;
import emulator.hardware.nmos6502.operands.NoOperand;

public class RTI implements Command {

    @Override
    public void execute(Cpu6502 cpu, Operand operand) throws EmulatorException {
        cpu.setFlags(new Flags6502(cpu.popByte()));
        cpu.setPC(cpu.popAddress());
    }

    @Override
    public String getName() {
        return "RTI";
    }

    @Override
    public void register(CommandSet cset) {
        cset.defineCommand(0x40, this, new NoOperand(), 6);
    }
}
