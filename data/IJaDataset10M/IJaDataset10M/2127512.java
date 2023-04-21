package nl.kbna.dioscuri.module.cpu;

/**
     * Intel opcode 11<BR>
     * Add word in register (source) + CF to memory/register (destination).<BR>
     * The addressbyte determines the source (rrr bits) and destination (sss bits).<BR>
     * Flags modified: OF, SF, ZF, AF, PF, CF
     */
public class Instruction_ADC_EvGv implements Instruction {

    private CPU cpu;

    boolean operandWordSize = true;

    byte addressByte = 0;

    byte[] memoryReferenceLocation = new byte[2];

    byte[] memoryReferenceDisplacement = new byte[2];

    byte[] sourceValue = new byte[2];

    byte[] sourceValue2 = new byte[2];

    byte[] destinationValue = new byte[2];

    byte[] destinationRegister = new byte[2];

    int intermediateResult;

    byte iCarryFlag;

    byte[] temp = new byte[2];

    byte[] oldValue = new byte[2];

    /**
     * Class constructor
     */
    public Instruction_ADC_EvGv() {
    }

    /**
     * Class constructor specifying processor reference
     * 
     * @param processor Reference to CPU class
     */
    public Instruction_ADC_EvGv(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Add word in register (source) + CF to memory/register (destination).<BR>
     */
    public void execute() {
        iCarryFlag = (byte) (cpu.flags[CPU.REGISTER_FLAGS_CF] ? 1 : 0);
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        sourceValue = (cpu.decodeRegister(operandWordSize, (addressByte & 0x38) >> 3));
        if (((addressByte >> 6) & 0x03) == 3) {
            destinationRegister = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
            System.arraycopy(destinationRegister, 0, oldValue, 0, destinationRegister.length);
            temp = Util.addWords(destinationRegister, sourceValue, iCarryFlag);
            System.arraycopy(temp, 0, destinationRegister, 0, temp.length);
            cpu.flags[CPU.REGISTER_FLAGS_AF] = Util.test_AF_ADD(oldValue[CPU.REGISTER_GENERAL_LOW], destinationRegister[CPU.REGISTER_GENERAL_LOW]);
            cpu.flags[CPU.REGISTER_FLAGS_CF] = Util.test_CF_ADD(oldValue, sourceValue, iCarryFlag);
            cpu.flags[CPU.REGISTER_FLAGS_OF] = Util.test_OF_ADD(oldValue, sourceValue, destinationRegister, iCarryFlag);
            cpu.flags[CPU.REGISTER_FLAGS_ZF] = destinationRegister[CPU.REGISTER_GENERAL_HIGH] == 0 && destinationRegister[CPU.REGISTER_GENERAL_LOW] == 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_SF] = destinationRegister[CPU.REGISTER_GENERAL_HIGH] < 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(destinationRegister[CPU.REGISTER_GENERAL_LOW]);
        } else {
            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
            sourceValue2 = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
            temp = Util.addWords(sourceValue, sourceValue2, iCarryFlag);
            System.arraycopy(temp, 0, destinationValue, 0, temp.length);
            cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, destinationValue);
            cpu.flags[CPU.REGISTER_FLAGS_AF] = Util.test_AF_ADD(sourceValue2[CPU.REGISTER_GENERAL_LOW], destinationValue[CPU.REGISTER_GENERAL_LOW]);
            cpu.flags[CPU.REGISTER_FLAGS_CF] = Util.test_CF_ADD(sourceValue2, sourceValue, iCarryFlag);
            cpu.flags[CPU.REGISTER_FLAGS_OF] = Util.test_OF_ADD(sourceValue2, sourceValue, destinationValue, iCarryFlag);
            cpu.flags[CPU.REGISTER_FLAGS_ZF] = destinationValue[CPU.REGISTER_GENERAL_HIGH] == 0 && destinationValue[CPU.REGISTER_GENERAL_LOW] == 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_SF] = destinationValue[CPU.REGISTER_GENERAL_HIGH] < 0 ? true : false;
            cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(destinationValue[CPU.REGISTER_GENERAL_LOW]);
        }
    }
}
