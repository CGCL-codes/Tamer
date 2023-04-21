package dioscuri.module.cpu;

/**
 * Intel opcode 9C<BR>
 * Transfer FLAGS register onto stack SS:SP.<BR>
 * Flags modified: none
 */
public class Instruction_PUSHF implements Instruction {

    private CPU cpu;

    /**
     * Class constructor
     */
    public Instruction_PUSHF() {
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_PUSHF(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Transfer FLAGS register onto stack top SS:SP
     */
    public void execute() {
        if (cpu.doubleWord) {
            cpu.setWordToStack(new byte[] { 0x00, 0x00 });
        }
        cpu.setWordToStack(Util.booleansToBytes(cpu.flags));
    }
}
