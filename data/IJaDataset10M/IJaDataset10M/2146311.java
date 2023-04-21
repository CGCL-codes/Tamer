package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * Class file for UshortLoad Instruction class.
 */
public final class UshortLoad extends Load {

    /**
    * Constructor for UshortLoad.
    *
    * @param result
    * @param address
    * @param offset
    * @param field
    * @param guard
    */
    public UshortLoad(Operand result, Operand address, Operand offset, FieldOperand field, Operand guard) {
        super(result, address, offset, field, guard);
    }

    /**
    * Constructor for UshortLoad without option parameter.
    *
    * @param result
    * @param address
    * @param offset
    * @param field
    */
    public UshortLoad(Operand result, Operand address, Operand offset, FieldOperand field) {
        super(result, address, offset, field, null);
    }

    /**
    * Return the name of the instruction.
    */
    public String nameOf() {
        return "UshortLoad";
    }

    @Override
    public char getOpcode() {
        return Operators.UshortLoad;
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
