package dioscuri.module.cpu;

/**
 * Intel opcode 1F<BR>
 * Pop word from stack SP:SS into general register DS.<BR>
 * Flags modified: none
 */
public class Instruction_POP_DS implements Instruction {

    private CPU cpu;

    /**
     * Class constructor
     */
    public Instruction_POP_DS() {
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_POP_DS(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * This pops the word at stack top SS:SP into DS
     */
    public void execute() {
        cpu.ds = cpu.getWordFromStack();
        if (cpu.doubleWord) {
            System.out.println("POP_DS: 32-bits not supported");
        }
    }
}
