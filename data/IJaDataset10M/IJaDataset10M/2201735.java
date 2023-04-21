package emulator.hardware.nmos6502.commands;

import emulator.EmulatorException;
import emulator.hardware.HwByte;
import emulator.hardware.nmos6502.Command;
import emulator.hardware.nmos6502.CommandSet;
import emulator.hardware.nmos6502.Cpu6502;
import emulator.hardware.nmos6502.Flags6502;
import emulator.hardware.nmos6502.Operand;
import emulator.hardware.nmos6502.operands.*;

/** LDA (LoaD Accumulator)
 *  
 *  Affects Flags: S Z
 *  
 *  MODE           SYNTAX       HEX LEN TIM
 *  Immediate     LDA #$44      $A9  2   2
 *  Zero Page     LDA $44       $A5  2   3
 *  Zero Page,X   LDA $44,X     $B5  2   4
 *  Absolute      LDA $4400     $AD  3   4
 *  Absolute,X    LDA $4400,X   $BD  3   4+
 *  Absolute,Y    LDA $4400,Y   $B9  3   4+
 *  Indirect,X    LDA ($44,X)   $A1  2   6
 *  Indirect,Y    LDA ($44),Y   $B1  2   5+
 *  
 *  + add 1 cycle if page boundary crossed
 */
public class LDA implements Command {

    @Override
    public void execute(Cpu6502 cpu, Operand operand) throws EmulatorException {
        HwByte value = operand.getByte();
        Flags6502 flags = cpu.getFlags();
        flags.setNZFromValue(value);
        cpu.setAccu(value);
        cpu.setFlags(flags);
    }

    @Override
    public String getName() {
        return "LDA";
    }

    @Override
    public void register(CommandSet cset) {
        cset.defineCommand(0xA9, this, new Immediate());
        cset.defineCommand(0xA5, this, new ZeroPage());
        cset.defineCommand(0xB5, this, new ZeroPageX());
        cset.defineCommand(0xAD, this, new Absolute());
        cset.defineCommand(0xBD, this, new AbsoluteX());
        cset.defineCommand(0xB9, this, new AbsoluteY());
        cset.defineCommand(0xA1, this, new IndirectX());
        cset.defineCommand(0xB1, this, new IndirectY());
    }
}
