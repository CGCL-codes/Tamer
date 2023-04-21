package org.fernwood.jbasic.opcodes;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.runtime.JBasicException;

/**
 * @author cole
 * 
 */
public class OpDEFFN extends AbstractOpcode {

    /**
	 * Execute the _DEFFN operand. The integer argument indicates the number
	 * of byte codes to skip over in the program stream, which have already
	 * been gathered up during link time.
	 * 
	 * @see org.fernwood.jbasic.opcodes.AbstractOpcode#execute(org.fernwood.jbasic.opcodes.InstructionContext)
	 */
    public void execute(final InstructionContext env) throws JBasicException {
        final int count = env.instruction.integerOperand;
        if (count < 1) throw new JBasicException(Status.FAULT, new Status(Status.INVOPARG, count));
        env.codeStream.programCounter += count;
        return;
    }
}
