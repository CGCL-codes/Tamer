package org.fernwood.jbasic.opcodes;

import org.fernwood.jbasic.JBasic;
import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.runtime.SymbolTable;
import org.fernwood.jbasic.value.Value;

/**
 * @author cole
 * 
 */
public class OpSTOR extends AbstractOpcode {

    /**
	 * Store top stack item in a variable.
	 * 
	 * @see org.fernwood.jbasic.opcodes.AbstractOpcode#execute(org.fernwood.jbasic.opcodes.InstructionContext)
	 */
    public void execute(final InstructionContext env) throws JBasicException {
        Value value1 = env.popForUpdate();
        final int argc = env.instruction.integerValid ? env.instruction.integerOperand : 0;
        SymbolTable localTable = env.localSymbols;
        if (argc != 0) if (argc == -1) localTable = env.session.globals(); else if (argc == -2) localTable = JBasic.rootTable; else if (argc == -3) localTable = env.session.macroTable; else for (int ix = 0; ix < argc; ix++) if (localTable.parentTable != null) localTable = localTable.parentTable;
        if (!env.codeStream.fDynamicSymbolCreation && localTable.localReference(env.instruction.stringOperand) == null) throw new JBasicException(Status.UNKVAR, env.instruction.stringOperand);
        if (localTable.fRootTable) localTable.insertSynchronized(env.instruction.stringOperand, value1); else localTable.insert(env.instruction.stringOperand, value1);
    }
}
