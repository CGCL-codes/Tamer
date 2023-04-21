package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * Class file for Int2float Instruction class.
 */
public final class Int2float extends Unary {

    /**
    * Constructor for Int2float.
    *
    * @param result
    * @param val
    */
    public Int2float(Operand result, Operand val) {
        super(result, val);
    }

    /**
    * Return the name of the instruction.
    */
    public String nameOf() {
        return "Int2float";
    }

    @Override
    public char getOpcode() {
        return Operators.Int2float;
    }
}
