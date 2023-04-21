package org.fernwood.jbasic.opcodes;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.runtime.JBasicException;

/**
 * @author cole
 * 
 */
public class OpSETDYNVAR extends AbstractOpcode {

    /**
	 * Modify the current running context's dynamic variable creation flag.
	 * 
	 * @see org.fernwood.jbasic.opcodes.AbstractOpcode#execute(org.fernwood.jbasic.opcodes.InstructionContext)
	 */
    public void execute(final InstructionContext env) throws JBasicException {
        if (!env.instruction.integerValid) throw new JBasicException(Status.INVOPARG, "<missing integer>");
        int code = env.instruction.integerOperand;
        switch(code) {
            case 0:
                env.codeStream.fDynamicSymbolCreation = false;
                break;
            case 1:
                env.codeStream.fDynamicSymbolCreation = true;
                break;
            default:
                throw new JBasicException(Status.INVOPARG, code);
        }
    }
}
