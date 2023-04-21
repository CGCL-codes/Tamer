package nl.kbna.dioscuri.module.cpu;

/**
	 * Intel opcode 30<BR>
	 * Logical byte-sized XOR of memory/register (destination) and register (source).<BR>
	 * The addressbyte determines the source (rrr bits) and destination (sss bits).<BR>
	 * Flags modified: OF, SF, ZF, AF, PF, CF
	 */
public class Instruction_XOR_EbGb implements Instruction {

    private CPU cpu;

    boolean operandWordSize = false;

    byte addressByte = 0;

    byte[] memoryReferenceLocation = new byte[2];

    byte[] memoryReferenceDisplacement = new byte[2];

    byte sourceValue = 0;

    byte[] destinationRegister = new byte[2];

    byte registerHighLow = 0;

    byte logicalXORResult = 0;

    /**
	 * Class constructor
	 */
    public Instruction_XOR_EbGb() {
    }

    /**
	 * Class constructor specifying processor reference
	 * 
	 * @param processor	Reference to CPU class
	 */
    public Instruction_XOR_EbGb(CPU processor) {
        this();
        cpu = processor;
    }

    /**
	 * Logical XOR of memory/register (destination) and register (source).<BR>
	 * OF and CF are cleared. AF is undefined.
	 */
    public void execute() {
        cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
        cpu.flags[CPU.REGISTER_FLAGS_CF] = false;
        cpu.flags[CPU.REGISTER_FLAGS_AF] = false;
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        registerHighLow = ((addressByte & 0x20) >> 5) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
        sourceValue = (cpu.decodeRegister(operandWordSize, (addressByte & 0x38) >> 3))[registerHighLow];
        if (((addressByte >> 6) & 0x03) == 3) {
            destinationRegister = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
            registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
            destinationRegister[registerHighLow] ^= sourceValue;
            cpu.flags[CPU.REGISTER_FLAGS_ZF] = destinationRegister[registerHighLow] == 0x00 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_SF] = destinationRegister[registerHighLow] < 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(destinationRegister[registerHighLow]);
        } else {
            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
            logicalXORResult = (byte) (cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation) ^ sourceValue);
            cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, logicalXORResult);
            cpu.flags[CPU.REGISTER_FLAGS_ZF] = logicalXORResult == 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_SF] = logicalXORResult < 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(logicalXORResult);
        }
    }
}
