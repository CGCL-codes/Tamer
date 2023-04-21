package net.virtualinfinity.atrobots.computer;

/**
 * An instruction which invokes the interrupt specified by the first operand.
 *
 * @author Daniel Pitts
 */
public class CallInterruptInstruction extends Instruction {

    public CallInterruptInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.callInterrupt();
    }
}
