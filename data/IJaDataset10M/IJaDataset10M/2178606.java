package dioscuri.module.cpu;

/**
 * Intel opcode 2D<BR>
 * Subtract immediate word from AX.<BR>
 * Flags modified: OF, SF, ZF, AF, PF, CF
 */
public class Instruction_SUB_AXIv implements Instruction {

    private CPU cpu;

    byte[] immediateWord;

    byte[] oldDest;

    byte[] temp;

    /**
     * Class constructor
     */
    public Instruction_SUB_AXIv() {
        immediateWord = new byte[2];
        oldDest = new byte[2];
        temp = new byte[2];
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_SUB_AXIv(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Subtract immediate word from AX
     */
    public void execute() {
        immediateWord = cpu.getWordFromCode();
        System.arraycopy(cpu.ax, 0, oldDest, 0, cpu.ax.length);
        temp = Util.subtractWords(cpu.ax, immediateWord, 0);
        System.arraycopy(temp, 0, cpu.ax, 0, temp.length);
        cpu.flags[CPU.REGISTER_FLAGS_AF] = Util.test_AF_SUB(oldDest[CPU.REGISTER_GENERAL_LOW], cpu.ax[CPU.REGISTER_GENERAL_LOW]);
        cpu.flags[CPU.REGISTER_FLAGS_CF] = Util.test_CF_SUB(oldDest, immediateWord, 0);
        cpu.flags[CPU.REGISTER_FLAGS_OF] = Util.test_OF_SUB(oldDest, immediateWord, cpu.ax, 0);
        cpu.flags[CPU.REGISTER_FLAGS_ZF] = cpu.ax[CPU.REGISTER_GENERAL_HIGH] == 0x00 && cpu.ax[CPU.REGISTER_GENERAL_LOW] == 0x00 ? true : false;
        cpu.flags[CPU.REGISTER_FLAGS_SF] = cpu.ax[CPU.REGISTER_GENERAL_HIGH] < 0 ? true : false;
        cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(cpu.ax[CPU.REGISTER_GENERAL_LOW]);
    }
}
