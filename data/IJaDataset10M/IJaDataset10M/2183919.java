package nl.kbna.dioscuri.module.cpu;

import nl.kbna.dioscuri.exception.CPUInstructionException;

/**
 * Intel opcode C1<BR>
 * Immediate Group 2 opcode extension: ROL, ROR, RCL, RCR, SHL/SAL, SHR, SAR.<BR>
 * Performs the selected instruction (indicated by bits 5, 4, 3 of the ModR/M byte) using immediate byte.<BR>
 * Flags modified: depending on instruction can be any of: OF, SF, ZF, AF, PF, CF
 */
public class Instruction_ShiftGRP2_EvIb implements Instruction {

    private CPU cpu;

    boolean operandWordSize = true;

    byte addressByte = 0;

    byte[] memoryReferenceLocation = new byte[2];

    byte[] memoryReferenceDisplacement = new byte[2];

    byte[] sourceValue = new byte[2];

    byte[] eSourceValue = new byte[2];

    int overFlowCheck;

    byte[] tempResult = new byte[2];

    int bitShift;

    long shiftResult;

    int carryBit;

    int newCarryBit;

    /**
     * Class constructor
     */
    public Instruction_ShiftGRP2_EvIb() {
        bitShift = 0;
        shiftResult = 0;
        carryBit = 0;
        newCarryBit = 0;
    }

    /**
     * Class constructor specifying processor reference
     * 
     * @param processor Reference to CPU class
     */
    public Instruction_ShiftGRP2_EvIb(CPU processor) {
        this();
        cpu = processor;
    }

    /**
     * Execute any of the following Immediate Group 2 instructions: ROL, ROR, RCL, RCR, SHL/SAL, SHR, SAR.<BR>
     * @throws CPUInstructionException 
     */
    public void execute() throws CPUInstructionException {
        sourceValue = new byte[2];
        eSourceValue = new byte[2];
        addressByte = cpu.getByteFromCode();
        memoryReferenceDisplacement = cpu.decodeMM(addressByte);
        bitShift = (byte) (cpu.getByteFromCode() & 0x1F);
        switch((addressByte & 0x38) >> 3) {
            case 0:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                shiftResult = (((((long) sourceValue[CPU.REGISTER_GENERAL_HIGH]) << 8) & 0xFF00) + ((((long) sourceValue[CPU.REGISTER_GENERAL_LOW]) & 0xFF)));
                carryBit = (shiftResult & 0x8000) == 0x8000 ? 1 : 0;
                for (int s = 0; s < bitShift; s++) {
                    carryBit = (shiftResult & 0x8000) == 0x8000 ? 1 : 0;
                    shiftResult = (((shiftResult << 1) & 0xFFFF) | carryBit);
                }
                sourceValue[CPU.REGISTER_GENERAL_HIGH] = ((byte) (shiftResult >> 8));
                sourceValue[CPU.REGISTER_GENERAL_LOW] = ((byte) (shiftResult & 0xFF));
                if (((addressByte >> 6) & 0x03) != 3) {
                    cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceValue);
                }
                cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                if (bitShift == 1) {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = (carryBit ^ ((sourceValue[CPU.REGISTER_GENERAL_HIGH] & 0x80) == 0x80 ? 1 : 0)) == 1 ? true : false;
                } else {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                }
                break;
            case 1:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                shiftResult = (((((long) sourceValue[CPU.REGISTER_GENERAL_HIGH]) << 8) & 0xFF00) + ((((long) sourceValue[CPU.REGISTER_GENERAL_LOW]) & 0xFF)));
                carryBit = (shiftResult & 0x01) == 0x01 ? 1 : 0;
                for (int s = 0; s < bitShift; s++) {
                    carryBit = (shiftResult & 0x01) == 0x01 ? 1 : 0;
                    shiftResult = ((shiftResult >> 1) | (carryBit << (15)));
                }
                sourceValue[CPU.REGISTER_GENERAL_HIGH] = ((byte) (shiftResult >> 8));
                sourceValue[CPU.REGISTER_GENERAL_LOW] = ((byte) (shiftResult & 0xFF));
                if (((addressByte >> 6) & 0x03) != 3) {
                    cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceValue);
                }
                cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                if (bitShift == 1) {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = (((sourceValue[CPU.REGISTER_GENERAL_HIGH] >> 7) & 0x01) ^ ((sourceValue[CPU.REGISTER_GENERAL_HIGH] >> 6) & 0x01)) == 0x01 ? true : false;
                } else {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                }
                break;
            case 2:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                shiftResult = (((((long) sourceValue[CPU.REGISTER_GENERAL_HIGH]) << 8) & 0xFF00) + ((((long) sourceValue[CPU.REGISTER_GENERAL_LOW]) & 0xFF)));
                carryBit = cpu.flags[CPU.REGISTER_FLAGS_CF] == true ? 1 : 0;
                for (int s = 0; s < bitShift; s++) {
                    newCarryBit = (shiftResult & 0x8000) == 0x8000 ? 1 : 0;
                    shiftResult = ((shiftResult << 1) & 0xFFFF) | carryBit;
                    carryBit = newCarryBit;
                }
                sourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) ((shiftResult >> 8) & 0xFF);
                sourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) (shiftResult & 0xFF);
                if (((addressByte >> 6) & 0x03) != 3) {
                    cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceValue);
                }
                cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                if (bitShift == 1) {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = (carryBit ^ ((sourceValue[CPU.REGISTER_GENERAL_HIGH] & 0x80) == 0x80 ? 1 : 0)) == 1 ? true : false;
                } else {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                }
                break;
            case 3:
                if (((addressByte >> 6) & 0x03) == 3) {
                    sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                } else {
                    memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                    sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                }
                shiftResult = (((((long) sourceValue[CPU.REGISTER_GENERAL_HIGH]) << 8) & 0xFF00) + ((((long) sourceValue[CPU.REGISTER_GENERAL_LOW]) & 0xFF)));
                carryBit = cpu.flags[CPU.REGISTER_FLAGS_CF] == true ? 1 : 0;
                for (int s = 0; s < bitShift; s++) {
                    newCarryBit = (shiftResult & 0x0001) == 0x0001 ? 1 : 0;
                    shiftResult = ((shiftResult >> 1) & 0x7FFF) | ((carryBit) << 15);
                    carryBit = newCarryBit;
                }
                sourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) ((shiftResult >> 8) & 0xFF);
                sourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) (shiftResult & 0xFF);
                if (((addressByte >> 6) & 0x03) != 3) {
                    cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceValue);
                }
                cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                if (bitShift == 1) {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = (((sourceValue[CPU.REGISTER_GENERAL_HIGH] >> 7) & 0x01) ^ ((sourceValue[CPU.REGISTER_GENERAL_HIGH] >> 6) & 0x01)) == 0x01 ? true : false;
                } else {
                    cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                }
                break;
            case 4:
                if (bitShift > 0) {
                    if (cpu.doubleWord) {
                        if (((addressByte >> 6) & 0x03) == 3) {
                            sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                            eSourceValue = cpu.decodeExtraRegister(addressByte & 0x07);
                        } else {
                            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                            sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                            int overFlowCheck = (((int) memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW]) & 0xFF) + 2;
                            if (overFlowCheck > 0xFF) {
                                memoryReferenceLocation[CPU.REGISTER_GENERAL_HIGH]++;
                            }
                            memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW] += 2;
                            eSourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                        }
                        shiftResult = (((((long) eSourceValue[CPU.REGISTER_GENERAL_HIGH]) << 24) & 0xFF000000) + ((((long) eSourceValue[CPU.REGISTER_GENERAL_LOW]) << 16) & 0xFF0000) + ((((long) sourceValue[CPU.REGISTER_GENERAL_HIGH]) << 8) & 0xFF00) + ((((long) sourceValue[CPU.REGISTER_GENERAL_LOW]) & 0xFF))) << (bitShift - 1);
                        carryBit = (int) ((shiftResult & 0x80000000) >> 31) & 0x01;
                        shiftResult = shiftResult << 1;
                        eSourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) ((shiftResult >> 24) & 0xFF);
                        eSourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) ((shiftResult >> 16) & 0xFF);
                        sourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) ((shiftResult >> 8) & 0xFF);
                        sourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) (shiftResult & 0xFF);
                        if (((addressByte >> 6) & 0x03) != 3) {
                            cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, eSourceValue);
                            memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW] -= 2;
                            if (memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW] == -1 || memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW] == -2) {
                                memoryReferenceLocation[CPU.REGISTER_GENERAL_HIGH]--;
                            }
                            cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceValue);
                        }
                    } else {
                        if (((addressByte >> 6) & 0x03) == 3) {
                            sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                        } else {
                            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                            sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                        }
                        shiftResult = (((((long) sourceValue[CPU.REGISTER_GENERAL_HIGH]) << 8) & 0xFF00) + ((((long) sourceValue[CPU.REGISTER_GENERAL_LOW]) & 0xFF))) << (bitShift - 1);
                        carryBit = (int) ((shiftResult & 0x8000) >> 15) & 0x01;
                        shiftResult = shiftResult << 1;
                        sourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) ((shiftResult >> 8) & 0xFF);
                        sourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) (shiftResult & 0xFF);
                        if (((addressByte >> 6) & 0x03) != 3) {
                            cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceValue);
                        }
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_AF] = false;
                    cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                    if (!cpu.doubleWord) {
                        cpu.flags[CPU.REGISTER_FLAGS_OF] = (cpu.flags[CPU.REGISTER_FLAGS_CF] && ((sourceValue[CPU.REGISTER_GENERAL_HIGH]) >> 7) == 1) || (!(cpu.flags[CPU.REGISTER_FLAGS_CF]) && ((sourceValue[CPU.REGISTER_GENERAL_HIGH]) >> 7) == 0) ? false : true;
                        cpu.flags[CPU.REGISTER_FLAGS_ZF] = sourceValue[CPU.REGISTER_GENERAL_HIGH] == 0 && sourceValue[CPU.REGISTER_GENERAL_LOW] == 0 ? true : false;
                        cpu.flags[CPU.REGISTER_FLAGS_SF] = sourceValue[CPU.REGISTER_GENERAL_HIGH] < 0 ? true : false;
                    } else {
                        if (bitShift == 1) {
                            cpu.flags[CPU.REGISTER_FLAGS_OF] = (cpu.flags[CPU.REGISTER_FLAGS_CF] && ((eSourceValue[CPU.REGISTER_GENERAL_HIGH]) >> 7) == 1) || (!(cpu.flags[CPU.REGISTER_FLAGS_CF]) && ((eSourceValue[CPU.REGISTER_GENERAL_HIGH]) >> 7) == 0) ? false : true;
                        }
                        cpu.flags[CPU.REGISTER_FLAGS_ZF] = sourceValue[CPU.REGISTER_GENERAL_HIGH] == 0 && sourceValue[CPU.REGISTER_GENERAL_LOW] == 0 && eSourceValue[CPU.REGISTER_GENERAL_HIGH] == 0 && eSourceValue[CPU.REGISTER_GENERAL_LOW] == 0 ? true : false;
                        cpu.flags[CPU.REGISTER_FLAGS_SF] = eSourceValue[CPU.REGISTER_GENERAL_HIGH] < 0 ? true : false;
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(sourceValue[CPU.REGISTER_GENERAL_LOW]);
                }
                break;
            case 5:
                if (bitShift > 0) {
                    if (cpu.doubleWord) {
                        if (((addressByte >> 6) & 0x03) == 3) {
                            sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                            eSourceValue = cpu.decodeExtraRegister(addressByte & 0x07);
                        } else {
                            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                            sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                            int overFlowCheck = (((int) memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW]) & 0xFF) + 2;
                            if (overFlowCheck > 0xFF) {
                                memoryReferenceLocation[CPU.REGISTER_GENERAL_HIGH]++;
                            }
                            memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW] += 2;
                            eSourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                        }
                        if (bitShift == 1) {
                            cpu.flags[CPU.REGISTER_FLAGS_OF] = (eSourceValue[CPU.REGISTER_GENERAL_HIGH] & 0x80) == 0x80 ? true : false;
                        }
                        shiftResult = (((((long) eSourceValue[CPU.REGISTER_GENERAL_HIGH]) << 24) & 0xFF000000) + ((((long) eSourceValue[CPU.REGISTER_GENERAL_LOW]) << 16) & 0xFF0000) + ((((long) sourceValue[CPU.REGISTER_GENERAL_HIGH]) << 8) & 0xFF00) + ((((long) sourceValue[CPU.REGISTER_GENERAL_LOW]) & 0xFF))) >> (bitShift - 1);
                        carryBit = (int) (shiftResult & 0x01);
                        shiftResult = shiftResult >> 1;
                        eSourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) ((shiftResult >> 24) & 0xFF);
                        eSourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) ((shiftResult >> 16) & 0xFF);
                        sourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) ((shiftResult >> 8) & 0xFF);
                        sourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) (shiftResult & 0xFF);
                        if (((addressByte >> 6) & 0x03) != 3) {
                            cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, eSourceValue);
                            memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW] -= 2;
                            if (memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW] == -1 || memoryReferenceLocation[CPU.REGISTER_GENERAL_LOW] == -2) {
                                memoryReferenceLocation[CPU.REGISTER_GENERAL_HIGH]--;
                            }
                            cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceValue);
                        }
                    } else {
                        if (((addressByte >> 6) & 0x03) == 3) {
                            sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                        } else {
                            memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                            sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                        }
                        if (bitShift == 1) {
                            cpu.flags[CPU.REGISTER_FLAGS_OF] = (sourceValue[CPU.REGISTER_GENERAL_HIGH] & 0x80) == 0x80 ? true : false;
                        }
                        shiftResult = (((((long) sourceValue[CPU.REGISTER_GENERAL_HIGH]) << 8) & 0xFF00) + ((((long) sourceValue[CPU.REGISTER_GENERAL_LOW]) & 0xFF))) >> (bitShift - 1);
                        carryBit = (int) (shiftResult & 0x01);
                        shiftResult = shiftResult >> 1;
                        sourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) ((shiftResult >> 8) & 0xFF);
                        sourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) (shiftResult & 0xFF);
                        if (((addressByte >> 6) & 0x03) != 3) {
                            cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceValue);
                        }
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_AF] = false;
                    cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                    if (!cpu.doubleWord) {
                        cpu.flags[CPU.REGISTER_FLAGS_ZF] = sourceValue[CPU.REGISTER_GENERAL_HIGH] == 0 && sourceValue[CPU.REGISTER_GENERAL_LOW] == 0 ? true : false;
                        cpu.flags[CPU.REGISTER_FLAGS_SF] = sourceValue[CPU.REGISTER_GENERAL_HIGH] < 0 ? true : false;
                    } else {
                        cpu.flags[CPU.REGISTER_FLAGS_ZF] = sourceValue[CPU.REGISTER_GENERAL_HIGH] == 0 && sourceValue[CPU.REGISTER_GENERAL_LOW] == 0 && eSourceValue[CPU.REGISTER_GENERAL_HIGH] == 0 && eSourceValue[CPU.REGISTER_GENERAL_LOW] == 0 ? true : false;
                        cpu.flags[CPU.REGISTER_FLAGS_SF] = eSourceValue[CPU.REGISTER_GENERAL_HIGH] < 0 ? true : false;
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(sourceValue[CPU.REGISTER_GENERAL_LOW]);
                }
                break;
            case 6:
                throw new CPUInstructionException("Shift Group 2 (0xD2/6) illegal reg bits");
            case 7:
                if (bitShift > 0) {
                    if (((addressByte >> 6) & 0x03) == 3) {
                        sourceValue = cpu.decodeRegister(operandWordSize, addressByte & 0x07);
                    } else {
                        memoryReferenceLocation = cpu.decodeSSSMemDest(addressByte, memoryReferenceDisplacement);
                        sourceValue = cpu.getWordFromMemorySegment(addressByte, memoryReferenceLocation);
                    }
                    if (bitShift >= 16) {
                        if ((sourceValue[CPU.REGISTER_GENERAL_HIGH] & 0x80) == 0x80) {
                            sourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) 0xff;
                            sourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) 0xff;
                        } else {
                            sourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) 0x00;
                            sourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) 0x00;
                        }
                    } else {
                        shiftResult = (((((long) sourceValue[CPU.REGISTER_GENERAL_HIGH]) << 8) & 0xFF00) + ((((long) sourceValue[CPU.REGISTER_GENERAL_LOW]) & 0xFF))) >> (bitShift - 1);
                        carryBit = (int) (shiftResult & 0x01);
                        shiftResult = shiftResult >> 1;
                        if ((sourceValue[CPU.REGISTER_GENERAL_HIGH] & 0x80) == 0x80) {
                            shiftResult |= (0xFFFF << (16 - bitShift));
                        }
                    }
                    sourceValue[CPU.REGISTER_GENERAL_HIGH] = (byte) ((shiftResult >> 8) & 0xFF);
                    sourceValue[CPU.REGISTER_GENERAL_LOW] = (byte) (shiftResult & 0xFF);
                    if (((addressByte >> 6) & 0x03) != 3) {
                        cpu.setWordInMemorySegment(addressByte, memoryReferenceLocation, sourceValue);
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_AF] = false;
                    cpu.flags[CPU.REGISTER_FLAGS_CF] = carryBit == 1 ? true : false;
                    if (bitShift == 1) {
                        cpu.flags[CPU.REGISTER_FLAGS_OF] = false;
                    }
                    cpu.flags[CPU.REGISTER_FLAGS_ZF] = sourceValue[CPU.REGISTER_GENERAL_HIGH] == 0 && sourceValue[CPU.REGISTER_GENERAL_LOW] == 0 ? true : false;
                    cpu.flags[CPU.REGISTER_FLAGS_SF] = sourceValue[CPU.REGISTER_GENERAL_HIGH] < 0 ? true : false;
                    cpu.flags[CPU.REGISTER_FLAGS_PF] = Util.checkParityOfByte(sourceValue[CPU.REGISTER_GENERAL_LOW]);
                }
                break;
            default:
                throw new CPUInstructionException("Shift Group 2 (0xD2/6) no case match");
        }
    }
}
