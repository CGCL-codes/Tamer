package nl.kbna.dioscuri.module.cpu;

/**
	 * Intel opcode 93<BR>
	 * Exchange contents of registers BX and AX.<BR>
	 * Flags modified: none
	 */
public class Instruction_XCHG_BXAX implements Instruction {

    private CPU cpu;

    byte[] temp;

    /**
	 * Class constructor
	 * 
	 */
    public Instruction_XCHG_BXAX() {
    }

    /**
	 * Class constructor specifying processor reference
	 * 
	 * @param processor	Reference to CPU class
	 */
    public Instruction_XCHG_BXAX(CPU processor) {
        cpu = processor;
        temp = new byte[2];
    }

    /**
	 * Execute instruction
	 */
    public void execute() {
        temp = cpu.bx;
        cpu.bx = cpu.ax;
        cpu.ax = temp;
    }
}
