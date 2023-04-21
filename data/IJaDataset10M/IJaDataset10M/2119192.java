package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * Class file for RefAload Instruction class.
 */
public final class RefAload extends ALoad {

    /**
    * Constructor for RefAload.
    *
    * @param result
    * @param array
    * @param index
    * @param field
    * @param guard
    */
    public RefAload(Operand result, Operand array, Operand index, FieldOperand field, Operand guard) {
        super(result, array, index, field, guard);
    }

    /**
    * Return the name of the instruction.
    */
    public String nameOf() {
        return "RefAload";
    }

    @Override
    public char getOpcode() {
        return Operators.RefAload;
    }

    @Override
    public boolean isExplicitLoad() {
        return true;
    }

    @Override
    public boolean isImplicitLoad() {
        return true;
    }
}
