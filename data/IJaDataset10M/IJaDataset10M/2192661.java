package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * Class file for MustImplementInterface Instruction class.
 */
public final class MustImplementInterface extends TypeCheck {

    /**
    * Constructor for MustImplementInterface.
    *
    * @param result
    * @param ref
    * @param type
    * @param guard
    */
    public MustImplementInterface(Operand result, Operand ref, TypeOperand type, Operand guard) {
        super(result, ref, type, guard);
    }

    /**
    * Constructor for MustImplementInterface without option parameter.
    *
    * @param result
    * @param ref
    * @param type
    */
    public MustImplementInterface(Operand result, Operand ref, TypeOperand type) {
        super(result, ref, type, null);
    }

    /**
    * Return the name of the instruction.
    */
    public String nameOf() {
        return "MustImplementInterface";
    }

    @Override
    public char getOpcode() {
        return Operators.MustImplementInterface;
    }

    @Override
    public boolean isPEI() {
        return true;
    }

    @Override
    public boolean isGCPoint() {
        return true;
    }
}
