package simulator.processor.instructions.definitions;

import main.NumberConversions;
import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.templates.InstructionTypeI_iStore;
import simulator.processor.stages.StageMEM;

public class InstructionTypeI_SB extends InstructionTypeI_iStore {

    public InstructionTypeI_SB(Instruction instruction) {
        super(instruction);
        assemblyName = "sb";
    }

    protected void writeData(StageMEM stage, int addr, int value) {
        byte[] values = NumberConversions.toByteArray(value);
        stage.writeByte(addr, values[0]);
    }
}
