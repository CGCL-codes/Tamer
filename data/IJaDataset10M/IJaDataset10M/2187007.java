package net.sourceforge.cobertura.util;

import org.apache.bcel.generic.BIPUSH;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.LDC;

public abstract class InstructionListHelper {

    public static Instruction push(ConstantPoolGen cg, int i) {
        if ((i >= -1) && (i <= 5)) {
            return new ICONST(i);
        } else if ((i >= Byte.MIN_VALUE) && (i <= Byte.MAX_VALUE)) {
            return new BIPUSH((byte) i);
        }
        return new LDC(cg.addInteger(i));
    }
}
