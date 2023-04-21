package simulator.processor.instructions.definitions;

import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.templates.InstructionTypeR_iALU;

public class InstructionTypeR_OR extends InstructionTypeR_iALU {

    public InstructionTypeR_OR(Instruction instruction) {
        super(instruction);
        assemblyName = "or";
    }

    protected int computeResult(int rs1, int rs2) {
        return rs1 | rs2;
    }
}
