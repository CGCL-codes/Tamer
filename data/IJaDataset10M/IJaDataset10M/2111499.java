package nl.kbna.dioscuri.module.cpu;

/**
 * Intel opcode F9<BR>
 * Set carry flag.<BR>
 * Set CF to 1<BR>
 * Flags modified: none
 */
public class Instruction_STC implements Instruction {

    private CPU cpu;

    /**
     * Class constructor
     */
    public Instruction_STC() {
    }

    /**
     * Class constructor specifying processor reference
     * 
     * @param processor Reference to CPU class
     */
    public Instruction_STC(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Set CF to 1
     */
    public void execute() {
        cpu.flags[CPU.REGISTER_FLAGS_CF] = true;
    }
}
