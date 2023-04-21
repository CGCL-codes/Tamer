package dioscuri.module.cpu;

/**
 * Intel opcode 9E<BR>
 * Load the FLAGS register with values from AH register. <BR>
 * The FLAGS register is written as SF:ZF:0:AF:0:PF:1:CF. <BR>
 * Flags modified: SF, ZF, AF, PF, CF.
 */
public class Instruction_SAHF implements Instruction {

    private CPU cpu;

    boolean[] tempFlags = new boolean[8];

    /**
     * Class constructor
     */
    public Instruction_SAHF() {
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_SAHF(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Move AH register into low byte of FLAGS register.
     */
    public void execute() {
        tempFlags = Util.bytesToBooleans(new byte[] { cpu.ax[CPU.REGISTER_GENERAL_HIGH] });
        System.arraycopy(tempFlags, 0, cpu.flags, 0, tempFlags.length);
    }
}
