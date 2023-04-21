package nl.kbna.dioscuri.module.cpu;

/**
 * Intel opcode 02<BR>
 * Add byte in memory/register (source) to register (destination).<BR>
 * The addressbyte determines the source (rrr bits) and destination (sss bits).<BR>
 * Flags modified: OF, SF, ZF, AF, PF, CF
 */
public class Instruction_ADD_GbEb implements Instruction {

    private CPU cpu;

    boolean operandWordSize = false;

    byte addressByte = 0;

    byte[] memoryReferenceLocation = new byte[2];

    byte[] memoryReferenceDisplacement = new byte[2];

    byte sourceValue = 0;

    byte oldValue = 0;

    byte[] destinationRegister = new byte[2];

    byte registerHighLow = 0;

    /**
     * Class constructor
     */
    public Instruction_ADD_GbEb() {
    }

    /**
     * Class constructor specifying processor reference
     * 
     * @param processor Reference to CPU class
     */
    public Instruction_ADD_GbEb(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Add byte in memory/register (source) to register (destination).<BR>
     */
    public void execute() {
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        if (((addressByte >> 6) & 0x03) == 3) {
            registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
            sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07)[registerHighLow];
        } else {
            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
            sourceValue = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
        }
        registerHighLow = ((addressByte & 0x20) >> 5) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
        destinationRegister = (cpu.decodeRegister(operandWordSize, (addressByte & 0x38) >> 3));
        oldValue = destinationRegister[registerHighLow];
        destinationRegister[registerHighLow] += sourceValue;
        cpu.flags[CPU.REGISTER_FLAGS_AF] = Util.test_AF_ADD(oldValue, destinationRegister[registerHighLow]);
        cpu.flags[CPU.REGISTER_FLAGS_CF] = Util.test_CF_ADD(oldValue, sourceValue, 0);
        cpu.flags[CPU.REGISTER_FLAGS_OF] = Util.test_OF_ADD(oldValue, sourceValue, destinationRegister[registerHighLow], 0);
        cpu.flags[CPU.REGISTER_FLAGS_ZF] = destinationRegister[registerHighLow] == 0 ? true : false;
        cpu.flags[CPU.REGISTER_FLAGS_SF] = destinationRegister[registerHighLow] < 0 ? true : false;
        cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(destinationRegister[registerHighLow]);
    }
}
