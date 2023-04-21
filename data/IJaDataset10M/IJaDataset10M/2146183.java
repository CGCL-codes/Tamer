package nl.kbna.dioscuri.module.cpu;

import nl.kbna.dioscuri.exception.CPUInstructionException;

/**
 * Intel opcode C0<BR>
 * Immediate Group 2 opcode extension: ROL, ROR, RCL, RCR, SHL/SAL, SHR, SAR.<BR>
 * Performs the selected instruction (indicated by bits 5, 4, 3 of the ModR/M byte) using immediate byte.<BR>
 * Flags modified: depending on instruction can be any of: OF, SF, ZF, AF, PF, CF
 */
public class Instruction_ShiftGRP2_EbIb implements Instruction {

    private CPU cpu;

    boolean operandWordSize;

    byte addressByte;

    byte[] memoryReferenceLocation;

    byte[] memoryReferenceDisplacement;

    byte registerHighLow;

    byte[] sourceValue;

    int overFlowCheck;

    int bitShift;

    int carryBit;

    int newCarryBit;

    long shiftResult;

    byte[] tempResult;

    /**
     * Class constructor
     */
    public Instruction_ShiftGRP2_EbIb() {
        operandWordSize = false;
        addressByte = 0;
        memoryReferenceLocation = new byte[2];
        memoryReferenceDisplacement = new byte[2];
        registerHighLow = 0;
        sourceValue = new byte[2];
        overFlowCheck = 0;
        bitShift = 0;
        carryBit = 0;
        newCarryBit = 0;
        shiftResult = 0;
        tempResult = new byte[2];
    }

    /**
     * Class constructor specifying processor reference
     * 
     * @param processor Reference to CPU class
     */
    public Instruction_ShiftGRP2_EbIb(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Execute any of the following Immediate Group 2 instructions: ROL, ROR, RCL, RCR, SHL/SAL, SHR, SAR.<BR>
     * @throws CPUInstructionException 
     */
    public void execute() throws CPUInstructionException {
        sourceValue = new byte[2];
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        bitShift = (byte) (cpu.getByteFromCode() & 0x1F);
        switch((addressByte & 0x38) >> 3) {
            case 0:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                    registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    registerHighLow = CPU.REGISTER_GENERAL_LOW;
                    sourceValue[registerHighLow] = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                carryBit = (sourceValue[registerHighLow] & 0x80) == 0x80 ? 1 : 0;
                for (int s = 0; s < bitShift; s++) {
                    carryBit = (sourceValue[registerHighLow] & 0x80) == 0x80 ? 1 : 0;
                    sourceValue[registerHighLow] = (byte) ((sourceValue[registerHighLow] << 1) | carryBit);
                }
                if (((addressByte >> 6) & 0x03) != 3) {
                    cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, sourceValue[registerHighLow]);
                }
                cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                if (bitShift == 1) {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = (carryBit ^ ((sourceValue[registerHighLow] & 0x80) == 0x80 ? 1 : 0)) == 1 ? true : false;
                }
                break;
            case 1:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                    registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    registerHighLow = 0;
                    sourceValue[registerHighLow] = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                for (int s = 0; s < bitShift; s++) {
                    sourceValue[registerHighLow] = (byte) ((((sourceValue[registerHighLow]) >> 1) & 0x7F) | ((sourceValue[registerHighLow]) << 7));
                }
                if (((addressByte >> 6) & 0x03) != 3) {
                    cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, sourceValue[registerHighLow]);
                }
                cpu.flags[CPU.REGISTER_FLAGS_CF] = (sourceValue[registerHighLow] & 0x80) == 0x80 ? true : false;
                if (bitShift == 1) {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = ((cpu.flags[CPU.REGISTER_FLAGS_CF] ^ ((sourceValue[registerHighLow]) & 0x40) == 0x40)) ? true : false;
                }
                break;
            case 2:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                    registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    registerHighLow = 0;
                    sourceValue[registerHighLow] = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                carryBit = cpu.flags[CPU.REGISTER_FLAGS_CF] == true ? 1 : 0;
                for (int s = 0; s < bitShift; s++) {
                    newCarryBit = (sourceValue[registerHighLow] & 0x80) == 0x80 ? 1 : 0;
                    sourceValue[registerHighLow] = (byte) ((sourceValue[registerHighLow] << 1) | carryBit);
                    carryBit = newCarryBit;
                }
                if (((addressByte >> 6) & 0x03) != 3) {
                    cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, sourceValue[registerHighLow]);
                }
                cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                if (bitShift == 1) {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = (carryBit ^ ((sourceValue[registerHighLow] & 0x80) == 0x80 ? 1 : 0)) == 1 ? true : false;
                }
                break;
            case 3:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                    registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    registerHighLow = 0;
                    sourceValue[registerHighLow] = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                carryBit = cpu.flags[CPU.REGISTER_FLAGS_CF] == true ? 1 : 0;
                for (int s = 0; s < bitShift; s++) {
                    newCarryBit = (sourceValue[registerHighLow] & 0x01) == 0x01 ? 1 : 0;
                    sourceValue[registerHighLow] = (byte) ((((sourceValue[registerHighLow]) >> 1) & 0x7F) | ((carryBit) << 7));
                    carryBit = newCarryBit;
                }
                if (((addressByte >> 6) & 0x03) != 3) {
                    cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, sourceValue[registerHighLow]);
                }
                cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                if (bitShift == 1) {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = (carryBit ^ ((sourceValue[registerHighLow] & 0x80) == 0x80 ? 1 : 0)) == 1 ? true : false;
                }
                break;
            case 4:
                if (bitShift > 0) {
                    if (((addressByte >> 6) & 0x03) == 3) {
                        sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                        registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                    } else {
                        memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                        registerHighLow = 0;
                        sourceValue[registerHighLow] = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                    }
                    sourceValue[registerHighLow] = (byte) ((sourceValue[registerHighLow]) << bitShift);
                    carryBit = (((sourceValue[registerHighLow]) << (bitShift - 1)) & 0x80) == 0x80 ? 1 : 0;
                    if (((addressByte >> 6) & 0x03) != 3) {
                        cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, sourceValue[registerHighLow]);
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_AF] = false;
                    cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                    if (bitShift == 1) {
                        cpu.flags[CPU.REGISTER_FLAGS_OF] = (cpu.flags[CPU.REGISTER_FLAGS_CF] && ((sourceValue[registerHighLow]) >> 7) == 1) || (!(cpu.flags[CPU.REGISTER_FLAGS_CF]) && ((sourceValue[registerHighLow]) >> 7) == 0) ? false : true;
                    } else {
                        cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_ZF] = sourceValue[registerHighLow] == 0 ? true : false;
                    cpu.flags[CPU.REGISTER_FLAGS_SF] = sourceValue[registerHighLow] < 0 ? true : false;
                    cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(sourceValue[registerHighLow]);
                }
                break;
            case 5:
                if (bitShift > 0) {
                    if (((addressByte >> 6) & 0x03) == 3) {
                        sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                        registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                    } else {
                        memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                        registerHighLow = 0;
                        sourceValue[registerHighLow] = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                    }
                    if (bitShift == 1) {
                        cpu.flags[CPU.REGISTER_FLAGS_OF] = (sourceValue[registerHighLow] & 0x80) == 0x80 ? true : false;
                    } else {
                        cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                    }
                    shiftResult = ((int) sourceValue[registerHighLow]) & 0xFF;
                    shiftResult >>= (bitShift - 1);
                    cpu.flags[CPU.REGISTER_FLAGS_CF] = (shiftResult & 0x01) == 1 ? true : false;
                    shiftResult >>= 1;
                    sourceValue[registerHighLow] = (byte) shiftResult;
                    if (((addressByte >> 6) & 0x03) != 3) {
                        cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, sourceValue[registerHighLow]);
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_AF] = false;
                    cpu.flags[CPU.REGISTER_FLAGS_ZF] = sourceValue[registerHighLow] == 0 ? true : false;
                    cpu.flags[CPU.REGISTER_FLAGS_SF] = sourceValue[registerHighLow] < 0 ? true : false;
                    cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(sourceValue[registerHighLow]);
                }
                break;
            case 6:
                throw new CPUInstructionException("Shift Group 2 (0xD2/6) illegal reg bits");
            case 7:
                if (bitShift > 0) {
                    if (((addressByte >> 6) & 0x03) == 3) {
                        sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                        registerHighLow = ((addressByte & 0x04) >> 2) == 0 ? (byte) CPU.REGISTER_GENERAL_LOW : (byte) CPU.REGISTER_GENERAL_HIGH;
                    } else {
                        memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                        registerHighLow = 0;
                        sourceValue[registerHighLow] = cpu.getByteFromMemorySegment(addressByte, memoryReferenceLocation);
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_CF] = (sourceValue[registerHighLow] & 0x01) == 1 ? true : false;
                    if (bitShift == 1) {
                        cpu.flags[CPU.REGISTER_FLAGS_OF] = (sourceValue[registerHighLow] & 0x80) == 0x80 ? true : false;
                    } else {
                        cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                    }
                    sourceValue[registerHighLow] = (byte) (sourceValue[registerHighLow] >> bitShift);
                    if (((addressByte >> 6) & 0x03) != 3) {
                        cpu.setByteInMemorySegment(addressByte, memoryReferenceLocation, sourceValue[registerHighLow]);
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_AF] = false;
                    cpu.flags[CPU.REGISTER_FLAGS_ZF] = sourceValue[registerHighLow] == 0 ? true : false;
                    cpu.flags[CPU.REGISTER_FLAGS_SF] = sourceValue[registerHighLow] < 0 ? true : false;
                    cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(sourceValue[registerHighLow]);
                }
                break;
            default:
                throw new CPUInstructionException("Shift Group 2 (0xD2/6) no case match");
        }
    }
}
