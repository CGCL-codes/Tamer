package emulator.hardware.nmos6502.commands;

import emulator.EmulatorException;
import emulator.hardware.HwByte;
import emulator.hardware.nmos6502.Command;
import emulator.hardware.nmos6502.CommandSet;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Flags6502;
import emulator.hardware.nmos6502.Operand;
import emulator.hardware.nmos6502.operands.*;

/** BIT (test BITs)
 *  
 *  Affects Flags: N V Z
 *  
 *  MODE           SYNTAX       HEX LEN TIM
 *  Zero Page     BIT $44       $24  2   3
 *  Absolute      BIT $4400     $2C  3   4
 *  
 *  BIT sets the Z flag as though the value in the address tested were ANDed
 *  with the accumulator. The S and V flags are set to match bits 7 and 6
 *  respectively in the value stored at the tested address.
 *  
 *  BIT is often used to skip one or two following bytes as in:
 *  
 *  CLOSE1 LDX #$10   If entered here, we
 *         .BYTE $2C  effectively perform
 *  CLOSE2 LDX #$20   a BIT test on $20A2,
 *         .BYTE $2C  another one on $30A2,
 *  CLOSE3 LDX #$30   and end up with the X
 *  CLOSEX LDA #12    register still at $10
 *         STA ICCOM,X upon arrival here.
 */
public class BIT implements Command {

    @Override
    public void execute(Cpu6502 cpu, Operand operand) throws EmulatorException {
        HwByte value = operand.getByte();
        Flags6502 flags = cpu.getFlags();
        flags.setNegative(value.getBit(7));
        flags.setOverflow(value.getBit(6));
        value.and(cpu.getAccu());
        flags.setZero(value.isZero());
        cpu.setFlags(flags);
    }

    @Override
    public String getName() {
        return "BIT";
    }

    @Override
    public void register(CommandSet cset) {
        cset.defineCommand(0x24, this, new ZeroPage());
        cset.defineCommand(0x2C, this, new Absolute());
    }
}
