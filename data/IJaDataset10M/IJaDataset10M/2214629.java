package icebird.compiler.ncomp.vs;

import icebird.compiler.ncomp.X86Assembler;
import icebird.compiler.ncomp.X86Register.GPR;
import icebird.metadata.BasicType;

/**
 * @author Sergey Shulepoff[Knott]
 */
public final class LongItem extends DoubleWordItem {

    /**
	 * @param l
	 */
    public LongItem(Location l) {
        super(l);
    }

    protected DoubleWordItem cloneConstant() {
        if (!getLocation().isConstant()) throw new IllegalStateException();
        LongConstantLocation l = (LongConstantLocation) getLocation();
        return new LongItem(ConstLocation.createLConst(l.getValue()));
    }

    protected void loadConstant(GPR lsb, GPR msb) {
        if (!getLocation().isConstant()) throw new IllegalStateException();
        long v = ((LongConstantLocation) getLocation()).getValue();
        X86Assembler asm = X86Assembler.getAsm();
        if (v == 0) {
            asm.clear(lsb);
            asm.clear(msb);
        } else {
            asm.move_const(lsb, (int) (v & 0xFFFFFFFFL));
            asm.move_const(msb, (int) ((v >>> 32) & 0xFFFFFFFFL));
        }
    }

    protected void popFPU(GPR r, int disp) {
        X86Assembler asm = X86Assembler.getAsm();
        asm.fistp64(r, disp);
    }

    protected void pushConstant() {
        if (!getLocation().isConstant()) throw new IllegalStateException();
        long v = ((LongConstantLocation) getLocation()).getValue();
        X86Assembler asm = X86Assembler.getAsm();
        asm.push((int) ((v >>> 32) & 0xFFFFFFFFL));
        asm.push((int) (v & 0xFFFFFFFFL));
    }

    protected void pushFPU(GPR r, int disp) {
        X86Assembler asm = X86Assembler.getAsm();
        asm.fild64(r, disp);
    }

    @Override
    public BasicType getType() {
        return BasicType.Long;
    }
}
