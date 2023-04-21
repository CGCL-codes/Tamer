package simulator.processor.instructions.definitions;

import main.NumberConversions;
import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.templates.InstructionTypeI_iLoad;
import simulator.processor.stages.StageMEM;

public class InstructionTypeI_LW extends InstructionTypeI_iLoad {

    public InstructionTypeI_LW(Instruction instruction) {
        super(instruction);
        assemblyName = "lw";
    }

    protected int readData(StageMEM stage, int addr) {
        byte[] values = new byte[4];
        for (int i = 0; i < 4; i++) {
            values[i] = stage.readByte(addr + i);
        }
        return NumberConversions.toInt32(values);
    }
}
