package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * Class file for IntOr Instruction class.
 */
public final class IntOr extends Binary {

    /**
    * Constructor for IntOr.
    *
    * @param result
    * @param val1
    * @param val2
    */
    public IntOr(Operand result, Operand val1, Operand val2) {
        super(result, val1, val2);
    }

    /**
    * Return the name of the instruction.
    */
    public String nameOf() {
        return "IntOr";
    }

    @Override
    public char getOpcode() {
        return Operators.IntOr;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }
}
