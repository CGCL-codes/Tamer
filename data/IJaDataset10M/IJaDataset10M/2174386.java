package simulator.processor.instructions.definitions;

import main.NumberConversions;
import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.templates.InstructionTypeI;
import simulator.processor.stages.StageEX;

public class InstructionTypeI_SLEI extends InstructionTypeI {

    public InstructionTypeI_SLEI(Instruction instruction) {
        super(instruction);
        assemblyName = "slei";
    }

    public void executeEX(StageEX stage) {
        int rs = this.<Integer>getFieldContent(RS);
        int imm = NumberConversions.signExtendInt32(getFieldValue(IMM), FIELD_BITS[IMM]);
        int result = (rs <= imm) ? 1 : 0;
        this.<Integer>setFieldContent(RD, result);
    }
}
