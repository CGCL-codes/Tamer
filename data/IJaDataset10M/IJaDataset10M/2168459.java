package dioscuri.module.cpu;

/**
 * Intel opcode 8B<BR>
 * Word-sized copy of register (destination) from memory/register (source).<BR>
 * The addressbyte determines the source (sss bits) and destination (rrr bits).<BR>
 * Flags modified: none
 */
public class Instruction_MOV_GvEv implements Instruction {

    private CPU cpu;

    boolean operandWordSize = true;

    byte addressByte = 0;

    byte[] memoryReferenceLocation = new byte[2];

    byte[] memoryReferenceDisplacement = new byte[2];

    byte[] sourceWord = new byte[2];

    byte[] destinationRegister = new byte[2];

    /**
     * Class constructor
     */
    public Instruction_MOV_GvEv() {
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_MOV_GvEv(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Word-sized copy of register (destination) from memory/register (source).<BR>
     * Flags modified: none
     */
    public void execute() {
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        if (((addressByte >> 6) & 0x03) == 3) {
            sourceWord = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
        } else {
            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
            sourceWord = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
        }
        destinationRegister = (cpu.decodeRegister(operandWordSize, (addressByte & 0x38) >> 3));
        System.arraycopy(sourceWord, 0, destinationRegister, 0, sourceWord.length);
    }
}
