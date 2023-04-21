package org.fernwood.jbasic.opcodes;

import org.fernwood.jbasic.JBasic;
import org.fernwood.jbasic.Loader;
import org.fernwood.jbasic.Permissions;
import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.runtime.JBasicException;

/**
 * @author cole
 * 
 */
public class OpLOADFILE extends AbstractOpcode {

    /**
	 * Load a symbol value on the stack
	 * 
	 * @see org.fernwood.jbasic.opcodes.AbstractOpcode#execute(org.fernwood.jbasic.opcodes.InstructionContext)
	 */
    public void execute(final InstructionContext env) throws JBasicException {
        String fname = env.pop().getString();
        String pname = fname.toUpperCase();
        boolean loadIF = (env.instruction.integerOperand == 1);
        if (loadIF) if (env.session.programs.find(pname) != null) return;
        env.session.checkPermission(Permissions.FILE_IO);
        String fsName;
        try {
            fsName = JBasic.userManager.makeFSPath(env.session, fname);
        } catch (JBasicException e) {
            throw new JBasicException(e.getStatus());
        }
        Status status = Loader.loadFile(env.session, fsName);
        if (status.failed()) throw new JBasicException(status);
    }
}
