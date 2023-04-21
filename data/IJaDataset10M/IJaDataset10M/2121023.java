package dioscuri.module.cpu;

/**
 * Intel opcode D7<BR>
 * Set AL to memory byte DS:[BX + unsigned AL].<BR>
 * Flags modified: none
 */
public class Instruction_XLAT implements Instruction {

    private CPU cpu;

    byte defaultAddressByteDS;

    byte[] memoryReferenceLocation;

    /**
     * Class constructor
     */
    public Instruction_XLAT() {
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_XLAT(CPU processor) {
        cpu = processor;
        defaultAddressByteDS = 0x00;
        memoryReferenceLocation = new byte[2];
    }

    /**
     * Set AL to memory byte DS:[BX + unsigned AL]
     */
    public void execute() {
        memoryReferenceLocation = Util.addWords(cpu.bx, new byte[] { 0x00, cpu.ax[CPU.REGISTER_GENERAL_LOW] }, 0);
        cpu.ax[CPU.REGISTER_GENERAL_LOW] = cpu.getByteFromMemorySegment(defaultAddressByteDS, memoryReferenceLocation);
    }
}
