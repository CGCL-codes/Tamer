package simulator.processor.instructions.definitions;

import simulator.processor.StageIdentifier;
import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.InstructionField;
import simulator.processor.instructions.templates.InstructionTypeI;
import simulator.processor.stages.StageID;
import simulator.processor.stages.StageWB;
import static main.SystemConstants.FPSR;
import static main.SystemConstants.SPECIAL_FIELD;
import static simulator.processor.StageIdentifier.STAGE_ID;
import static simulator.processor.instructions.InstructionState.IN_PROGRESS;

public class InstructionTypeI_BFPT extends InstructionTypeI {

    private InstructionField fpsr;

    public InstructionTypeI_BFPT(Instruction instruction) {
        super(instruction);
        fpsr = new InstructionField(FPSR_FIELD, FPSR);
        fpsr.setSource(SPECIAL_FIELD);
        assemblyName = "bfpt";
    }

    public void executeID(StageID stage) {
        setState(IN_PROGRESS);
        checkRegisterAvailability(stage, fpsr, READ);
        if (getState() == IN_PROGRESS) {
            stage.<Integer>readRegister(fpsr, this);
            if (fpsr.<Integer>getContent() != 0) {
                int imm = getFieldValue(IMM);
                stage.processBranch(address + getByteLength() + imm);
            }
        }
    }

    public void executeWB(StageWB stage) {
    }

    public StageIdentifier getExecutionStage() {
        return STAGE_ID;
    }

    public String toAssembly() {
        return String.format("%s 0x%04x", assemblyName, getFieldValue(IMM));
    }
}
