package dioscuri.module.cpu;

/**
 * Intel opcode 57<BR>
 * Push general register DI onto stack SS:SP.<BR>
 * Flags modified: none
 */
public class Instruction_PUSH_DI implements Instruction {

    private CPU cpu;

    /**
     * Class constructor
     */
    public Instruction_PUSH_DI() {
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_PUSH_DI(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * This pushes the word in DI onto stack top SS:SP
     */
    public void execute() {
        if (cpu.doubleWord) {
            cpu.setWordToStack(cpu.edi);
        }
        cpu.setWordToStack(cpu.di);
    }
}
