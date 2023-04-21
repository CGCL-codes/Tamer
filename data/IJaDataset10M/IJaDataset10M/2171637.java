package org.ancora.MicroblazeInterpreter.Instructions;

import org.ancora.MicroblazeInterpreter.Commons.BitOperations;
import org.ancora.MicroblazeInterpreter.Configuration.MbConfiguration;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Shift Right Arithmetic.
 * 
 * <p> Includes the instruction sra.
 *
 * @author Joao Bispo
 */
public class MbSra implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbSra() {
        regA = -1;
        regD = -1;
        regs = null;
        spr = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbSra(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbSra(TraceData data, MicroBlazeProcessor processor) {
        regs = processor.getRegisterFile();
        spr = processor.getSpecialRegisters();
        regA = InstructionParser.parseRegister(data.getR2());
        regD = InstructionParser.parseRegister(data.getR1());
    }

    /**
     * Executes the instruction
     */
    public void execute() {
        int rA = regs.read(regA);
        int lsbBit = BitOperations.getBit(0, rA);
        spr.writeCarryBit(lsbBit);
        int rD = rA >> 1;
        regs.write(regD, rD);
    }

    public int latency() {
        return LATENCY;
    }

    public boolean isBranch() {
        return IS_BRANCH;
    }

    private final int regA;

    private final int regD;

    private final RegisterFile regs;

    private final SpecialPurposeRegisters spr;

    private final boolean IS_BRANCH = false;

    private final int LATENCY = 1;
}
