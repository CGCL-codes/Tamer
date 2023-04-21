package physis.core.virtualmachine;

/**
 * It is the base class for all virtual machines, which executes the code tapes.
 * Subclasses of this class may represent concrete CPUs. They have to define their states with registers,
 * stacks or whatever.
 * This VM has its own stored executable program in its codetape.
 * The instructions are represented with integers, which are hardcoded in the main execute loop.
 * (The instruction names can be obtained from InstructionSet)
 * All the VMs should have corresponding instruction set.
 * All virtual machines should be computationally universal!
 * @see InstructionSet
 */
public abstract class VirtualMachine {

    /**
     * It should have one zero-argument constructor!
     * Because a separate factory instantiates them.
     */
    public VirtualMachine() {
    }

    /**
     * Calling this method forces the machine to execute the next instruction.
     * (Execution step by step).
     */
    public abstract void execute();

    /**
     * Calling this method forces te VM to execute the next N instructions.
     * @param number_of_instructions The exact number of the instructions to be executed.
     */
    public abstract void execute(int number_of_instructions);

    /**
     * Resets the virtual machine's state.
     */
    public abstract void reset();

    /**
     * Returns the String representation of the current state of the virtualmachine including all stacks registers
     * in the architecture and the instruction point by the instructionpointer: etc.
     * It's mainly used by the debuggers or tracers.
     */
    public abstract String getState();

    /**
     * Returns the descriptive information of the processor's instructionset and inner structure.
     */
    public abstract String getProcessorInformation();
}
