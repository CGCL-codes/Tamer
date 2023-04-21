package nl.kbna.dioscuri.module.cpu;

/**
	 * Intel opcode AB<BR>
	 * Copy word from register AX to ES:DI; update DI register according to DF.<BR>
	 * Flags modified: none
	 */
public class Instruction_STOSW_YvAX implements Instruction {

    private CPU cpu;

    /**
	 * Class constructor
	 * 
	 */
    public Instruction_STOSW_YvAX() {
    }

    /**
	 * Class constructor specifying processor reference
	 * 
	 * @param processor	Reference to CPU class
	 */
    public Instruction_STOSW_YvAX(CPU processor) {
        this();
        cpu = processor;
    }

    /**
	 * Copy word from register AX to ES:DI; update DI register according to flag DF
	 */
    public void execute() {
        cpu.setWordToExtra(cpu.di, cpu.ax);
        if (cpu.flags[CPU.REGISTER_FLAGS_DF]) {
            cpu.di[CPU.REGISTER_GENERAL_LOW] -= 2;
            if (cpu.di[CPU.REGISTER_GENERAL_LOW] == -1 || cpu.di[CPU.REGISTER_GENERAL_LOW] == -2) {
                cpu.di[CPU.REGISTER_GENERAL_HIGH]--;
            }
        } else {
            cpu.di[CPU.REGISTER_GENERAL_LOW] += 2;
            if (cpu.di[CPU.REGISTER_GENERAL_LOW] == 0 || cpu.di[CPU.REGISTER_GENERAL_LOW] == 1) {
                cpu.di[CPU.REGISTER_GENERAL_HIGH]++;
            }
        }
    }
}
