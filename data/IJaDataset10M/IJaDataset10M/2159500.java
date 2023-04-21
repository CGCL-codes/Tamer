package net.virtualinfinity.atrobots.computer;

/**
 * An instruction which causes an unknown instruction signal on the computer bus.
 *
 * @author Daniel Pitts
 */
public class UnknownInstruction extends Instruction {

    public UnknownInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.unknownInstructionError(computer.getOperandValue(0));
    }
}
