package dioscuri.module.cpu;

/**
 * Intel opcode 28<BR>
 * Subtract byte in register (source) from memory/register (destination).<BR>
 * The addressbyte determines the source (rrr bits) and destination (sss bits).<BR>
 * Flags modified: OF, SF, ZF, AF, PF, CF
 */
public class Instruction_SUB_EbGb implements Instruction {

    private CPU cpu;

    boolean operandWordSize = false;

    byte addressByte = 0;

    byte[] memoryReferenceLocation = new byte[2];

    byte[] memoryReferenceDisplacement = new byte[2];

    byte sourceValue = 0;

    byte sourceValue2 = 0;

    byte[] destinationRegister = new byte[2];

    byte oldDest = 0;

    byte registerHighLow = 0;

    byte tempResult = 0;

    /**
     * Class constructor
     */
    public Instruction_SUB_EbGb() {
    }

    /**
     * Class constructor specifying processor reference
     *
     * @param processor Reference to CPU class
     */
    public Instruction_SUB_EbGb(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Subtract byte in register (source) from memory/register (destination).<BR>
     */
    public void execute() {
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        registerHighLow = ((addressByte & 0x20) >> 5) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
        sourceValue = (cpu.decodeRegister(operandWordSize, (addressByte & 0x38) >> 3))[registerHighLow];
        if (((addressByte >> 6) & 0x03) == 3) {
            registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
            destinationRegister = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
            oldDest = destinationRegister[registerHighLow];
            destinationRegister[registerHighLow] -= sourceValue;
            cpu.flags[CPU.REGISTER_FLAGS_AF] = Util.test_AF_SUB(oldDest, destinationRegister[registerHighLow]);
            cpu.flags[CPU.REGISTER_FLAGS_CF] = Util.test_CF_SUB(oldDest, sourceValue, 0);
            cpu.flags[CPU.REGISTER_FLAGS_OF] = Util.test_OF_SUB(oldDest, sourceValue, destinationRegister[registerHighLow], 0);
            cpu.flags[CPU.REGISTER_FLAGS_ZF] = destinationRegister[registerHighLow] == 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_SF] = destinationRegister[registerHighLow] < 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(destinationRegister[registerHighLow]);
        } else {
            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
            sourceValue2 = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
            tempResult = (byte) (sourceValue2 - sourceValue);
            cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, tempResult);
            cpu.flags[CPU.REGISTER_FLAGS_AF] = Util.test_AF_SUB(sourceValue2, tempResult);
            cpu.flags[CPU.REGISTER_FLAGS_CF] = Util.test_CF_SUB(sourceValue2, sourceValue, 0);
            cpu.flags[CPU.REGISTER_FLAGS_OF] = Util.test_OF_SUB(sourceValue2, sourceValue, tempResult, 0);
            cpu.flags[CPU.REGISTER_FLAGS_ZF] = tempResult == 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_SF] = tempResult < 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(tempResult);
        }
    }
}
