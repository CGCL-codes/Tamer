package emulator.hardware.nmos6502.operands;

import emulator.EmulatorException;
import emulator.hardware.nmos6502.Operand;

public class OperandException extends EmulatorException {

    Operand operand;

    OperandException(String text, Operand operand) {
        super(text);
        this.operand = operand;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = -3933934572656719164L;
}
