package simulator.processor.instructions.templates;

import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.InstructionField;
import simulator.processor.stages.StageEX;
import simulator.processor.stages.StageID;
import simulator.processor.stages.StageMEM;
import simulator.processor.stages.StageWB;
import static main.SystemConstants.FLOAT_ADDER_LATENCY;
import static main.SystemConstants.FLOAT_FIELD;
import static simulator.processor.StageIdentifier.*;
import static simulator.processor.instructions.InstructionState.IN_PROGRESS;

/**
 * All R-type coded instruction working with floating point values.
 */
public abstract class InstructionTypeR_fALU extends InstructionTypeR {

    public InstructionTypeR_fALU(Instruction instruction) {
        super(instruction);
        executionData.setStageLatency(STAGE_EX, FLOAT_ADDER_LATENCY);
        executionData.setStageLatency(STAGE_MEM, 0);
        executionData.setEXPart(PART_DX);
        getField(RD).setSource(FLOAT_FIELD);
        getField(RS1).setSource(FLOAT_FIELD);
        getField(RS2).setSource(FLOAT_FIELD);
    }

    public void executeID(StageID stage) {
        setState(IN_PROGRESS);
        InstructionField rd = getField(RD);
        InstructionField rs1 = getField(RS1);
        InstructionField rs2 = getField(RS2);
        checkRegisterAvailability(stage, rs1, READ);
        checkRegisterAvailability(stage, rs2, READ);
        checkRegisterAvailability(stage, rd, WRITE);
        if (getState() == IN_PROGRESS) {
            stage.reserveRegister(rd, this);
            stage.<Float>readRegister(rs1, this);
            stage.<Float>readRegister(rs2, this);
        }
    }

    public void executeEX(StageEX stage) {
        float rs1 = this.<Float>getFieldContent(RS1);
        float rs2 = this.<Float>getFieldContent(RS2);
        float rd = computeResult(rs1, rs2);
        this.<Float>setFieldContent(RD, rd);
    }

    public void executeMEM(StageMEM stage) {
    }

    public void executeWB(StageWB stage) {
        InstructionField rd = getField(RD);
        stage.<Float>writeRegister(rd);
        stage.unreserveRegister(rd, this);
    }

    public String toAssembly() {
        return String.format("%s f%d, f%d, f%d", assemblyName, getFieldValue(RD), getFieldValue(RS1), getFieldValue(RS2));
    }

    protected abstract float computeResult(float rs1, float rs2);
}
