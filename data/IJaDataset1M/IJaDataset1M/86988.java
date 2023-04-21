package com.amd.javalabs.tools.disasm.instructions.operandencoding.lhs;

import com.amd.javalabs.tools.disasm.InstructionBuildContext;
import com.amd.javalabs.tools.disasm.instructions.operandencoding.rhs.OperandEncodingRHS;
import com.amd.javalabs.tools.disasm.instructions.operandencoding.rhs.RegisterOperandEncodingRHS;
import com.amd.javalabs.tools.disasm.operands.Operand;

public class INDIR_REG extends OperandEncodingLHS implements OperandBuilder {

    public INDIR_REG() {
        super(Enum.INDIR_REG);
    }

    public Operand build(OperandEncodingRHS _rhs, InstructionBuildContext _ibc) {
        return (((RegisterOperandEncodingRHS) _rhs).buildRegisterFromReg(_ibc));
    }
}
