package net.sf.jadretro;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

final class OpCodeConst extends OpByteCode {

    private static final int ANEWARRAY = 0xbd;

    private static final int BIPUSH = 0x10;

    private static final int CHECKCAST = 0xc0;

    private static final int GETSTATIC = 0xb2;

    private static final int INSTANCEOF = 0xc1;

    private static final int INVOKEINTERFACE = 0xb9;

    private static final int INVOKESPECIAL = 0xb7;

    private static final int INVOKESTATIC = 0xb8;

    private static final int INVOKEVIRTUAL = 0xb6;

    private static final int LDC = 0x12;

    private static final int LDC2_W = 0x14;

    private static final int LDC_W = 0x13;

    private static final int MULTIANEWARRAY = 0xc5;

    private static final int NEW = 0xbb;

    private static final int NEWARRAY = 0xbc;

    private static final int PUTFIELD = 0xb5;

    private static final int PUTSTATIC = 0xb3;

    private static final int SIPUSH = 0x11;

    private int op;

    private ConstantRef poolConst;

    private int intArg;

    private OpCodeConst(int op, ConstantRef poolConst, int intArg) {
        this.op = op;
        this.poolConst = poolConst;
        this.intArg = intArg;
    }

    private OpCodeConst(int op, ConstantRef poolConst) {
        if (poolConst == null) throw new IllegalArgumentException();
        this.op = op;
        this.poolConst = poolConst;
        intArg = 0;
    }

    static OpCodeConst decode(int op, InputStream in, ClassFile classFile) throws IOException {
        if (op == LDC) return new OpCodeConst(LDC, ConstantRef.readAsByteFrom(in, classFile), 0);
        if (op == SIPUSH) return new OpCodeConst(SIPUSH, null, readShort(in));
        if (op == BIPUSH || op == NEWARRAY) return new OpCodeConst(op, null, readByte(in));
        if (op == LDC_W || op == LDC2_W || (op >= GETSTATIC && op <= INVOKEINTERFACE) || op == NEW || op == ANEWARRAY || op == CHECKCAST || op == INSTANCEOF || op == MULTIANEWARRAY) {
            ConstantRef poolConst = new ConstantRef(in, classFile, false);
            int intArg = 0;
            if (op == INVOKEINTERFACE || op == MULTIANEWARRAY) {
                intArg = readUnsignedByte(in);
                if (op == INVOKEINTERFACE && in.read() != 0) throw new BadClassFileException();
            }
            return new OpCodeConst(op, poolConst, intArg);
        }
        return null;
    }

    static OpCodeConst makeInvokestatic(ConstantRef method) {
        return new OpCodeConst(INVOKESTATIC, method);
    }

    static OpCodeConst makeInvokevirtualSpecial(ConstantRef method, boolean isVirtual) {
        return new OpCodeConst(isVirtual ? INVOKEVIRTUAL : INVOKESPECIAL, method);
    }

    static OpCodeConst makeLdc(ConstantRef poolConst) {
        return new OpCodeConst(poolConst.isWide() ? LDC_W : LDC, poolConst);
    }

    static OpCodeConst makeNewCheckcast(ConstantRef classConst, boolean isNew) {
        return new OpCodeConst(isNew ? NEW : CHECKCAST, classConst);
    }

    static OpCodeConst makePutGetstatic(ConstantRef field, boolean isPut) {
        return new OpCodeConst(isPut ? PUTSTATIC : GETSTATIC, field);
    }

    int getLength(int curPc) {
        return op == INVOKEINTERFACE ? 5 : op == MULTIANEWARRAY ? 4 : op == BIPUSH || op == LDC || op == NEWARRAY ? 2 : 3;
    }

    void writeTo(OutputStream out) throws IOException {
        out.write(op);
        if (poolConst != null) {
            if (op == LDC) poolConst.writeAsByteTo(out); else {
                poolConst.writeTo(out);
                if (op == INVOKEINTERFACE || op == MULTIANEWARRAY) {
                    out.write(intArg);
                    if (op == INVOKEINTERFACE) out.write(0);
                }
            }
        } else {
            if (op == SIPUSH) writeShort(out, intArg); else out.write(intArg);
        }
    }

    boolean isEqualTo(OpByteCode other, int startIndex, int deltaIndex, int endIndex2, int[] indexRef, int[] deltaVarRef, int argSlots, Hashtable diffVarsSet) {
        if (!(other instanceof OpCodeConst)) return false;
        OpCodeConst opCodeConst = (OpCodeConst) other;
        return op == opCodeConst.op && intArg == opCodeConst.intArg && (poolConst == null || poolConst.isEqualTo(opCodeConst.poolConst));
    }

    boolean isCheckcast() {
        return op == CHECKCAST;
    }

    boolean isInvokeMethod() {
        return op >= INVOKEVIRTUAL && op <= INVOKEINTERFACE;
    }

    boolean isInvokestaticSpecial(boolean isStatic) {
        return isStatic ? op == INVOKESTATIC : op == INVOKESPECIAL;
    }

    boolean isInvokevirtual() {
        return op == INVOKEVIRTUAL;
    }

    boolean isLdc() {
        return op == LDC || op == LDC_W;
    }

    boolean isPutGetstatic(boolean isPut) {
        return isPut ? op == PUTSTATIC : op == GETSTATIC;
    }

    boolean isPutfield() {
        return op == PUTFIELD;
    }

    boolean isXConstZero() {
        return (op == BIPUSH || op == SIPUSH) && intArg == 0;
    }

    ConstantRef getConstRef() {
        return poolConst;
    }
}
