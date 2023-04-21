package net.virtualinfinity.atrobots.computer;

/**
 * An instructions which reads a value from a port specified by the second operand,
 * and stores that value into the first operand.
 *
 * @author Daniel Pitts
 */
public class ReadPortInstruction extends Instruction {

    public ReadPortInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.readPort();
    }
}
