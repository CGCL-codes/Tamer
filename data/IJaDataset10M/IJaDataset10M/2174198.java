package net.virtualinfinity.atrobots.computer;

/**
 * An instruction which writes the second operand to the port specified by the first operand.
 *
 * @author Daniel Pitts
 */
public class WritePortInstruction extends Instruction {

    public WritePortInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.writePort();
    }
}
