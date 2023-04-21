package edu.rice.cs.cunit.classFile.code;

import edu.rice.cs.cunit.util.Types;
import edu.rice.cs.cunit.classFile.code.instructions.AInstruction;
import edu.rice.cs.cunit.classFile.code.instructions.GenericInstruction;
import edu.rice.cs.cunit.classFile.code.instructions.WideInstruction;

/**
 * Java opcode class.
 *
 * @author Mathias Ricken
 */
public abstract class Opcode {

    public static final byte NOP = (byte) 0x00;

    public static final byte ACONST_NULL = (byte) 0x01;

    public static final byte ICONST_M1 = (byte) 0x02;

    public static final byte ICONST_0 = (byte) 0x03;

    public static final byte ICONST_1 = (byte) 0x04;

    public static final byte ICONST_2 = (byte) 0x05;

    public static final byte ICONST_3 = (byte) 0x06;

    public static final byte ICONST_4 = (byte) 0x07;

    public static final byte ICONST_5 = (byte) 0x08;

    public static final byte LCONST_0 = (byte) 0x09;

    public static final byte LCONST_1 = (byte) 0x0A;

    public static final byte FCONST_0 = (byte) 0x0B;

    public static final byte FCONST_1 = (byte) 0x0C;

    public static final byte FCONST_2 = (byte) 0x0D;

    public static final byte DCONST_0 = (byte) 0x0E;

    public static final byte DCONST_1 = (byte) 0x0F;

    public static final byte BIPUSH = (byte) 0x10;

    public static final byte SIPUSH = (byte) 0x11;

    public static final byte LDC = (byte) 0x12;

    public static final byte LDC_W = (byte) 0x13;

    public static final byte LDC2_W = (byte) 0x14;

    public static final byte ILOAD = (byte) 0x15;

    public static final byte LLOAD = (byte) 0x16;

    public static final byte FLOAD = (byte) 0x17;

    public static final byte DLOAD = (byte) 0x18;

    public static final byte ALOAD = (byte) 0x19;

    public static final byte ILOAD_0 = (byte) 0x1A;

    public static final byte ILOAD_1 = (byte) 0x1B;

    public static final byte ILOAD_2 = (byte) 0x1C;

    public static final byte ILOAD_3 = (byte) 0x1D;

    public static final byte LLOAD_0 = (byte) 0x1E;

    public static final byte LLOAD_1 = (byte) 0x1F;

    public static final byte LLOAD_2 = (byte) 0x20;

    public static final byte LLOAD_3 = (byte) 0x21;

    public static final byte FLOAD_0 = (byte) 0x22;

    public static final byte FLOAD_1 = (byte) 0x23;

    public static final byte FLOAD_2 = (byte) 0x24;

    public static final byte FLOAD_3 = (byte) 0x25;

    public static final byte DLOAD_0 = (byte) 0x26;

    public static final byte DLOAD_1 = (byte) 0x27;

    public static final byte DLOAD_2 = (byte) 0x28;

    public static final byte DLOAD_3 = (byte) 0x29;

    public static final byte ALOAD_0 = (byte) 0x2A;

    public static final byte ALOAD_1 = (byte) 0x2B;

    public static final byte ALOAD_2 = (byte) 0x2C;

    public static final byte ALOAD_3 = (byte) 0x2D;

    public static final byte IALOAD = (byte) 0x2E;

    public static final byte LALOAD = (byte) 0x2F;

    public static final byte FALOAD = (byte) 0x30;

    public static final byte DALOAD = (byte) 0x31;

    public static final byte AALOAD = (byte) 0x32;

    public static final byte BALOAD = (byte) 0x33;

    public static final byte CALOAD = (byte) 0x34;

    public static final byte SALOAD = (byte) 0x35;

    public static final byte ISTORE = (byte) 0x36;

    public static final byte LSTORE = (byte) 0x37;

    public static final byte FSTORE = (byte) 0x38;

    public static final byte DSTORE = (byte) 0x39;

    public static final byte ASTORE = (byte) 0x3A;

    public static final byte ISTORE_0 = (byte) 0x3B;

    public static final byte ISTORE_1 = (byte) 0x3C;

    public static final byte ISTORE_2 = (byte) 0x3D;

    public static final byte ISTORE_3 = (byte) 0x3E;

    public static final byte LSTORE_0 = (byte) 0x3F;

    public static final byte LSTORE_1 = (byte) 0x40;

    public static final byte LSTORE_2 = (byte) 0x41;

    public static final byte LSTORE_3 = (byte) 0x42;

    public static final byte FSTORE_0 = (byte) 0x43;

    public static final byte FSTORE_1 = (byte) 0x44;

    public static final byte FSTORE_2 = (byte) 0x45;

    public static final byte FSTORE_3 = (byte) 0x46;

    public static final byte DSTORE_0 = (byte) 0x47;

    public static final byte DSTORE_1 = (byte) 0x48;

    public static final byte DSTORE_2 = (byte) 0x49;

    public static final byte DSTORE_3 = (byte) 0x4A;

    public static final byte ASTORE_0 = (byte) 0x4B;

    public static final byte ASTORE_1 = (byte) 0x4C;

    public static final byte ASTORE_2 = (byte) 0x4D;

    public static final byte ASTORE_3 = (byte) 0x4E;

    public static final byte IASTORE = (byte) 0x4F;

    public static final byte LASTORE = (byte) 0x50;

    public static final byte FASTORE = (byte) 0x51;

    public static final byte DASTORE = (byte) 0x52;

    public static final byte AASTORE = (byte) 0x53;

    public static final byte BASTORE = (byte) 0x54;

    public static final byte CASTORE = (byte) 0x55;

    public static final byte SASTORE = (byte) 0x56;

    public static final byte POP = (byte) 0x57;

    public static final byte POP2 = (byte) 0x58;

    public static final byte DUP = (byte) 0x59;

    public static final byte DUP_X1 = (byte) 0x5A;

    public static final byte DUP_X2 = (byte) 0x5B;

    public static final byte DUP2 = (byte) 0x5C;

    public static final byte DUP2_X1 = (byte) 0x5D;

    public static final byte DUP2_X2 = (byte) 0x5E;

    public static final byte SWAP = (byte) 0x5F;

    public static final byte IADD = (byte) 0x60;

    public static final byte LADD = (byte) 0x61;

    public static final byte FADD = (byte) 0x62;

    public static final byte DADD = (byte) 0x63;

    public static final byte ISUB = (byte) 0x64;

    public static final byte LSUB = (byte) 0x65;

    public static final byte FSUB = (byte) 0x66;

    public static final byte DSUB = (byte) 0x67;

    public static final byte IMUL = (byte) 0x68;

    public static final byte LMUL = (byte) 0x69;

    public static final byte FMUL = (byte) 0x6A;

    public static final byte DMUL = (byte) 0x6B;

    public static final byte IDIV = (byte) 0x6C;

    public static final byte LDIV = (byte) 0x6D;

    public static final byte FDIV = (byte) 0x6E;

    public static final byte DDIV = (byte) 0x6F;

    public static final byte IREM = (byte) 0x70;

    public static final byte LREM = (byte) 0x71;

    public static final byte FREM = (byte) 0x72;

    public static final byte DREM = (byte) 0x73;

    public static final byte INEG = (byte) 0x74;

    public static final byte LNEG = (byte) 0x75;

    public static final byte FNEG = (byte) 0x76;

    public static final byte DNEG = (byte) 0x77;

    public static final byte ISHL = (byte) 0x78;

    public static final byte LSHL = (byte) 0x79;

    public static final byte ISHR = (byte) 0x7A;

    public static final byte LSHR = (byte) 0x7B;

    public static final byte IUSHR = (byte) 0x7C;

    public static final byte LUSHR = (byte) 0x7D;

    public static final byte IAND = (byte) 0x7E;

    public static final byte LAND = (byte) 0x7F;

    public static final byte IOR = (byte) 0x80;

    public static final byte LOR = (byte) 0x81;

    public static final byte IXOR = (byte) 0x82;

    public static final byte LXOR = (byte) 0x83;

    public static final byte IINC = (byte) 0x84;

    public static final byte I2L = (byte) 0x85;

    public static final byte I2F = (byte) 0x86;

    public static final byte I2D = (byte) 0x87;

    public static final byte L2I = (byte) 0x88;

    public static final byte L2F = (byte) 0x89;

    public static final byte L2D = (byte) 0x8A;

    public static final byte F2I = (byte) 0x8B;

    public static final byte F2L = (byte) 0x8C;

    public static final byte F2D = (byte) 0x8D;

    public static final byte D2I = (byte) 0x8E;

    public static final byte D2L = (byte) 0x8F;

    public static final byte D2F = (byte) 0x90;

    public static final byte I2B = (byte) 0x91;

    public static final byte I2C = (byte) 0x92;

    public static final byte I2S = (byte) 0x93;

    public static final byte LCMP = (byte) 0x94;

    public static final byte FCMPL = (byte) 0x95;

    public static final byte FCMPG = (byte) 0x96;

    public static final byte DCMPL = (byte) 0x97;

    public static final byte DCMPG = (byte) 0x98;

    public static final byte IFEQ = (byte) 0x99;

    public static final byte IFNE = (byte) 0x9A;

    public static final byte IFLT = (byte) 0x9B;

    public static final byte IFGE = (byte) 0x9C;

    public static final byte IFGT = (byte) 0x9D;

    public static final byte IFLE = (byte) 0x9E;

    public static final byte IF_ICMPEQ = (byte) 0x9F;

    public static final byte IF_ICMPNE = (byte) 0xA0;

    public static final byte IF_ICMPLT = (byte) 0xA1;

    public static final byte IF_ICMPGE = (byte) 0xA2;

    public static final byte IF_ICMPGT = (byte) 0xA3;

    public static final byte IF_ICMPLE = (byte) 0xA4;

    public static final byte IF_ACMPEQ = (byte) 0xA5;

    public static final byte IF_ACMPNE = (byte) 0xA6;

    public static final byte GOTO = (byte) 0xA7;

    public static final byte JSR = (byte) 0xA8;

    public static final byte RET = (byte) 0xA9;

    public static final byte TABLESWITCH = (byte) 0xAA;

    public static final byte LOOKUPSWITCH = (byte) 0xAB;

    public static final byte IRETURN = (byte) 0xAC;

    public static final byte LRETURN = (byte) 0xAD;

    public static final byte FRETURN = (byte) 0xAE;

    public static final byte DRETURN = (byte) 0xAF;

    public static final byte ARETURN = (byte) 0xB0;

    public static final byte RETURN = (byte) 0xB1;

    public static final byte GETSTATIC = (byte) 0xB2;

    public static final byte PUTSTATIC = (byte) 0xB3;

    public static final byte GETFIELD = (byte) 0xB4;

    public static final byte PUTFIELD = (byte) 0xB5;

    public static final byte INVOKEVIRTUAL = (byte) 0xB6;

    public static final byte INVOKESPECIAL = (byte) 0xB7;

    public static final byte INVOKESTATIC = (byte) 0xB8;

    public static final byte INVOKEINTERFACE = (byte) 0xB9;

    public static final byte XXXUNUSEDXXX = (byte) 0xBA;

    public static final byte NEW = (byte) 0xBB;

    public static final byte NEWARRAY = (byte) 0xBC;

    public static final byte ANEWARRAY = (byte) 0xBD;

    public static final byte ARRAYLENGTH = (byte) 0xBE;

    public static final byte ATHROW = (byte) 0xBF;

    public static final byte CHECKCAST = (byte) 0xC0;

    public static final byte INSTANCEOF = (byte) 0xC1;

    public static final byte MONITORENTER = (byte) 0xC2;

    public static final byte MONITOREXIT = (byte) 0xC3;

    public static final byte WIDE = (byte) 0xC4;

    public static final byte MULTIANEWARRAY = (byte) 0xC5;

    public static final byte IFNULL = (byte) 0xC6;

    public static final byte IFNONNULL = (byte) 0xC7;

    public static final byte GOTO_W = (byte) 0xC8;

    public static final byte JSR_W = (byte) 0xC9;

    public static final byte BREAKPOINT = (byte) 0xCA;

    public static final byte LDC_QUICK = (byte) 0xCB;

    public static final byte LDC_W_QUICK = (byte) 0xCC;

    public static final byte LDC2_W_QUICK = (byte) 0xCD;

    public static final byte GETFIELD_QUICK = (byte) 0xCE;

    public static final byte PUTFIELD_QUICK = (byte) 0xCF;

    public static final byte GETFIELD2_QUICK = (byte) 0xD0;

    public static final byte PUTFIELD2_QUICK = (byte) 0xD1;

    public static final byte GETSTATIC_QUICK = (byte) 0xD2;

    public static final byte PUTSTATIC_QUICK = (byte) 0xD3;

    public static final byte GETSTATIC2_QUICK = (byte) 0xD4;

    public static final byte PUTSTATIC2_QUICK = (byte) 0xD5;

    public static final byte INVOKEVIRTUAL_QUICK = (byte) 0xD6;

    public static final byte INVOKENONVIRTUAL_QUICK = (byte) 0xD7;

    public static final byte INVOKESUPER_QUICK = (byte) 0xD8;

    public static final byte INVOKESTATIC_QUICK = (byte) 0xD9;

    public static final byte INVOKEINTERFACE_QUICK = (byte) 0xDA;

    public static final byte INVOKEVIRTUALOBJECT_QUICK = (byte) 0xDB;

    public static final byte UNKNOWN_DC = (byte) 0xDC;

    public static final byte NEW_QUICK = (byte) 0xDD;

    public static final byte ANEWARRAY_QUICK = (byte) 0xDE;

    public static final byte MULTIANEWARRAY_QUICK = (byte) 0xDF;

    public static final byte CHECKCAST_QUICK = (byte) 0xE0;

    public static final byte INSTANCEOF_QUICK = (byte) 0xE1;

    public static final byte INVOKEVIRTUAL_QUICK_W = (byte) 0xE2;

    public static final byte GETFIELD_QUICK_W = (byte) 0xE3;

    public static final byte PUTFIELD_QUICK_W = (byte) 0xE4;

    public static final byte UNKNOWN_E5 = (byte) 0xE5;

    public static final byte UNKNOWN_E6 = (byte) 0xE6;

    public static final byte UNKNOWN_E7 = (byte) 0xE7;

    public static final byte UNKNOWN_E8 = (byte) 0xE8;

    public static final byte UNKNOWN_E9 = (byte) 0xE9;

    public static final byte UNKNOWN_EA = (byte) 0xEA;

    public static final byte UNKNOWN_EB = (byte) 0xEB;

    public static final byte UNKNOWN_EC = (byte) 0xEC;

    public static final byte UNKNOWN_ED = (byte) 0xED;

    public static final byte UNKNOWN_EE = (byte) 0xEE;

    public static final byte UNKNOWN_EF = (byte) 0xEF;

    public static final byte UNKNOWN_F0 = (byte) 0xF0;

    public static final byte UNKNOWN_F1 = (byte) 0xF1;

    public static final byte UNKNOWN_F2 = (byte) 0xF2;

    public static final byte UNKNOWN_F3 = (byte) 0xF3;

    public static final byte UNKNOWN_F4 = (byte) 0xF4;

    public static final byte UNKNOWN_F5 = (byte) 0xF5;

    public static final byte UNKNOWN_F6 = (byte) 0xF6;

    public static final byte UNKNOWN_F7 = (byte) 0xF7;

    public static final byte UNKNOWN_F8 = (byte) 0xF8;

    public static final byte UNKNOWN_F9 = (byte) 0xF9;

    public static final byte UNKNOWN_FA = (byte) 0xFA;

    public static final byte UNKNOWN_FB = (byte) 0xFB;

    public static final byte UNKNOWN_FC = (byte) 0xFC;

    public static final byte UNKNOWN_FD = (byte) 0xFD;

    public static final byte IMPDEP1 = (byte) 0xFE;

    public static final byte IMPDEP2 = (byte) 0xFF;

    /**
     * Return a human-readable version of the code.
     *
     * @param opcode code
     *
     * @return mnemonic
     */
    public static final String getOpcodeName(byte opcode) {
        return NAMES[((int) opcode) & 0xFF];
    }

    /**
     * Table of mnemonics.
     */
    private static final String[] NAMES = new String[] { "nop", "aconst_null", "iconst_m1", "iconst_0", "iconst_1", "iconst_2", "iconst_3", "iconst_4", "iconst_5", "lconst_0", "lconst_1", "fconst_0", "fconst_1", "fconst_2", "dconst_0", "dconst_1", "bipush", "sipush", "ldc", "ldc_w", "ldc2_w", "iload", "lload", "fload", "dload", "aload", "iload_0", "iload_1", "iload_2", "iload_3", "lload_0", "lload_1", "lload_2", "lload_3", "fload_0", "fload_1", "fload_2", "fload_3", "dload_0", "dload_1", "dload_2", "dload_3", "aload_0", "aload_1", "aload_2", "aload_3", "iaload", "laload", "faload", "daload", "aaload", "baload", "caload", "saload", "istore", "lstore", "fstore", "dstore", "astore", "istore_0", "istore_1", "istore_2", "istore_3", "lstore_0", "lstore_1", "lstore_2", "lstore_3", "fstore_0", "fstore_1", "fstore_2", "fstore_3", "dstore_0", "dstore_1", "dstore_2", "dstore_3", "astore_0", "astore_1", "astore_2", "astore_3", "iastore", "lastore", "fastore", "dastore", "aastore", "bastore", "castore", "sastore", "pop", "pop2", "dup", "dup_x1", "dup_x2", "dup2", "dup2_x1", "dup2_x2", "swap", "iadd", "ladd", "fadd", "dadd", "isub", "lsub", "fsub", "dsub", "imul", "lmul", "fmul", "dmul", "idiv", "ldiv", "fdiv", "ddiv", "irem", "lrem", "frem", "drem", "ineg", "lneg", "fneg", "dneg", "ishl", "lshl", "ishr", "lshr", "iushr", "lushr", "iand", "land", "ior", "lor", "ixor", "lxor", "iinc", "i2l", "i2f", "i2d", "l2i", "l2f", "l2d", "f2i", "f2l", "f2d", "d2i", "d2l", "d2f", "i2b", "i2c", "i2s", "lcmp", "fcmpl", "fcmpg", "dcmpl", "dcmpg", "ifeq", "ifne", "iflt", "ifge", "ifgt", "ifle", "if_icmpeq", "if_icmpne", "if_icmplt", "if_icmpge", "if_icmpgt", "if_icmple", "if_acmpeq", "if_acmpne", "goto", "jsr", "ret", "tableswitch", "lookupswitch", "ireturn", "lreturn", "freturn", "dreturn", "areturn", "return", "getstatic", "putstatic", "getfield", "putfield", "invokevirtual", "invokespecial", "invokestatic", "invokeinterface", "xxxunusedxxx", "new", "newarray", "anewarray", "arraylength", "athrow", "checkcast", "instanceof", "monitorenter", "monitorexit", "wide", "multianewarray", "ifnull", "ifnonnull", "goto_w", "jsr_w", "breakpoint", "ldc_quick", "ldc_w_quick", "ldc2_w_quick", "getfield_quick", "putfield_quick", "getfield2_quick", "putfield2_quick", "getstatic_quick", "putstatic_quick", "getstatic2_quick", "putstatic2_quick", "invokevirtual_quick", "invokenonvirtual_quick", "invokesuper_quick", "invokestatic_quick", "invokeinterface_quick", "invokevirtualobject_quick", "unknown_dc", "new_quick", "anewarray_quick", "multianewarray_quick", "checkcast_quick", "instanceof_quick", "invokevirtual_quick_w", "getfield_quick_w", "putfield_quick_w", "unknown_e5", "unknown_e6", "unknown_e7", "unknown_e8", "unknown_e9", "unknown_ea", "unknown_eb", "unknown_ec", "unknown_ed", "unknown_ee", "unknown_ef", "unknown_f0", "unknown_f1", "unknown_f2", "unknown_f3", "unknown_f4", "unknown_f5", "unknown_f6", "unknown_f7", "unknown_f8", "unknown_f9", "unknown_fa", "unknown_fb", "unknown_fc", "unknown_fd", "impdep1", "impdep2" };

    /**
     * Table with code offsets.
     */
    private static final byte[] OFFSET = new byte[256];

    static {
        for (int i = 0; i < OFFSET.length; ++i) {
            OFFSET[i] = 0;
        }
        OFFSET[Types.unsigned(BIPUSH)] = 1;
        OFFSET[Types.unsigned(SIPUSH)] = 2;
        OFFSET[Types.unsigned(LDC)] = 1;
        OFFSET[Types.unsigned(LDC_W)] = 2;
        OFFSET[Types.unsigned(LDC2_W)] = 2;
        for (int i = Types.unsigned(ILOAD); i <= Types.unsigned(ALOAD); ++i) {
            OFFSET[i] = 1;
        }
        for (int i = Types.unsigned(ISTORE); i <= Types.unsigned(ASTORE); ++i) {
            OFFSET[i] = 1;
        }
        OFFSET[Types.unsigned(IINC)] = 2;
        for (int i = Types.unsigned(IFEQ); i <= Types.unsigned(JSR); i++) {
            OFFSET[i] = 2;
        }
        OFFSET[Types.unsigned(RET)] = 1;
        for (int i = Types.unsigned(GETSTATIC); i <= Types.unsigned(INVOKESTATIC); ++i) {
            OFFSET[i] = 2;
        }
        OFFSET[Types.unsigned(INVOKEINTERFACE)] = 4;
        OFFSET[Types.unsigned(NEW)] = 2;
        OFFSET[Types.unsigned(NEWARRAY)] = 1;
        OFFSET[Types.unsigned(ANEWARRAY)] = 2;
        OFFSET[Types.unsigned(CHECKCAST)] = 2;
        OFFSET[Types.unsigned(INSTANCEOF)] = 2;
        OFFSET[Types.unsigned(MULTIANEWARRAY)] = 3;
        OFFSET[Types.unsigned(IFNULL)] = 2;
        OFFSET[Types.unsigned(IFNONNULL)] = 2;
        OFFSET[Types.unsigned(GOTO_W)] = 4;
        OFFSET[Types.unsigned(JSR_W)] = 4;
    }

    /**
     * Return the length of the instruction at the specified offset in the bytecode array. This includes the code itself
     * and its operands.
     *
     * @param code the bytecode array.
     * @param pc   offset
     *
     * @return the length of the code and operands
     */
    public static final int getInstrSize(byte[] code, int pc) {
        return getInstrSize(code, pc, pc);
    }

    /**
     * Return the length of the instruction at the specified offset in the bytecode array. This includes the code itself
     * and its operands.
     *
     * @param code      the bytecode array.
     * @param pc        offset
     * @param paddingPC offset for calculating padding
     *
     * @return the length of the code and operands
     */
    public static final int getInstrSize(byte[] code, int pc, int paddingPC) {
        byte opcode = code[pc];
        switch(opcode) {
            case LOOKUPSWITCH:
                {
                    int pad = 3 - (paddingPC % 4);
                    long npairs = Types.intFromBytes(code, pc + pad + 5);
                    assert npairs >= 0;
                    return (int) (npairs * 8) + pad + 9;
                }
            case TABLESWITCH:
                {
                    int pad = 3 - (paddingPC % 4);
                    long low = Types.intFromBytes(code, pc + pad + 5);
                    long high = Types.intFromBytes(code, pc + pad + 9);
                    long npairs = high - low + 1;
                    assert low <= high;
                    return (int) (npairs * 4) + pad + 13;
                }
            case WIDE:
                if (code[pc + 1] == IINC) {
                    return 6;
                }
                return 4;
            default:
                return 1 + OFFSET[Types.unsigned(opcode)];
        }
    }

    /**
     * Change the padding on the inside of the instruction found at pc, so that it is padded for a PC=newPC, not for a
     * PC=paddingPC.
     *
     * @param code      bytecode
     * @param pc        start of instruction in bytecode array
     * @param paddingPC old PC used for padding
     * @param newPC     new PC used for padding
     *
     * @return repadded bytecode array
     */
    public static final byte[] repadInstr(byte[] code, int pc, int paddingPC, int newPC) {
        byte opcode = code[pc];
        switch(opcode) {
            case LOOKUPSWITCH:
                {
                    int pad = 3 - (paddingPC % 4);
                    int newPad = 3 - (newPC % 4);
                    byte[] newCode = new byte[code.length - pad + newPad];
                    System.arraycopy(code, 0, newCode, 0, pc + 1);
                    System.arraycopy(code, pc + pad + 1, newCode, pc + newPad + 1, code.length - pc - pad - 1);
                    return newCode;
                }
            case TABLESWITCH:
                {
                    int pad = 3 - (paddingPC % 4);
                    int newPad = 3 - (newPC % 4);
                    byte[] newCode = new byte[code.length - pad + newPad];
                    System.arraycopy(code, 0, newCode, 0, pc + 1);
                    System.arraycopy(code, pc + pad + 1, newCode, pc + newPad + 1, code.length - pc - pad - 1);
                    return newCode;
                }
            default:
                byte[] newCode = new byte[code.length];
                System.arraycopy(code, 0, newCode, 0, code.length);
                return newCode;
        }
    }

    /**
     * Return true if the specified code is a return instruction.
     *
     * NOTE: ATHROW is not a return instruction. It does not leave immediately if there are matching exception handlers.
     *
     * @param opcode code to check
     *
     * @return true if the code is a return instruction
     */
    public static final boolean isReturn(byte opcode) {
        switch(opcode) {
            case IRETURN:
            case LRETURN:
            case FRETURN:
            case DRETURN:
            case ARETURN:
            case RETURN:
                return true;
            default:
                return false;
        }
    }

    /**
     * Table determining if an code is a branch instruction.
     */
    private static final boolean[] isBranch = new boolean[256];

    static {
        for (int i = 0; i < isBranch.length; ++i) {
            isBranch[i] = false;
        }
        for (int i = Types.unsigned(IFEQ); i <= Types.unsigned(RETURN); i++) {
            isBranch[i] = true;
        }
        for (int i = Types.unsigned(IFNULL); i <= Types.unsigned(JSR_W); i++) {
            isBranch[i] = true;
        }
        isBranch[Types.unsigned(ATHROW)] = true;
    }

    /**
     * Return true if the specified code is a branch instruction.
     *
     * @param opcode code to check
     *
     * @return true if the code is a branch instruction
     */
    public static final boolean isBranch(byte opcode) {
        return isBranch[Types.unsigned(opcode)];
    }

    /**
     * Return true if the specified code is an unconditional branch instruction.
     *
     * @param opcode code to check
     *
     * @return true if the code is an unconditional branch instruction
     */
    public static final boolean isUnconditionalBranch(byte opcode) {
        switch(opcode) {
            case GOTO:
            case GOTO_W:
            case JSR:
            case JSR_W:
            case RET:
            case IRETURN:
            case LRETURN:
            case FRETURN:
            case DRETURN:
            case ARETURN:
            case RETURN:
            case ATHROW:
            case LOOKUPSWITCH:
            case TABLESWITCH:
                return true;
            default:
                return false;
        }
    }

    /**
     * Return true if the specified code is a subroutine call.
     *
     * @param opcode code to check
     *
     * @return true if the code is a JSR or JSR_W instruction
     */
    public static final boolean isJSR(byte opcode) {
        return ((opcode == JSR) || (opcode == JSR_W));
    }

    /**
     * Returns an array of branch targets for the instruction.
     *
     * @param code      byte code
     * @param pc        program counter
     * @param paddingPC
     *
     * @return array of branch targets
     */
    public static final int[] getBranchTargets(byte[] code, int pc, int paddingPC) {
        if (!isBranch(code[pc])) {
            return new int[0];
        }
        switch(code[pc]) {
            case LOOKUPSWITCH:
                {
                    int pad = 3 - (paddingPC % 4);
                    long deflt = Types.intFromBytes(code, pc + pad + 1);
                    long npairs = Types.intFromBytes(code, pc + pad + 5);
                    assert npairs >= 0;
                    int[] result = new int[(int) npairs + 1];
                    result[0] = pc + ((int) deflt);
                    for (int i = 0; i < npairs; i++) {
                        long offset = Types.intFromBytes(code, pc + pad + 9 + 4 + (8 * i));
                        result[i + 1] = pc + ((int) offset);
                    }
                    return result;
                }
            case TABLESWITCH:
                {
                    int pad = 3 - (paddingPC % 4);
                    long deflt = Types.intFromBytes(code, pc + pad + 1);
                    long low = Types.intFromBytes(code, pc + pad + 5);
                    long high = Types.intFromBytes(code, pc + pad + 9);
                    long npairs = high - low + 1;
                    assert low <= high;
                    int[] result = new int[(int) npairs + 1];
                    result[0] = pc + ((int) deflt);
                    for (int i = 0; i < npairs; i++) {
                        long offset = Types.intFromBytes(code, pc + pad + 13 + (4 * i));
                        result[i + 1] = pc + ((int) offset);
                    }
                    return result;
                }
            case GOTO_W:
            case JSR_W:
                {
                    long offset = Types.intFromBytes(code, pc + 1);
                    int[] result = new int[1];
                    result[0] = pc + (int) offset;
                    return result;
                }
            case RET:
            case RETURN:
            case IRETURN:
            case LRETURN:
            case FRETURN:
            case DRETURN:
            case ARETURN:
            case ATHROW:
                {
                    return new int[0];
                }
            default:
                {
                    int offset = Types.ushortFromBytes(code, pc + 1);
                    int[] result = new int[1];
                    result[0] = pc + offset;
                    return result;
                }
        }
    }

    /**
     * Sets the branch targets for an instruction.
     *
     * NOTE: The number of branch targets has to remain unchanged.
     *
     * @param code          byte code
     * @param pc            program counter
     * @param branchTargets array of branch targets
     */
    public static final void setBranchTargets(byte[] code, int pc, int[] branchTargets) {
        if (!isBranch(code[pc])) {
            throw new IllegalArgumentException("Cannot set branch targets for non-branch instruction");
        }
        switch(code[pc]) {
            case LOOKUPSWITCH:
                {
                    int pad = 3 - (pc % 4);
                    long npairs = Types.intFromBytes(code, pc + pad + 5);
                    assert npairs >= 0;
                    if (branchTargets.length != npairs + 1) {
                        throw new IllegalArgumentException("Not allowed to change number of branch targets");
                    }
                    int delta = branchTargets[0] - pc;
                    Types.bytesFromInt(delta, code, pc + pad + 1);
                    for (int i = 0; i < npairs; i++) {
                        delta = branchTargets[1 + i] - pc;
                        Types.bytesFromInt(delta, code, pc + pad + 9 + 4 + (8 * i));
                    }
                    return;
                }
            case TABLESWITCH:
                {
                    int pad = 3 - (pc % 4);
                    long low = Types.intFromBytes(code, pc + pad + 5);
                    long high = Types.intFromBytes(code, pc + pad + 9);
                    long npairs = high - low + 1;
                    assert low <= high;
                    if (branchTargets.length != npairs + 1) {
                        throw new IllegalArgumentException("Not allowed to change number of branch targets");
                    }
                    int delta = branchTargets[0] - pc;
                    Types.bytesFromInt(delta, code, pc + pad + 1);
                    for (int i = 0; i < npairs; i++) {
                        delta = branchTargets[1 + i] - pc;
                        Types.bytesFromInt(delta, code, pc + pad + 13 + (4 * i));
                    }
                    return;
                }
            case GOTO_W:
            case JSR_W:
                {
                    if (branchTargets.length != 1) {
                        throw new IllegalArgumentException("Not allowed to change number of branch targets");
                    }
                    int delta = branchTargets[0] - pc;
                    Types.bytesFromInt(delta, code, pc + 1);
                    return;
                }
            case RET:
            case RETURN:
            case IRETURN:
            case LRETURN:
            case FRETURN:
            case DRETURN:
            case ARETURN:
            case ATHROW:
                {
                    if (branchTargets.length != 0) {
                        throw new IllegalArgumentException("Not allowed to change number of branch targets");
                    }
                    return;
                }
            default:
                {
                    if (branchTargets.length != 1) {
                        throw new IllegalArgumentException("Not allowed to change number of branch targets");
                    }
                    int delta = (branchTargets[0] - pc);
                    Types.bytesFromShort((short) (0xffff & delta), code, pc + 1);
                    return;
                }
        }
    }

    /**
     * Return the shortest instruction possible that loads/stores local variable index onto the stack.
     * @param baseOpcode the kind of load/store (ALOAD, ILOAD, ...)
     * @param index local variable index
     * @return shortest instruction to load/store
     */
    public static AInstruction getShortestLoadStoreInstruction(byte baseOpcode, short index) {
        switch(baseOpcode) {
            case ILOAD:
            case ILOAD_0:
            case ILOAD_1:
            case ILOAD_2:
            case ILOAD_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(ILOAD_0);
                    case 1:
                        return new GenericInstruction(ILOAD_1);
                    case 2:
                        return new GenericInstruction(ILOAD_2);
                    case 3:
                        return new GenericInstruction(ILOAD_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(ILOAD, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { ILOAD, b[0], b[1] });
                        }
                }
            case LLOAD:
            case LLOAD_0:
            case LLOAD_1:
            case LLOAD_2:
            case LLOAD_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(LLOAD_0);
                    case 1:
                        return new GenericInstruction(LLOAD_1);
                    case 2:
                        return new GenericInstruction(LLOAD_2);
                    case 3:
                        return new GenericInstruction(LLOAD_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(LLOAD, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { LLOAD, b[0], b[1] });
                        }
                }
            case FLOAD:
            case FLOAD_0:
            case FLOAD_1:
            case FLOAD_2:
            case FLOAD_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(FLOAD_0);
                    case 1:
                        return new GenericInstruction(FLOAD_1);
                    case 2:
                        return new GenericInstruction(FLOAD_2);
                    case 3:
                        return new GenericInstruction(FLOAD_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(FLOAD, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { FLOAD, b[0], b[1] });
                        }
                }
            case DLOAD:
            case DLOAD_0:
            case DLOAD_1:
            case DLOAD_2:
            case DLOAD_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(DLOAD_0);
                    case 1:
                        return new GenericInstruction(DLOAD_1);
                    case 2:
                        return new GenericInstruction(DLOAD_2);
                    case 3:
                        return new GenericInstruction(DLOAD_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(DLOAD, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { DLOAD, b[0], b[1] });
                        }
                }
            case ALOAD:
            case ALOAD_0:
            case ALOAD_1:
            case ALOAD_2:
            case ALOAD_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(ALOAD_0);
                    case 1:
                        return new GenericInstruction(ALOAD_1);
                    case 2:
                        return new GenericInstruction(ALOAD_2);
                    case 3:
                        return new GenericInstruction(ALOAD_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(ALOAD, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { ALOAD, b[0], b[1] });
                        }
                }
            case ISTORE:
            case ISTORE_0:
            case ISTORE_1:
            case ISTORE_2:
            case ISTORE_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(ISTORE_0);
                    case 1:
                        return new GenericInstruction(ISTORE_1);
                    case 2:
                        return new GenericInstruction(ISTORE_2);
                    case 3:
                        return new GenericInstruction(ISTORE_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(ISTORE, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { ISTORE, b[0], b[1] });
                        }
                }
            case LSTORE:
            case LSTORE_0:
            case LSTORE_1:
            case LSTORE_2:
            case LSTORE_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(LSTORE_0);
                    case 1:
                        return new GenericInstruction(LSTORE_1);
                    case 2:
                        return new GenericInstruction(LSTORE_2);
                    case 3:
                        return new GenericInstruction(LSTORE_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(LSTORE, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { LSTORE, b[0], b[1] });
                        }
                }
            case FSTORE:
            case FSTORE_0:
            case FSTORE_1:
            case FSTORE_2:
            case FSTORE_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(FSTORE_0);
                    case 1:
                        return new GenericInstruction(FSTORE_1);
                    case 2:
                        return new GenericInstruction(FSTORE_2);
                    case 3:
                        return new GenericInstruction(FSTORE_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(FSTORE, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { FSTORE, b[0], b[1] });
                        }
                }
            case DSTORE:
            case DSTORE_0:
            case DSTORE_1:
            case DSTORE_2:
            case DSTORE_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(DSTORE_0);
                    case 1:
                        return new GenericInstruction(DSTORE_1);
                    case 2:
                        return new GenericInstruction(DSTORE_2);
                    case 3:
                        return new GenericInstruction(DSTORE_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(DSTORE, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { DSTORE, b[0], b[1] });
                        }
                }
            case ASTORE:
            case ASTORE_0:
            case ASTORE_1:
            case ASTORE_2:
            case ASTORE_3:
                switch(index) {
                    case 0:
                        return new GenericInstruction(ASTORE_0);
                    case 1:
                        return new GenericInstruction(ASTORE_1);
                    case 2:
                        return new GenericInstruction(ASTORE_2);
                    case 3:
                        return new GenericInstruction(ASTORE_3);
                    default:
                        if (index <= 256) {
                            return new GenericInstruction(ASTORE, (byte) index);
                        } else {
                            byte[] b = Types.bytesFromInt(index);
                            return new WideInstruction(new byte[] { ASTORE, b[0], b[1] });
                        }
                }
            default:
                throw new IllegalArgumentException("Only ILOAD, LLOAD, FLOAD, DLOAD, ALOAD, ISTORE, LSTORE, FSTORE, DSTORE and ASTORE allowed");
        }
    }
}
