package dioscuri.module.cpu;

/**
 * Intel opcode 7F<BR>
 * Conditional short jump if not zero and sign == overflow.<BR>
 * Displacement is relative to next instruction.<BR>
 * Flags modified: none
 */
public class Instruction_JNLE_JG implements Instruction {

    private CPU cpu;

    byte displacement;

    /**
     * Class constructor
     */
    public Instruction_JNLE_JG() {
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_JNLE_JG(CPU processor) {
        cpu = processor;
    }

    /**
     * Execute conditional short jump if not zero and sign == overflow
     */
    public void execute() {
        displacement = (byte) cpu.getByteFromCode();
        if ((!cpu.flags[CPU.REGISTER_FLAGS_ZF]) && (cpu.flags[CPU.REGISTER_FLAGS_SF] == cpu.flags[CPU.REGISTER_FLAGS_OF])) {
            cpu.ip = Util.addWords(cpu.ip, new byte[] { Util.signExtend(displacement), displacement }, 0);
        }
    }
}
