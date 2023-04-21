package simulator.processor.instructions.definitions;

import simulator.processor.instructions.Instruction;
import simulator.processor.instructions.templates.InstructionTypeR_dCompare;

public class InstructionTypeR_NED extends InstructionTypeR_dCompare {

    public InstructionTypeR_NED(Instruction instruction) {
        super(instruction);
        assemblyName = "ned";
    }

    protected int computeFPSR(double rs1, double rs2) {
        return (Double.compare(rs1, rs2) != 0) ? 1 : 0;
    }
}
