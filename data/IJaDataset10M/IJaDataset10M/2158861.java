package net.virtualinfinity.atrobots.computer;

/**
 * An instruction which consumes the number of CPU cycles equal to the first operand.
 *
 * @author Daniel Pitts
 */
public class DelayInstruction extends Instruction {

    public DelayInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.consumeCycles(computer.getOperandValue(1));
    }
}
