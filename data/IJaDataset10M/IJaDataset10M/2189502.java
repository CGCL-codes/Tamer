package dioscuri.module.cpu;

import dioscuri.exception.CPUInstructionException;
import java.util.logging.Logger;

/**
 * @author Bram Lohman
 * @author Bart Kiers
 */
@SuppressWarnings("unused")
public class Instruction_NULL implements Instruction {

    private CPU cpu;

    private static final Logger logger = Logger.getLogger(Instruction_NULL.class.getName());

    /**
     * Construct class
     */
    public Instruction_NULL() {
    }

    /**
     * Construct class
     *
     * @param processor
     */
    public Instruction_NULL(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Execute instruction
     *
     * @throws CPUInstructionException
     */
    public void execute() throws CPUInstructionException {
        throw new CPUInstructionException("Unknown instruction (NULL) encountered at " + cpu.getRegisterHex(0) + ":" + cpu.getRegisterHex(1));
    }
}
