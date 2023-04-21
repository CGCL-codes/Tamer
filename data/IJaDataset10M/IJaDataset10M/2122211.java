package org.ancora.MicroblazeInterpreter.Instructions;

import org.ancora.MicroblazeInterpreter.Commons.BitOperations;
import org.ancora.MicroblazeInterpreter.Configuration.MbConfiguration;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor.MicroBlazeProcessor;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.MsrBit;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialRegister;
import org.ancora.MicroblazeInterpreter.Parser.InstructionParser;
import org.ancora.MicroblazeInterpreter.Parser.TraceData;

/**
 *  Implements the MicroBlaze Unconditional Branch Immediate.
 * 
 * <p> Includes the instructions bri, brai, brid, braid, brlid and bralid.
 *
 * @author Joao Bispo
 */
public class MbBri implements Instruction, Builder {

    /**
     * Constructor for using this object as a MbBuilder
     */
    public MbBri() {
        dBit = false;
        aBit = false;
        lBit = false;
        imm = -1;
        regD = -1;
        regs = null;
        lockReg = null;
        spr = null;
        config = null;
    }

    public Instruction build(TraceData data, MicroBlazeProcessor processor) {
        return new MbBri(data, processor);
    }

    /**
     * Constructor for using this object as a MbInstruction
     *
     * @param data parsed trace data
     * @param processor a MicroBlaze processor
     */
    public MbBri(TraceData data, MicroBlazeProcessor processor) {
        regs = processor.getRegisterFile();
        lockReg = processor.getLockRegister();
        spr = processor.getSpecialRegisters();
        config = processor.getConfiguration();
        imm = data.getImm();
        final boolean hasD = data.getOpName().contains(D_CHAR);
        if (hasD) {
            dBit = true;
        } else {
            dBit = false;
        }
        final boolean hasL = data.getOpName().contains(L_CHAR);
        if (hasL) {
            lBit = true;
            regD = InstructionParser.parseRegister(data.getR1());
        } else {
            lBit = false;
            regD = -1;
        }
        final boolean hasA = data.getOpName().contains(A_CHAR);
        if (hasA) {
            aBit = true;
        } else {
            aBit = false;
        }
    }

    /**
     * Executes the instruction
     */
    public void execute() {
        if (lBit) {
            regs.write(regD, spr.getPc());
        }
        int immediate = lockReg.processImmediate(imm);
        if (aBit) {
            spr.writePc(immediate);
        } else {
            int pc = spr.getPc();
            spr.writePc(pc + immediate);
        }
        if (config.C_USE_MMU() >= 1) {
            if (dBit & aBit & lBit & immediate == 0x8) {
                int bit;
                int msr = spr.read(SpecialRegister.rmsr);
                bit = BitOperations.getBit(MsrBit.UM.getPosition(), msr);
                msr = BitOperations.writeBit(MsrBit.UMS.getPosition(), bit, msr);
                bit = BitOperations.getBit(MsrBit.VM.getPosition(), msr);
                msr = BitOperations.writeBit(MsrBit.VMS.getPosition(), bit, msr);
                msr = BitOperations.clearBit(MsrBit.UM.getPosition(), msr);
                msr = BitOperations.clearBit(MsrBit.VM.getPosition(), msr);
            }
        }
    }

    public int latency() {
        if (dBit) {
            return 2;
        } else {
            return 3;
        }
    }

    public boolean isBranch() {
        return IS_BRANCH;
    }

    private final int regD;

    private final int imm;

    private final boolean dBit;

    private final boolean lBit;

    private final boolean aBit;

    private final RegisterFile regs;

    private final LockRegister lockReg;

    private final SpecialPurposeRegisters spr;

    private final MbConfiguration config;

    private final boolean IS_BRANCH = true;

    private final String D_CHAR = "d";

    private final String L_CHAR = "d";

    private final String A_CHAR = "d";
}
