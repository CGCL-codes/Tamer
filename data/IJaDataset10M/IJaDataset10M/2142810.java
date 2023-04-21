package dioscuri.module.cpu;

/**
 * Intel opcode F8<BR>
 * Clear carry flag.<BR>
 * Set CF to 0<BR>
 * Flags modified: none
 */
public class Instruction_CLC implements Instruction {

    private CPU cpu;

    /**
     * Class constructor
     */
    public Instruction_CLC() {
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_CLC(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Set CF to 0
     */
    public void execute() {
        cpu.flags[CPU.REGISTER_FLAGS_CF] = false;
    }
}
