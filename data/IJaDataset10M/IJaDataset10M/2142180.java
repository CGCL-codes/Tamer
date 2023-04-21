package simulator.processor.instructions.templates;

import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.InstructionField;
import simulator.processor.stages.StageEX;
import simulator.processor.stages.StageID;
import simulator.processor.stages.StageMEM;
import simulator.processor.stages.StageWB;
import static main.SystemConstants.DOUBLE_FLOAT_FIELD;
import static main.SystemConstants.FLOAT_ADDER_LATENCY;
import static simulator.processor.StageIdentifier.*;
import static simulator.processor.instructions.InstructionState.IN_PROGRESS;

/**
 * All R-type coded instruction working with floating point values.
 */
public abstract class InstructionTypeR_dALU extends InstructionTypeR {

    public InstructionTypeR_dALU(Instruction instruction) {
        super(instruction);
        executionData.setStageLatency(STAGE_EX, FLOAT_ADDER_LATENCY);
        executionData.setStageLatency(STAGE_MEM, 0);
        executionData.setEXPart(PART_DX);
        getField(RD).setSource(DOUBLE_FLOAT_FIELD);
        getField(RS1).setSource(DOUBLE_FLOAT_FIELD);
        getField(RS2).setSource(DOUBLE_FLOAT_FIELD);
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
            stage.<Double>readRegister(rs1, this);
            stage.<Double>readRegister(rs2, this);
        }
    }

    public void executeEX(StageEX stage) {
        double rs1 = this.<Double>getFieldContent(RS1);
        double rs2 = this.<Double>getFieldContent(RS2);
        double rd = computeResult(rs1, rs2);
        this.<Double>setFieldContent(RD, rd);
    }

    public void executeMEM(StageMEM stage) {
    }

    public void executeWB(StageWB stage) {
        InstructionField rd = getField(RD);
        stage.<Double>writeRegister(rd);
        stage.unreserveRegister(rd, this);
    }

    public String toAssembly() {
        return String.format("%s d%d, d%d, d%d", assemblyName, getFieldValue(RD), getFieldValue(RS1), getFieldValue(RS2));
    }

    protected abstract double computeResult(double rs1, double rs2);
}
