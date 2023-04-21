package simulator.processor.instructions.definitions;

import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.InstructionField;
import simulator.processor.instructions.templates.InstructionTypeR_dALU;
import simulator.processor.stages.StageEX;
import simulator.processor.stages.StageID;
import static simulator.processor.StageIdentifier.PART_IX;
import static simulator.processor.StageIdentifier.STAGE_EX;
import static simulator.processor.instructions.InstructionState.IN_PROGRESS;

public class InstructionTypeR_MOVD extends InstructionTypeR_dALU {

    public InstructionTypeR_MOVD(Instruction instruction) {
        super(instruction);
        executionData.setEXPart(PART_IX);
        executionData.setStageLatency(STAGE_EX, 2);
        assemblyName = "movd";
    }

    public void executeID(StageID stage) {
        setState(IN_PROGRESS);
        InstructionField rd = getField(RD);
        InstructionField rs1 = getField(RS1);
        checkRegisterAvailability(stage, rs1, READ);
        checkRegisterAvailability(stage, rd, WRITE);
        if (getState() == IN_PROGRESS) {
            stage.reserveRegister(rd, this);
            stage.<Double>readRegister(rs1, this);
        }
    }

    public void executeEX(StageEX stage) {
        this.<Double>setFieldContent(RD, this.<Double>getFieldContent(RS1));
    }

    public String toAssembly() {
        return String.format("%s d%2d, d%2d", assemblyName, getFieldValue(RD), getFieldValue(RS1));
    }

    protected double computeResult(double rs1, double rs2) {
        throw new IllegalStateException("Not allowed to be called for instruction: " + toAssembly());
    }
}
