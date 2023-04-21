package nl.kbna.dioscuri.module.cpu;

import nl.kbna.dioscuri.exception.CPUInstructionException;

/**
 * Intel opcode F6<BR>
 * Unary Group 3 opcode extension: TEST, NOT, NEG, MUL, IMUL, DIV, IDIV.<BR>
 * Performs the selected instruction (indicated by bits 5, 4, 3 of the ModR/M byte).<BR>
 * Flags modified: depending on instruction can be any of: OF, SF, ZF, AF, PF, CF
 */
public class Instruction_UnaryGrp3_Eb implements Instruction {

    private CPU cpu;

    boolean operandWordSize = false;

    byte addressByte = 0;

    byte[] memoryReferenceLocation = new byte[2];

    byte[] memoryReferenceDisplacement = new byte[2];

    byte sourceByte1 = 0;

    byte sourceByte2 = 0;

    byte resultByte = 0;

    byte[] sourceValue = new byte[2];

    byte[] destinationRegister = new byte[2];

    int overFlowCheck;

    byte registerHighLow = 0;

    byte[] tempResult = new byte[2];

    /**
     * Class constructor
     */
    public Instruction_UnaryGrp3_Eb() {
    }

    /**
     * Class constructor specifying processor reference
     * 
     * @param processor Reference to CPU class
     */
    public Instruction_UnaryGrp3_Eb(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Execute any of the following Unary Group 3 opcode extension: TEST, NOT, NEG, MUL, IMUL, DIV, IDIV.<BR>
     * @throws CPUInstructionException 
     */
    public void execute() throws CPUInstructionException {
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        switch((addressByte & 0x38) >> 3) {
            case 0:
                cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                cpu.flags[CPU.REGISTER_FLAGS_CF] = false;
                cpu.flags[CPU.REGISTER_FLAGS_AF] = false;
                sourceByte1 = cpu.getByteFromCode();
                if (((addressByte >> 6) & 0x03) == 3) {
                    registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                    sourceByte2 = cpu.decodeRegister(operandWordSize, addressByte & 0x07)[registerHighLow];
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceByte2 = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                resultByte = (byte) (sourceByte1 & sourceByte2);
                cpu.flags[CPU.REGISTER_FLAGS_ZF] = resultByte == 0 ? true : false;
                cpu.flags[CPU.REGISTER_FLAGS_SF] = resultByte < 0 ? true : false;
                cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(resultByte);
                break;
            case 1:
                throw new CPUInstructionException("Unary Group 3 (0xF6) illegal reg bits");
            case 2:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                    registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                    sourceValue[registerHighLow] = (byte) ~sourceValue[registerHighLow];
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceByte1 = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                    sourceByte1 = (byte) ~sourceByte1;
                    cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, sourceByte1);
                }
                break;
            case 3:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                    registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                    sourceByte1 = sourceValue[registerHighLow];
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceByte1 = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                sourceByte2 = (byte) (0 - sourceByte1);
                if (((addressByte >> 6) & 0x03) != 3) {
                    cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, sourceByte2);
                } else {
                    sourceValue[registerHighLow] = sourceByte2;
                }
                cpu.flags[CPU.REGISTER_FLAGS_CF] = sourceByte2 == 0 ? false : true;
                cpu.flags[CPU.REGISTER_FLAGS_OF] = Util.test_OF_SUB((byte) 0, sourceByte1, sourceByte2, 0);
                cpu.flags[CPU.REGISTER_FLAGS_AF] = (sourceByte2 & 0x0F) != 0 ? true : false;
                cpu.flags[CPU.REGISTER_FLAGS_ZF] = sourceByte2 == 0 ? true : false;
                cpu.flags[CPU.REGISTER_FLAGS_SF] = sourceByte2 < 0 ? true : false;
                cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(sourceByte2);
                break;
            case 4:
                destinationRegister = cpu.ax;
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                    overFlowCheck = ((((int) (destinationRegister[CPU.REGISTER_GENERAL_LOW])) & 0xFF) * (((int) sourceValue[((addressByte & 0x07) > 3 ? CPU.REGISTER_GENERAL_HIGH : CPU.REGISTER_GENERAL_LOW)]) & 0xFF));
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceByte1 = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                    overFlowCheck = ((((int) (destinationRegister[CPU.REGISTER_GENERAL_LOW])) & 0xFF) * (((int) sourceByte1) & 0xFF));
                }
                destinationRegister[CPU.REGISTER_GENERAL_LOW] = (byte) (overFlowCheck);
                destinationRegister[CPU.REGISTER_GENERAL_HIGH] = (byte) ((overFlowCheck) >> 8);
                cpu.flags[CPU.REGISTER_FLAGS_OF] = cpu.flags[CPU.REGISTER_FLAGS_CF] = destinationRegister[CPU.REGISTER_GENERAL_HIGH] == 0 ? false : true;
                cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(destinationRegister[CPU.REGISTER_GENERAL_LOW]);
                cpu.flags[CPU.REGISTER_FLAGS_AF] = false;
                cpu.flags[CPU.REGISTER_FLAGS_ZF] = destinationRegister[CPU.REGISTER_GENERAL_LOW] == 0x00 ? true : false;
                cpu.flags[CPU.REGISTER_FLAGS_SF] = destinationRegister[CPU.REGISTER_GENERAL_LOW] < 0 ? true : false;
                break;
            case 5:
                destinationRegister = cpu.ax;
                if (((addressByte & 0xC0) >> 6) == 3) {
                    registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                    sourceByte1 = cpu.decodeRegister(operandWordSize, addressByte & 0x07)[registerHighLow];
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceByte1 = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                int signedResult = sourceByte1 * cpu.ax[CPU.REGISTER_GENERAL_LOW];
                cpu.ax[CPU.REGISTER_GENERAL_HIGH] = (byte) (signedResult >> 8);
                cpu.ax[CPU.REGISTER_GENERAL_LOW] = (byte) (signedResult & 0xFF);
                if (cpu.ax[CPU.REGISTER_GENERAL_HIGH] == 0x00) {
                    cpu.flags[CPU.REGISTER_FLAGS_CF] = false;
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                } else {
                    cpu.flags[CPU.REGISTER_FLAGS_CF] = true;
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = true;
                }
                break;
            case 6:
                destinationRegister = cpu.ax;
                if (((addressByte & 0xC0) >> 6) == 3) {
                    registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                    sourceByte1 = cpu.decodeRegister(operandWordSize, addressByte & 0x07)[registerHighLow];
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceByte1 = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                int quotient = ((((((int) destinationRegister[CPU.REGISTER_GENERAL_HIGH]) & 0xFF) << 8) + (((int) destinationRegister[CPU.REGISTER_GENERAL_LOW]) & 0xFF)) / (((int) sourceByte1) & 0xFF));
                int remainder = ((((((int) destinationRegister[CPU.REGISTER_GENERAL_HIGH]) & 0xFF) << 8) + (((int) destinationRegister[CPU.REGISTER_GENERAL_LOW]) & 0xFF)) % (((int) sourceByte1) & 0xFF));
                destinationRegister[CPU.REGISTER_GENERAL_LOW] = (byte) (quotient);
                destinationRegister[CPU.REGISTER_GENERAL_HIGH] = (byte) ((remainder));
                break;
            case 7:
                throw new CPUInstructionException("Unary Group 3 (0xF6|7) instruction not implemented!!");
            default:
                throw new CPUInstructionException("Unary Group 3 (0xF6) illegal reg bits");
        }
    }
}
