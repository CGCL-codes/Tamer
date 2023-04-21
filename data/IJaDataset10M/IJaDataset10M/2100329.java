package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * Class file for RefStore Instruction class.
 */
public final class RefStore extends Store {

    /**
    * Constructor for RefStore.
    *
    * @param value
    * @param address
    * @param offset
    * @param field
    * @param guard
    */
    public RefStore(Operand value, Operand address, Operand offset, FieldOperand field, Operand guard) {
        super(value, address, offset, field, guard);
    }

    /**
    * Constructor for RefStore without option parameter.
    *
    * @param value
    * @param address
    * @param offset
    * @param field
    */
    public RefStore(Operand value, Operand address, Operand offset, FieldOperand field) {
        super(value, address, offset, field, null);
    }

    /**
    * Return the name of the instruction.
    */
    public String nameOf() {
        return "RefStore";
    }

    @Override
    public char getOpcode() {
        return Operators.RefStore;
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
