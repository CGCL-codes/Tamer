package com.amd.javalabs.tools.disasm.instructions.operandencoding.lhs;

import com.amd.javalabs.tools.disasm.InstructionBuildContext;
import com.amd.javalabs.tools.disasm.instructions.operandencoding.rhs.ImmediateBuilder;
import com.amd.javalabs.tools.disasm.instructions.operandencoding.rhs.OperandEncodingRHS;
import com.amd.javalabs.tools.disasm.operands.Operand;

class I extends OperandEncodingLHS implements OperandBuilder {

    public I() {
        super(Enum.I);
    }

    public Operand build(OperandEncodingRHS _rhs, InstructionBuildContext _ibc) {
        return (((ImmediateBuilder) _rhs).buildImmediate(_ibc));
    }

    public String getDescription() {
        return ("Immediate value.");
    }
}
