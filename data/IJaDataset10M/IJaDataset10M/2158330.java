package org.ancora.MicroblazeInterpreter.HardwareBlocks.Processor;

import org.ancora.MicroblazeInterpreter.Configuration.MbConfiguration;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.DataMemory.DataMemoryPlus;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.InstructionMemory.InstructionMemory;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.LockRegister;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.RegisterFile;
import org.ancora.MicroblazeInterpreter.HardwareBlocks.Registers.SpecialPurposeRegisters;

/**
 * Represents a MicroBlaze Processor which runs trace files.
 *
 * @author Joao Bispo
 */
public interface MicroBlazeProcessor {

    /**
     * Runs the trace.
     */
    public void run();

    /**
    * Access to the Instruction Memory.
    *
    * @return
    */
    public InstructionMemory getInstructionMemory();

    /**
    * Access to the Special Purpose Registers.
    * 
    * @return
    */
    public SpecialPurposeRegisters getSpecialRegisters();

    /**
    * Access to the General Purpose Register File.
    *
    * @return
    */
    public RegisterFile getRegisterFile();

    /**
    * Access to the Lock Register
    *
    * @return
    */
    public LockRegister getLockRegister();

    /**
    * Access to the Clock
    * 
    * @return
    */
    public Clock getClock();

    /**
    * Access to the Data Memory.
    * 
    * @return
    */
    public DataMemoryPlus getDataMemory();

    /**
    * Access to MPD Parameters.
    *
    * @return
    */
    public MbConfiguration getConfiguration();
}
