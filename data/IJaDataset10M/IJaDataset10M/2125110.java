package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * Class file for PutField Instruction class.
 */
public final class PutField extends PutFieldParent {

    /**
    * Constructor for PutField.
    *
    * @param value
    * @param ref
    * @param offset
    * @param field
    * @param guard
    */
    public PutField(Operand value, Operand ref, Operand offset, FieldOperand field, Operand guard) {
        super(value, ref, offset, field, guard);
    }

    /**
    * Return the name of the instruction.
    */
    public String nameOf() {
        return "PutField";
    }

    @Override
    public char getOpcode() {
        return Operators.PutField;
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
