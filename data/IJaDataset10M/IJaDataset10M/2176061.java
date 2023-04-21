package simulator.processor.instructions.templates;

import simulator.processor.instructions.Instruction;
import simulator.processor.stages.StageEX;
import simulator.processor.stages.StageID;
import simulator.processor.stages.StageMEM;
import simulator.processor.stages.StageWB;
import static main.SystemConstants.FLOAT_CONVERT_LATENCY;
import static simulator.processor.StageIdentifier.STAGE_EX;
import static simulator.processor.StageIdentifier.STAGE_MEM;

/**
 * Base class for conversion instructions.
 * <p/>
 * <p>
 * T is the type of result
 * <p>
 * U is the type of source
 * </p>
 */
public abstract class InstructionTypeR_fConvert extends InstructionTypeR {

    public InstructionTypeR_fConvert(Instruction instruction) {
        super(instruction);
        executionData.setStageLatency(STAGE_EX, FLOAT_CONVERT_LATENCY);
        executionData.setStageLatency(STAGE_MEM, 0);
    }

    public abstract void executeID(StageID stage);

    public abstract void executeEX(StageEX stage);

    public void executeMEM(StageMEM stage) {
    }

    public abstract void executeWB(StageWB stage);

    public abstract String toAssembly();
}
