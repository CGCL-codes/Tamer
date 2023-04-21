package x10me.opt.ir;

import x10me.opt.ir.operand.*;

/**
 * Class file for LongIfcmp Instruction class.
 */
public final class LongIfcmp extends IfCmp {

    /**
    * Constructor for LongIfcmp.
    *
    * @param guardResult
    * @param val1
    * @param val2
    * @param cond
    * @param target
    * @param branchProfile
    */
    public LongIfcmp(RegisterOperand guardResult, Operand val1, Operand val2, ConditionOperand cond, BranchOperand target, BranchProfileOperand branchProfile) {
        super(guardResult, val1, val2, cond, target, branchProfile);
    }

    /**
    * Return the name of the instruction.
    */
    public String nameOf() {
        return "LongIfcmp";
    }

    @Override
    public char getOpcode() {
        return Operators.LongIfcmp;
    }

    @Override
    public boolean isBranch() {
        return true;
    }

    @Override
    public boolean isConditionalBranch() {
        return true;
    }
}
