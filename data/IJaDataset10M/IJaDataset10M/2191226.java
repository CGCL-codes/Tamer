package dioscuri.module.cpu;

/**
 * Intel opcode 3B<BR>
 * Word-sized comparison (SUB) of register ("destination") with memory/register
 * (source).<BR>
 * The addressbyte determines the source (rrr bits) and "destination" (sss
 * bits).<BR>
 * Flags modified: OF, SF, ZF, AF, PF, CF
 */
public class Instruction_CMP_GvEv implements Instruction {

    private CPU cpu;

    boolean operandWordSize;

    byte addressByte;

    byte[] memoryReferenceLocation;

    byte[] memoryReferenceDisplacement;

    byte[] sourceValue;

    byte[] destinationValue;

    byte[] tempResult;

    byte[] temp;

    int intermediateResult;

    /**
     * Class constructor
     */
    public Instruction_CMP_GvEv() {
        operandWordSize = true;
        addressByte = 0;
        memoryReferenceLocation = new byte[2];
        memoryReferenceDisplacement = new byte[2];
        sourceValue = new byte[2];
        destinationValue = new byte[2];
        tempResult = new byte[2];
        intermediateResult = 0;
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_CMP_GvEv(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Word-sized comparison (SUB) of register ("destination") with
     * memory/register (source).<BR>
     * Does not update any registers, only sets appropriate flags.
     */
    public void execute() {
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        if (((addressByte >> 6) & 0x03) == 3) {
            sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
        } else {
            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
            sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
        }
        destinationValue = cpu.decodeRegister(operandWordSize, (addressByte & 0x38) >> 3);
        tempResult = Util.subtractWords(destinationValue, sourceValue, 0);
        cpu.flags[CPU.REGISTER_FLAGS_AF] = Util.test_AF_SUB(destinationValue[CPU.REGISTER_GENERAL_LOW], tempResult[CPU.REGISTER_GENERAL_LOW]);
        cpu.flags[CPU.REGISTER_FLAGS_CF] = Util.test_CF_SUB(destinationValue, sourceValue, 0);
        cpu.flags[CPU.REGISTER_FLAGS_OF] = Util.test_OF_SUB(destinationValue, sourceValue, tempResult, 0);
        cpu.flags[CPU.REGISTER_FLAGS_ZF] = tempResult[CPU.REGISTER_GENERAL_HIGH] == 0x00 && tempResult[CPU.REGISTER_GENERAL_LOW] == 0x00 ? true : false;
        cpu.flags[CPU.REGISTER_FLAGS_SF] = tempResult[CPU.REGISTER_GENERAL_HIGH] < 0 ? true : false;
        cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(tempResult[CPU.REGISTER_GENERAL_LOW]);
    }
}
