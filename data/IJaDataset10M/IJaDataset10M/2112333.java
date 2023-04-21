package simulator.processor.instructions.templates;

import main.NumberConversions;
import simulator.processor.StageIdentifier;
import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.InstructionField;
import simulator.processor.stages.StageID;
import simulator.processor.stages.StageMEM;
import simulator.processor.stages.StageWB;
import static main.SystemConstants.FLOAT_FIELD;
import static main.SystemConstants.INTEGER_FIELD;
import static simulator.processor.StageIdentifier.STAGE_MEM;
import static simulator.processor.instructions.InstructionState.IN_PROGRESS;

/**
 * Base class for all load memory access instruction operating on integer registers.
 */
public abstract class InstructionTypeI_fStore extends InstructionTypeI {

    public InstructionTypeI_fStore(Instruction instruction) {
        super(instruction);
        getField(RD).setSource(FLOAT_FIELD);
        getField(RS).setSource(INTEGER_FIELD);
    }

    public void executeID(StageID stage) {
        setState(IN_PROGRESS);
        InstructionField rd = getField(RD);
        InstructionField rs = getField(RS);
        checkRegisterAvailability(stage, rs, READ);
        checkRegisterAvailability(stage, rd, READ);
        if (getState() == IN_PROGRESS) {
            stage.<Float>readRegister(rs, this);
            stage.<Float>readRegister(rd, this);
        }
    }

    public void executeMEM(StageMEM stage) {
        int imm = NumberConversions.signExtendInt32(getFieldValue(IMM), 16);
        writeData(stage, this.<Integer>getFieldContent(RS) + imm, this.<Float>getFieldContent(RD));
    }

    public void executeWB(StageWB stage) {
    }

    public StageIdentifier getExecutionStage() {
        return STAGE_MEM;
    }

    public String toAssembly() {
        return String.format("%s 0x%x(r%d), f%d", assemblyName, getFieldValue(IMM), getFieldValue(RS), getFieldValue(RD));
    }

    /**
     * Writes data into memory.
     */
    protected abstract void writeData(StageMEM stage, int addr, float value);
}
