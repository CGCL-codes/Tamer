package emulator.hardware.nmos6502.commands;

import emulator.EmulatorException;
import emulator.hardware.nmos6502.Command;
import emulator.hardware.nmos6502.CommandSet;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Operand;
import emulator.hardware.nmos6502.operands.*;

/** JMP (JuMP)
 *  
 *  Affects Flags: none
 *  
 *  MODE           SYNTAX       HEX LEN TIM
 *  Absolute      JMP $5597     $4C  3   3 
 *  Indirect      JMP ($5597)   $6C  3   5 
 *  
 *  JMP transfers program execution to the following address (absolute) or to
 *  the location contained in the following address (indirect).
 *  Note that there is no carry associated with the indirect jump so:
 *  
 *  AN INDIRECT JUMP MUST NEVER USE A VECTOR BEGINNING ON THE LAST BYTE OF A
 *  PAGE
 *  
 *  For example if address $3000 contains $40, $30FF contains $80, and $3100
 *  contains $50, the result of JMP ($30FF) will be a transfer of control to
 *  $4080 rather than $5080 as you intended i.e. the 6502 took the low byte of
 *  the address from $30FF and the high byte from $3000.
 */
public class JMP implements Command {

    @Override
    public void execute(Cpu6502 cpu, Operand operand) throws EmulatorException {
        cpu.setPC((int) operand.getWord().getNumber());
    }

    @Override
    public String getName() {
        return "JMP";
    }

    @Override
    public void register(CommandSet cset) {
        cset.defineCommand(0x4C, this, new ImmediateAddress());
        cset.defineCommand(0x6C, this, new IndirectAddress(), 4);
    }
}
