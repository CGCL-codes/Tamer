package emulator.hardware.nmos6502.commands;

import emulator.EmulatorException;
import emulator.hardware.nmos6502.Command;
import emulator.hardware.nmos6502.CommandSet;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Operand;
import emulator.hardware.nmos6502.operands.Absolute;
import emulator.hardware.nmos6502.operands.ZeroPage;
import emulator.hardware.nmos6502.operands.ZeroPageX;

/** STY (STore Y register)
 *  
 *  Affects Flags: none
 *  
 *  MODE           SYNTAX       HEX LEN TIM
 *  Zero Page     STY $44       $84  2   3
 *  Zero Page,X   STY $44,X     $94  2   4
 *  Absolute      STY $4400     $8C  3   4
 */
public class STY implements Command {

    @Override
    public void execute(Cpu6502 cpu, Operand operand) throws EmulatorException {
        operand.setByte(cpu.getY());
    }

    @Override
    public String getName() {
        return "STY";
    }

    @Override
    public void register(CommandSet cset) {
        cset.defineCommand(0x84, this, new ZeroPage());
        cset.defineCommand(0x94, this, new ZeroPageX());
        cset.defineCommand(0x8C, this, new Absolute());
    }
}
