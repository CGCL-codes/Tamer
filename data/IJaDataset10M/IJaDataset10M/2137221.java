package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * Class file for IntAstore Instruction class.
 */
public final class IntAstore extends AStore {

    /**
    * Constructor for IntAstore.
    *
    * @param value
    * @param array
    * @param index
    * @param field
    * @param guard
    */
    public IntAstore(Operand value, Operand array, Operand index, FieldOperand field, Operand guard) {
        super(value, array, index, field, guard);
    }

    /**
    * Return the name of the instruction.
    */
    public String nameOf() {
        return "IntAstore";
    }

    @Override
    public char getOpcode() {
        return Operators.IntAstore;
    }

    @Override
    public boolean isExplicitStore() {
        return true;
    }

    @Override
    public boolean isImplicitStore() {
        return true;
    }
}
