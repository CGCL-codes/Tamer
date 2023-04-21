package dioscuri.module.cpu;

/**
 * Intel opcode A4<BR>
 * Move string byte at address DS:(E)SI to address ES:(E)DI.<BR>
 * After move, contents of SI and DI are incremented or decremented based on DF
 * flag:<BR>
 * Byte: +/- 1, word: +/- 2, doubleword: +/-4.<BR>
 * Flags modified: none
 */
public class Instruction_MOVS_XbYb implements Instruction {

    private CPU cpu;

    boolean operandWordSize;

    byte source1;

    byte[] transition;

    byte[] temp;

    /**
     * Class constructor
     */
    public Instruction_MOVS_XbYb() {
        operandWordSize = false;
        source1 = 0;
        transition = new byte[] { 0x00, 0x01 };
        temp = new byte[2];
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_MOVS_XbYb(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Move byte at address DS:(E)SI to address ES:(E)DI and increment/decrement
     * both depending on DF flag.<BR>
     * Flags modified: none
     */
    public void execute() {
        if (cpu.segmentOverride) {
            source1 = cpu.getByteFromMemorySegment((byte) 0, cpu.si);
        } else {
            source1 = cpu.getByteFromData(cpu.si);
        }
        cpu.setByteToExtra(cpu.di, source1);
        if (cpu.flags[CPU.REGISTER_FLAGS_DF]) {
            temp = Util.subtractWords(cpu.si, transition, 0);
            System.arraycopy(temp, 0, cpu.si, 0, temp.length);
            temp = Util.subtractWords(cpu.di, transition, 0);
            System.arraycopy(temp, 0, cpu.di, 0, temp.length);
        } else {
            temp = Util.addWords(cpu.si, transition, 0);
            System.arraycopy(temp, 0, cpu.si, 0, temp.length);
            temp = Util.addWords(cpu.di, transition, 0);
            System.arraycopy(temp, 0, cpu.di, 0, temp.length);
        }
    }
}
