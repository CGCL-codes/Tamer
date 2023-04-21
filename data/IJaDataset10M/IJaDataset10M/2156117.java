package net.virtualinfinity.atrobots.computer;

/**
 * An instruction which swaps the two operand values, and sets the swap register to
 * the first operand as a side effect.
 *
 * @author Daniel Pitts
 */
public class SwapInstruction extends Instruction {

    public SwapInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.getRegisters().getSwap().set(computer.getOperandValue(1));
        computer.setOperandValue(1, computer.getOperandValue(2));
        computer.setOperandValue(2, computer.getRegisters().getSwap().signed());
    }
}
