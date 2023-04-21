package com.ibm.JikesRVM.OSR;

import com.ibm.JikesRVM.classloader.*;
import com.ibm.JikesRVM.opt.ir.*;

/**
 * An OSR_LocalRegPair keeps the type information and localtion of 
 * a local variable/stack slot from byte code to machine code.
 *
 * @author Feng Qian
 */
public class OSR_LocalRegPair implements OSR_Constants {

    public int kind;

    public int num;

    public byte typeCode;

    public OPT_Operand operand;

    public int valueType;

    public int value;

    public OSR_LocalRegPair _otherHalf;

    public OSR_LocalRegPair(int kind, int num, byte type, OPT_Operand op) {
        this.kind = kind;
        this.num = num;
        this.typeCode = type;
        this.operand = op;
    }

    public OSR_LocalRegPair copy() {
        return new OSR_LocalRegPair(kind, num, typeCode, operand);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("(");
        buf.append((char) kind);
        buf.append(num + " , ");
        char tcode = (char) typeCode;
        buf.append(tcode + " , ");
        buf.append(valueType + " , ");
        buf.append(value + " , ");
        buf.append(operand + ")");
        if (tcode == LongTypeCode) {
            buf.append("(" + _otherHalf.valueType + " , ");
            buf.append(_otherHalf.value + ")");
        }
        return buf.toString();
    }
}
